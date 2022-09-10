package com.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import com.calculator.evaluation.ui.EvaluationComponentFactory
import com.calculator.evaluation.ui.EvaluationComponentImpl
import com.calculator.input.api.CalculatorInputComponentFactory
import com.calculator.ui.input.CalculatorKeyboard
import com.calculator.ui.theme.CalculatorTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: EvaluatorViewModel by viewModels()
    private val evaluationsDataSource: AllEvaluationsViewModel by viewModels()

    @Inject
    lateinit var evaluationComponentFactory: EvaluationComponentFactory

    @Inject
    lateinit var inputComponentFactory: CalculatorInputComponentFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainCalculatorInputComponent = inputComponentFactory.createCalculatorInputComponent()
        val mainEvaluationComponent = evaluationComponentFactory.createEvaluationComponent(
            calculatorInputObserver = mainCalculatorInputComponent,
            evaluator = viewModel,
            lifecycleOwner = this,
            context = this,
        )
        setContent {
            CalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        mainEvaluationComponent = mainEvaluationComponent,
                        mainCalculatorInput = mainCalculatorInputComponent,
                        evaluationComponentFactory = evaluationComponentFactory,
                        context = this,
                        lifecycleOwner = this,
                        evaluator = viewModel,
                        evaluationsDataSource = evaluationsDataSource,
                    )
                }
            }
        }
    }


    companion object {
        private val TAG = "MainActivityEval"
    }
}
