package com.calculator.evaluation

import com.calculator.entities.EvaluationToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface EvaluationsDataSource {
    
    val evaluations: StateFlow<List<Pair<List<EvaluationToken>, String?>>>
    
    fun reload()
}