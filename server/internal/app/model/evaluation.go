package model

import "strings"

type Token struct {
	Body string
	Type string
}

type Evaluation struct {
	UserUid    string
	Evaluation []Token
	Result     string
}

func (expression *Evaluation) ToString() string {
	var sb strings.Builder
	for _, token := range expression.Evaluation {
		sb.WriteString(token.Body)
	}
	return sb.String()
}
