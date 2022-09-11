package server

import (
	"encoding/json"
	"fmt"
	"io"
	"net/http"

	"github.com/Knetic/govaluate"
	"github.com/gorilla/mux"
	"github.com/sava-cska/SPbSU-Calculator/internal/app/model"
	"github.com/sava-cska/SPbSU-Calculator/internal/app/storage"
	"github.com/sirupsen/logrus"
)

type Server struct {
	config  *Config
	logger  *logrus.Logger
	router  *mux.Router
	storage *storage.Storage
}

type PostEvalResponse struct {
	Result string
}

type GetEvalResponse struct {
	Evaluation []model.Token
	Result     string
}

// New Upsert new instance of server
func New(config *Config) *Server {
	return &Server{
		config: config,
		logger: logrus.New(),
		router: mux.NewRouter(),
	}
}

// Start Configure and start listening
func (server *Server) Start() error {
	if err := server.configureLogger(); err != nil {
		return err
	}
	server.configureRouter()
	if err := server.configureStore(); err != nil {
		return err
	}
	server.logger.Info("Server is up")
	return http.ListenAndServe(server.config.BindAddress, server.router)
}

func (server *Server) configureLogger() error {
	level, err := logrus.ParseLevel(server.config.LogLevel)
	if err != nil {
		return err
	}
	server.logger.SetLevel(level)
	return nil
}

func (server *Server) configureRouter() {
	server.router.HandleFunc("/hello", server.handleHello()).Methods(http.MethodGet)
	server.router.HandleFunc("/evaluations", server.handleEvaluationsGet()).Methods(http.MethodGet)
	server.router.HandleFunc("/evaluations", server.handleEvaluationsPost()).Methods(http.MethodPost)
}

func (server *Server) handleHello() http.HandlerFunc {
	// Here we can add something, that is the reason behind making functor
	return func(writer http.ResponseWriter, request *http.Request) {
		server.logger.Debugf("GET - Called URI %s", request.RequestURI)
		io.WriteString(writer, "Hello")
	}
}

func (server *Server) handleError(writer http.ResponseWriter, code int, reason string, err error) {
	server.logger.Errorln(reason, err)
	writer.WriteHeader(code)
	writer.Write([]byte(reason))
}

func (server *Server) parseJSON(value any, writer http.ResponseWriter, request *http.Request) error {
	data, errReq := io.ReadAll(request.Body)
	if errReq != nil {
		server.handleError(writer, http.StatusBadRequest, "Can't read request body.", errReq)
		return errReq
	}

	if errJSON := json.Unmarshal(data, value); errJSON != nil {
		server.handleError(writer, http.StatusBadRequest, "Can't convert body json to correct struct.", errJSON)
		return errJSON
	}

	return nil
}

func (server *Server) handleEvaluationsGet() http.HandlerFunc {
	return func(writer http.ResponseWriter, request *http.Request) {
		server.logger.Debugf("GET - Called URI %s", request.RequestURI)
		var user model.Evaluation
		if errJSON := server.parseJSON(interface{}(&user), writer, request); errJSON != nil {
			return
		}

		evals, errList := server.storage.Evaluations().List(user.UserUid)
		if errList != nil {
			server.handleError(writer, http.StatusInternalServerError, "Can't get list of evaluations.", errList)
			return
		}

		getResponse := make([]GetEvalResponse, len(evals))
		for idx := 0; idx < len(evals); idx++ {
			getResponse[idx] = GetEvalResponse{Evaluation: evals[idx].Evaluation, Result: evals[idx].Result}
		}

		respJSON, errRespJSON := json.Marshal(getResponse)
		if errRespJSON != nil {
			server.handleError(writer, http.StatusInternalServerError, "Can't create JSON object from data.", errRespJSON)
			return
		}

		writer.Write(respJSON)
	}
}

func (server *Server) handleEvaluationsPost() http.HandlerFunc {
	return func(writer http.ResponseWriter, request *http.Request) {
		server.logger.Debugf("POST - Called URI %s", request.RequestURI)

		var expression model.Evaluation
		if errJSON := server.parseJSON(interface{}(&expression), writer, request); errJSON != nil {
			return
		}

		stringExpr := expression.ToString()
		formula, errCreateFormula := govaluate.NewEvaluableExpression(stringExpr)
		if errCreateFormula != nil {
			server.handleError(writer, http.StatusBadRequest,
				fmt.Sprintf("Can't create formula from string representation %s.", stringExpr), errCreateFormula)
			return
		}

		result, errEvaluate := formula.Evaluate(nil)
		if errEvaluate != nil {
			server.handleError(writer, http.StatusBadRequest, fmt.Sprintf("Can't evaluate formula %s.", stringExpr), errEvaluate)
			return
		}

		expression.Result = fmt.Sprintf("%v", result)
		server.logger.Debugf("Result of the expression %s equals %s", stringExpr, expression.Result)

		server.storage.Evaluations().Upsert(&expression)
		postResponse := PostEvalResponse{Result: expression.Result}
		respJSON, errRespJSON := json.Marshal(postResponse)
		if errRespJSON != nil {
			server.handleError(writer, http.StatusInternalServerError, "Can't create JSON object from data.", errRespJSON)
			return
		}

		writer.Write(respJSON)
	}
}

func (server *Server) configureStore() error {
	// TODO MSCSE-5
	//if err := storage.Open(); err != nil {
	//	return err
	//}
	//
	server.storage = storage.New(server.config.Storage)

	return nil
}
