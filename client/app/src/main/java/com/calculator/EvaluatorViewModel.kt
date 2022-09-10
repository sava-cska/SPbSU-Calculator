package com.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calculator.entities.EvaluationToken
import com.calculator.evaluation.Evaluator
import com.calculator.network.interactor.ApiInteractor
import com.calculator.network.response.EvaluationResult
import com.calculator.network.response.error.EvaluationErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class EvaluatorViewModel @Inject constructor(
    private val apiInteractor: ApiInteractor
) : ViewModel(), Evaluator {

    private val userId = "Billy"

    override fun evaluate(tokens: List<EvaluationToken>): Deferred<Evaluator.Result> =
        viewModelScope.async(Dispatchers.Default) {
            try {
                return@async when (val response = apiInteractor.evaluate(tokens, userId)) {
                    is NetworkResponse.Success<EvaluationResult, EvaluationErrorResponse> -> {
                        Success(response)
                    }
                    is NetworkResponse.ServerError<EvaluationResult, EvaluationErrorResponse> -> {
                        ServerError(response)
                    }
                    is NetworkResponse.NetworkError -> {
                        NetworkError(response)
                    }
                    is NetworkResponse.UnknownError -> {
                        UnknownError(response)
                    }
                }
            } catch (error: Throwable) {
                return@async Exception(error)
            }
        }


    companion object {
        class Success(res: NetworkResponse.Success<EvaluationResult, EvaluationErrorResponse>) :
            Evaluator.Result.Success {
            override val res = res.body.result
        }

        class ServerError(val e: NetworkResponse.ServerError<EvaluationResult, EvaluationErrorResponse>) :
            Evaluator.Result.Error {
            override val error: List<String> = e.body?.errors?.map { it.message } ?: listOf()
        }

        data class NetworkError(val e: NetworkResponse.NetworkError<*, *>) :
            Evaluator.Result.Error {
            override val error: List<String> = listOf(e.error.message).filterNotNull()
        }

        data class UnknownError(val e: NetworkResponse.UnknownError<*, *>) :
            Evaluator.Result.Error {
            override val error: List<String> = listOf(e.error.message).filterNotNull()
        }

        data class Exception(val e: Throwable) : Evaluator.Result.Error {
            override val error: List<String> = listOf(e.message).filterNotNull()
        }
    }
}
