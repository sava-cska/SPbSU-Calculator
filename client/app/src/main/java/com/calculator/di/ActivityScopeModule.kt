package com.calculator.di

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.calculator.entities.EvaluationToken
import com.calculator.evaluation.Evaluator
import com.calculator.evaluation.ui.EvaluationComponent
import com.calculator.evaluation.ui.EvaluationComponentFactory
import com.calculator.evaluation.ui.EvaluationComponentImpl
import com.calculator.input.api.CalculatorInputComponent
import com.calculator.input.api.CalculatorInputComponentFactory
import com.calculator.input.api.CalculatorInputObserver
import com.calculator.ui.input.CalculatorKeyboard
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(ActivityComponent::class)
class ActivityScopeModule {

    @ActivityScoped
    @Provides
    fun provideEvaluationComponentFactory(): EvaluationComponentFactory {
        return object : EvaluationComponentFactory {
            override fun createEvaluationComponent(
                calculatorInputObserver: CalculatorInputObserver,
                evaluator: Evaluator,
                lifecycleOwner: LifecycleOwner,
                context: Context
            ): EvaluationComponent {
                return EvaluationComponentImpl(
                    calculatorInputObserver, evaluator, lifecycleOwner, context
                )
            }

        }
    }

    @ActivityScoped
    @Provides
    fun provideCalculatorInputComponentFactory(): CalculatorInputComponentFactory {
        return object : CalculatorInputComponentFactory {
            override fun createCalculatorInputComponent(): CalculatorInputComponent {
                return CalculatorKeyboard()
            }

        }
    }
}
