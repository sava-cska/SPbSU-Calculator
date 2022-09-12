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
		logger.Debugf("HandleEvaluationsList - Called URI %s", request.RequestURI)
		var user ListRequest
		if errJSON := utils.ParseBody(logger, interface{}(&user), writer, request); errJSON != nil {
			return
		}

		evals, errList := storage.Evaluations().List(user.UserUid)
		if errList != nil {
			utils.HandleError(logger, writer, http.StatusInternalServerError, "Can't get list of evaluations.", errList)
			return
		}

		evaluationHistory := make([]EvaluationHistory, len(evals))
		for idx := 0; idx < len(evals); idx++ {
			evaluationHistory[idx] = EvaluationHistory{
				Evaluation: decode(evals[idx].Evaluation),
				Result:     evals[idx].Result,
			}
		}

		response := ListResponse{
			Response: evaluationHistory,
		}

		respJSON, errRespJSON := json.Marshal(response)
		if errRespJSON != nil {
			utils.HandleError(logger, writer, http.StatusInternalServerError, "Can't create JSON object from data.", errRespJSON)
			return
		}

		writer.Write(respJSON)
	}
}
