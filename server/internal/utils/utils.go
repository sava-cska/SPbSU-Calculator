package utils

import (
	"encoding/json"
	"github.com/sirupsen/logrus"
	"io"
	"net/http"
)

func HandleError(logger *logrus.Logger, writer http.ResponseWriter, code int, reason string, err error) {
	logger.Errorln(reason, err)
	writer.WriteHeader(code)
	writer.Write([]byte(reason))
}

func ParseBody(logger *logrus.Logger, value any, writer http.ResponseWriter, request *http.Request) error {
	data, errReq := io.ReadAll(request.Body)
	if errReq != nil {
		HandleError(logger, writer, http.StatusBadRequest, "Can't read request body.", errReq)
		return errReq
	}

	if errJSON := json.Unmarshal(data, value); errJSON != nil {
		HandleError(logger, writer, http.StatusBadRequest, "Can't convert body json to correct struct.", errJSON)
		return errJSON
	}

	return nil
}
