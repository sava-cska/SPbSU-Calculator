package com.calculator

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calculator.evaluation.EvaluationsDataSource
import com.calculator.evaluation.Evaluator
import com.calculator.evaluation.ui.EvaluationComponent
import com.calculator.evaluation.ui.EvaluationComponentFactory
import com.calculator.input.api.CalculatorInputComponent
import com.calculator.input.api.CalculatorInputComponentFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.ui.core.gesture.PressReleasedGestureDetector
import androidx.ui.foundation.Clickable
import com.calculator.entities.EvaluationToken
import com.calculator.input.api.CalculatorInputListener
import com.calculator.input.api.CalculatorInputObserver
import kotlinx.coroutines.Deferred

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainEvaluationComponent: EvaluationComponent,
    mainCalculatorInput: CalculatorInputComponent,
    evaluationComponentFactory: EvaluationComponentFactory,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    evaluator: Evaluator,
    evaluationsDataSource: EvaluationsDataSource,
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "evaluation") {
        composable("evaluation") {
            ConstraintLayout {
                val (eval, input, button) = createRefs()
                Box(
                    Modifier
                        .constrainAs(eval) {
                            top.linkTo(parent.top)
                            bottom.linkTo(input.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    mainEvaluationComponent.EvaluationContent()
                }
                Box(
                    Modifier
                        .constrainAs(input) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    mainCalculatorInput.CalculatorInput()
                }
                Button(
                    modifier = Modifier
                        .constrainAs(button) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                        .padding(top = 16.dp, start = 16.dp),
                    onClick = {
                        evaluationsDataSource.reload()
                        navController.navigate("all_evaluations") {
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text(text = context.getString(R.string.all_evaluations_button_name))
                }
            }
        }

        composable("all_evaluations") {
            val evaluations by evaluationsDataSource.evaluations.collectAsState(Dispatchers.Main)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                content = {
                    itemsIndexed(evaluations) { index, item ->
                        val component = remember {
                            val component = evaluationComponentFactory.createEvaluationComponent(
                                calculatorInputObserver = object : CalculatorInputObserver {
                                    override fun addListener(listener: CalculatorInputListener) = Unit
                                    override fun removeListener(listener: CalculatorInputListener) = Unit
                                },
                                evaluator = evaluator,
                                lifecycleOwner = lifecycleOwner,
                                context = context,
                                evaluationsDataSource = evaluationsDataSource,
                            )
                            component.setTokens(tokens = item.first, result = item.second, editable = false)
                            component
                        }

                        
                        
                        Box(
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                                .clickable {
                                    mainEvaluationComponent.setTokens(item.first, item.second, editable = true)
                                    navController.navigate("evaluation") {
                                        launchSingleTop = true
                                    }
                                },  
                        ) {
                            component.EvaluationContent()
                        }
                        if (index < evaluations.lastIndex) {
                            Divider(
                                Modifier
                                    .padding(horizontal = 16.dp)
                                    .alpha(0.4f)
                            )
                        }
                    }
                }
            )
            Button(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                onClick = {
                    navController.navigate("evaluation") {
                        launchSingleTop = true
                    }
                }
            ) {
                Text(text = context.getString(R.string.existing_evaluation_button_name))
            }
        }
    }
}