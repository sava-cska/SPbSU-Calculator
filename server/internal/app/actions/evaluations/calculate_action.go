package evaluations

import (
	"encoding/json"
	"fmt"
	"github.com/Knetic/govaluate"
	"github.com/sava-cska/SPbSU-Calculator/internal/app/storage"
	"github.com/sava-cska/SPbSU-Calculator/internal/utils"
	"github.com/sirupsen/logrus"
	"net/http"
)

func HandleEvaluationsCalculate(logger *logrus.Logger, storage *storage.Storage) http.HandlerFunc {
	evaluationsDAO := storage.Evaluations()

	return func(writer http.ResponseWriter, request *http.Request) {
		logger.Debugf("POST - Called URI %s", request.RequestURI)

		var expression CalculateRequest
		if errJSON := utils.ParseJSON(logger, interface{}(&expression), writer, request); errJSON != nil {
			return
		}

		stringExpr := toString(expression.Evaluation)
		formula, errCreateFormula := govaluate.NewEvaluableExpression(stringExpr)
		if errCreateFormula != nil {
			utils.HandleError(logger, writer, http.StatusBadRequest,
				fmt.Sprintf("Can't create formula from string representation %s.", stringExpr), errCreateFormula)
			return
		}

		rawResult, errEvaluate := formula.Evaluate(nil)
		if errEvaluate != nil {
			utils.HandleError(logger, writer, http.StatusBadRequest, fmt.Sprintf("Can't evaluate formula %s.", stringExpr), errEvaluate)
			return
		}

		result := fmt.Sprintf("%v", rawResult)
		logger.Debugf("Result of the expression %s equals %s", stringExpr, result)

		evaluationsDAO.Upsert(expression.UserUid, encode(expression.Evaluation), result)

		postResponse := CalculateResponse{Result: result}
		respJSON, errRespJSON := json.Marshal(postResponse)
		if errRespJSON != nil {
			utils.HandleError(logger, writer, http.StatusInternalServerError, "Can't create JSON object from data.", errRespJSON)
			return
		}

		writer.Write(respJSON)
	}
}
