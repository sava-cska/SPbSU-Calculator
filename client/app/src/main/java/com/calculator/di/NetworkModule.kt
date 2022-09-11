package com.calculator.di

import com.calculator.BuildConfig
import com.calculator.network.Api
import com.calculator.network.MockApi
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideFlipperNetworkPlugin(): NetworkFlipperPlugin {
        assert(BuildConfig.DEBUG)
        return NetworkFlipperPlugin()
    }
    
    @Provides
    @Singleton
    fun provideOkhttpClient(
        flipperNetworkPlugin: Lazy<NetworkFlipperPlugin>,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .apply {
                readTimeout(60, TimeUnit.SECONDS)
                connectTimeout(60, TimeUnit.SECONDS)
                if (BuildConfig.DEBUG) {
                    addNetworkInterceptor(FlipperOkhttpInterceptor(flipperNetworkPlugin.get()))
                }
            }
            .build()

    @Provides
    @Singleton
    fun provideApi(okHttpClient: OkHttpClient, moshi: Moshi): Api { 
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BACKEND_API_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .build()
            .create(Api::class.java)
    }
}
