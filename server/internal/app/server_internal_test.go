package server

import (
	"github.com/stretchr/testify/assert"
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestServer_HandleHello(t *testing.T) {
	serv := New(NewConfig())
	recorder := httptest.NewRecorder()
	request, _ := http.NewRequest(http.MethodGet, "/hello", nil)
	serv.handleHello().ServeHTTP(recorder, request)

	assert.Equal(t, recorder.Body.String(), "Hello")
}
