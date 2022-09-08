package com.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calculator.entities.EvaluationToken
import com.calculator.entities.ListItem
import com.calculator.network.Api
import com.calculator.network.interactor.ApiInteractor
import com.calculator.network.response.EvaluationResult
import com.calculator.network.response.error.EvaluationErrorResponse
import com.calculator.view_model.EvaluationViewModel
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiInteractor: ApiInteractor
) : EvaluationViewModel() {
    private val _evaluateStateFlow = MutableStateFlow<EvaluationState>(EvaluationState.Pending)
    fun evaluateStateFlow(): Flow<EvaluationState> {
        return _evaluateStateFlow.asStateFlow()
    }

    override fun evaluate(tokens: List<EvaluationToken>, userId: String) {
        viewModelScope.launch {
            _evaluateStateFlow.emit(EvaluationState.Loading)
            try {
                when (val response = apiInteractor.evaluate(tokens, userId)) {
                    is NetworkResponse.Success<EvaluationResult, EvaluationErrorResponse> -> {
                        _evaluateStateFlow.emit(EvaluationState.Success(response))
                    }
                    is NetworkResponse.ServerError<EvaluationResult, EvaluationErrorResponse> -> {
                        _evaluateStateFlow.emit(EvaluationState.ServerError(response))
                    }
                    is NetworkResponse.NetworkError -> {
                        _evaluateStateFlow.emit(EvaluationState.NetworkError(response))
                    }
                    is NetworkResponse.UnknownError -> {
                        _evaluateStateFlow.emit(EvaluationState.UnknownError(response))
                    }
                }
            } catch (error: Throwable) {
                _evaluateStateFlow.emit(EvaluationState.Exception(error))
            }
        }
    }

    sealed interface EvaluationState {
        object Pending : EvaluationState
        object Loading : EvaluationState
        data class Success(val res: NetworkResponse.Success<EvaluationResult, EvaluationErrorResponse>) :
            EvaluationState

        data class ServerError(val e: NetworkResponse.ServerError<EvaluationResult, EvaluationErrorResponse>) :
            EvaluationState

        data class NetworkError(val e: NetworkResponse.NetworkError<*, *>) : EvaluationState
        data class UnknownError(val e: NetworkResponse.UnknownError<*, *>) : EvaluationState
        data class Exception(val e: Throwable) : EvaluationState
    }
}
