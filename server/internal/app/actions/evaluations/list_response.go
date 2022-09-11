package evaluations

type EvaluationHistory struct {
	Evaluation []Token
}

type ListResponse struct {
	EvaluationHistory EvaluationHistory
}
