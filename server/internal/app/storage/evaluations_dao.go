package storage

import (
	"github.com/sava-cska/SPbSU-Calculator/internal/app/model"
)

type EvaluationsDAO struct {
	storage *Storage
}

var tmp = make(map[string][]model.Evaluation)

// Upsert
// Add new instance of evaluation, do not throw error if table contains
func (dao *EvaluationsDAO) Upsert(evaluation *model.Evaluation) (*model.Evaluation, error) {
	val, exists := tmp[evaluation.UserId]
	if !exists {
		tmp[evaluation.UserId] = make([]model.Evaluation, 0, 5)
	}
	tmp[evaluation.UserId] = append(val, *evaluation)
	return evaluation, nil
}

func (dao *EvaluationsDAO) List(userId string) ([]string, error) {
	mapped := make([]string, len(tmp[userId]))
	for _, v := range tmp[userId] {
		mapped = append(mapped, v.EncodedEvaluation)
	}
	return mapped, nil
}
