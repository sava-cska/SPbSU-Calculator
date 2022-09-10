package com.calculator.di

import android.annotation.SuppressLint
import android.app.Application
import android.provider.Settings
import com.calculator.EvaluatorViewModel
import com.calculator.entities.UserIdHolder
import com.calculator.evaluation.Evaluator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApplicationScopeModule {
    
    @SuppressLint("HardwareIds")
    @Provides
    @Singleton
    fun provideUserIdHolder(
        application: Application
    ): UserIdHolder {
        return UserIdHolder(
            Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID)
        )
    }
    
    @Provides
    @Singleton
    fun provideEvaluator(
        viewModel: EvaluatorViewModel,
    ): Evaluator {
        return viewModel
    }
}