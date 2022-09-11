package evaluations

import (
	"encoding/json"
	"github.com/sava-cska/SPbSU-Calculator/internal/app/storage"
	"github.com/sava-cska/SPbSU-Calculator/internal/utils"
	"github.com/sirupsen/logrus"
	"net/http"
)

func HandleEvaluationsList(logger *logrus.Logger, storage *storage.Storage) http.HandlerFunc {

	return func(writer http.ResponseWriter, request *http.Request) {
		logger.Debugf("GET - Called URI %s", request.RequestURI)
		var user ListRequest
		if errJSON := utils.ParseBody(logger, interface{}(&user), writer, request); errJSON != nil {
			return
		}

		evals, errList := storage.Evaluations().List(user.UserUid)
		if errList != nil {
			utils.HandleError(logger, writer, http.StatusInternalServerError, "Can't get list of evaluations.", errList)
			return
		}

		getResponse := make([]ListResponse, len(evals))
		for idx := 0; idx < len(evals); idx++ {
			getResponse[idx] = ListResponse{
				EvaluationHistory: EvaluationHistory{
					Evaluation: decode(evals[idx].Evaluation),
				},
			}
		}

		respJSON, errRespJSON := json.Marshal(getResponse)
		if errRespJSON != nil {
			utils.HandleError(logger, writer, http.StatusInternalServerError, "Can't create JSON object from data.", errRespJSON)
			return
		}

		writer.Write(respJSON)
	}
}
