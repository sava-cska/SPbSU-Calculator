package server

import (
	"github.com/gorilla/mux"
	"github.com/sava-cska/SPbSU-Calculator/internal/app/model"
	"github.com/sava-cska/SPbSU-Calculator/internal/app/storage"
	"github.com/sirupsen/logrus"
	"io"
	"math/rand"
	"net/http"
	"strconv"
	"strings"
)

type Server struct {
	config  *Config
	logger  *logrus.Logger
	router  *mux.Router
	storage *storage.Storage
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
		server.logger.Debugf("Called URI %s", request.RequestURI)
		io.WriteString(writer, "Hello")
	}
}

func (server *Server) handleEvaluationsGet() http.HandlerFunc {
	return func(writer http.ResponseWriter, request *http.Request) {
		server.logger.Debugf("Called URI %s", request.RequestURI)
		evals, _ := server.storage.Evaluations().List(request.RequestURI)
		server.logger.Debugf(strings.Join(evals, " "))
		io.WriteString(writer, strings.Join(evals, " "))
		// ...
	}
}

func (server *Server) handleEvaluationsPost() http.HandlerFunc {
	return func(writer http.ResponseWriter, request *http.Request) {
		server.logger.Debugf("Called URI %s", request.RequestURI)
		server.storage.Evaluations().Upsert(
			&model.Evaluation{
				UserId:            request.RequestURI,
				EncodedEvaluation: request.RequestURI + strconv.FormatInt(rand.Int63(), 10),
			})
		// ...
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
