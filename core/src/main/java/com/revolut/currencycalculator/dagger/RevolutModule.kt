package com.revolut.currencycalculator.dagger

import android.content.Context
import com.revolut.currencycalculator.api.RevolutWebService
import com.revolut.persistence_sharedprefs.SharedPrefsLocalCurrencyValuationRepository
import com.revolut.ports.LocalCurrencyValuationRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RevolutModule(context: Context) {

    private val applicationContext = context.applicationContext

    @Provides
    fun provideApplicationContext(): Context {
        return applicationContext
    }

    @Provides
    @Singleton
    fun provideRevolutApi() = RevolutWebService(applicationContext).api

    @Provides
    @Singleton
    fun provideLocalCurrencyValuationRepository(
        context: Context
    ): LocalCurrencyValuationRepository =
        SharedPrefsLocalCurrencyValuationRepository(context)
}
