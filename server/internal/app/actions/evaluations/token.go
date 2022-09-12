package evaluations

import "strings"

type Token struct {
	Body string
	Type string
}

func toString(tokens []Token) string {
	var sb strings.Builder
	for _, token := range tokens {
		sb.WriteString(token.Body)
	}
	return sb.String()
}

func encode(tokens []Token) string {
	//	TODO()
	return toString(tokens)
}

func decode(encodedToken string) []Token {
	//	TODO()
	return []Token{
		{
			Body: "5",
			Type: "Number",
		},
		{
			Body: "*",
			Type: "Operation",
		},
		{
			Body: "8",
			Type: "Number",
		},
	}
}
