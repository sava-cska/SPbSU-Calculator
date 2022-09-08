package com.calculator.view_model

import androidx.lifecycle.ViewModel
import com.calculator.entities.EvaluationToken
import com.calculator.network.response.EvaluationResult
import com.calculator.network.response.error.EvaluationErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse

abstract class EvaluationViewModel : ViewModel() {
    abstract fun evaluate(tokens : List<EvaluationToken>, userId : String)
}
