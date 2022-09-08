package com.calculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.calculator.evaluation.ui.EvaluationComponentImpl
import com.calculator.ui.input.CalculatorKeyboard
import com.calculator.ui.theme.CalculatorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inputComponent = CalculatorKeyboard()
        val evaluationComponent = EvaluationComponentImpl(
            tokens = listOf(),
            calculatorInputObserver = inputComponent,
            vm = viewModel
        )
        subscribeToEvaluateStatus()

        setContent {
            CalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ConstraintLayout {
                        val (eval, input) = createRefs()
                        Box(
                            Modifier
                                .constrainAs(eval) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(input.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                        ) {
                            evaluationComponent.EvaluationContent()
                        }
                        Box(
                            Modifier
                                .constrainAs(input) {
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                        ) {
                            inputComponent.CalculatorInput()
                        }
                    }
                }
            }
        }
    }

    private fun subscribeToEvaluateStatus() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.evaluateStateFlow().collect { vs ->
                    when (vs) {
                        MainViewModel.EvaluationState.Pending -> {
                            Log.d(TAG, "pending")
                        }
                        MainViewModel.EvaluationState.Loading -> {
                            Log.d(TAG, "loading")
                        }
                        is MainViewModel.EvaluationState.Success -> {
                            Log.d(TAG, vs.res.body.result)
                        }
                        is MainViewModel.EvaluationState.ServerError -> {}
                        is MainViewModel.EvaluationState.NetworkError -> {}
                        is MainViewModel.EvaluationState.UnknownError -> {}
                        is MainViewModel.EvaluationState.Exception -> {}
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = "MainActivityEval"
    }
}
