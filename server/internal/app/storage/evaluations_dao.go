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
	val, exists := tmp[evaluation.UserUid]
	if !exists {
		tmp[evaluation.UserUid] = make([]model.Evaluation, 0, 5)
	}
	tmp[evaluation.UserUid] = append(val, *evaluation)
	return evaluation, nil
}

func (dao *EvaluationsDAO) List(userId string) ([]model.Evaluation, error) {
	if val, ok := tmp[userId]; ok {
		return val, nil
	} else {
		return []model.Evaluation{}, nil
	}
}
