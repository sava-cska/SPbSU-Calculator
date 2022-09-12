package evaluations

type EvaluationHistory struct {
	Evaluation []Token
	Result     string
}

type ListResponse struct {
	Response []EvaluationHistory
}
