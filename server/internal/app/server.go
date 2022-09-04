package server

import (
	"github.com/gorilla/mux"
	"github.com/sirupsen/logrus"
	"io"
	"net/http"
)

type Server struct {
	config *Config
	logger *logrus.Logger
	router *mux.Router
}

// New Create new instance of server
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
	routeToHandler := make(map[string]func() http.HandlerFunc)
	routeToHandler["/hello"] = server.handleHello

	for path, handler := range routeToHandler {
		server.router.HandleFunc(path, handler())
	}
}

func (server *Server) handleHello() http.HandlerFunc {
	// Here we can add something, that is the reason behind making functor
	return func(writer http.ResponseWriter, request *http.Request) {
		io.WriteString(writer, "Hello")
	}
}
