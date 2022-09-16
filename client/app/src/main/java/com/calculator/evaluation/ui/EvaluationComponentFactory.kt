package com.calculator.evaluation.ui

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.calculator.entities.EvaluationToken
import com.calculator.evaluation.EvaluationsDataSource
import com.calculator.evaluation.Evaluator
import com.calculator.input.api.CalculatorInputObserver


interface EvaluationComponentFactory {
    fun createEvaluationComponent(
        calculatorInputObserver: CalculatorInputObserver,
        evaluator: Evaluator,
        lifecycleOwner: LifecycleOwner,
        context: Context,
        evaluationsDataSource: EvaluationsDataSource,

    ): EvaluationComponent
}