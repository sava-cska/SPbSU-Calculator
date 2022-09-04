.PHONY: build

build:
	go build -o main -v ./cmd

.PHONY: test
test:
	go test -v -race -timeout 30s ./...

.DEFAULT_GOAL := build