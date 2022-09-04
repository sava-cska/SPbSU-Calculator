package server

import (
	server "github.com/sava-cska/SPbSU-Calculator/internal/app"
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestServer_HandleHello(t *testing.T) {
	serv := server.New(server.NewConfig())
	recorder := httptest.NewRecorder()
	request, _ := http.NewRequest(http.MethodGet, "/hello", nil)
	serv.
}
