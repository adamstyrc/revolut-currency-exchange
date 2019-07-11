package com.adamstyrc.currencyrateconverter.dagger

import android.content.Context
import com.adamstyrc.currencyrateconverter.api.RevolutWebService
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

}
