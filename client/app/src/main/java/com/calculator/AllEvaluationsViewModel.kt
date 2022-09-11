package com.calculator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calculator.entities.EvaluationToken
import com.calculator.entities.UserIdHolder
import com.calculator.evaluation.EvaluationsDataSource
import com.calculator.network.interactor.ApiInteractor
import com.calculator.network.request.Token
import com.calculator.network.response.EvaluationResult
import com.calculator.network.response.GetEvaluationsResult
import com.calculator.network.response.error.EvaluationErrorResponse
import com.calculator.network.response.error.GetEvaluationsErrorResponse
import com.calculator.util.toJsonToken
import com.calculator.util.toModelToken
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllEvaluationsViewModel @Inject constructor(
    private val apiInteractor: ApiInteractor,
    private val userIdHolder: UserIdHolder,
) : ViewModel(), EvaluationsDataSource {
    override val evaluations: StateFlow<List<Pair<List<EvaluationToken>, String>>>
        get() {
            return innerFlow
        }

    private val innerFlow: MutableStateFlow<List<Pair<List<EvaluationToken>, String>>> = MutableStateFlow(listOf())

    override fun reload() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val evaluations = apiInteractor.getEvaluations(userId = userIdHolder.userId)) {
                    is NetworkResponse.Success<GetEvaluationsResult, GetEvaluationsErrorResponse> -> {
                        innerFlow.emit(
                            evaluations.body.evaluations.map { evaluation ->
                                evaluation.request.evaluation.map { it.toModelToken() } to evaluation.result
                            }
                        )
                    }
                    else -> {
                        throw RuntimeException()
                    }
                }
            } catch (e: Exception) {
                Log.e("EvaluationsGetAllError", e.stackTraceToString())
            }
        }
    }
}