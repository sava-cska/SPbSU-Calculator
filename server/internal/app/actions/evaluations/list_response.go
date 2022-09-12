package evaluations

type EvaluationHistory struct {
	Evaluation []Token
	Result     string
}

type ListResponse struct {
	EvaluationHistory []EvaluationHistory
}
