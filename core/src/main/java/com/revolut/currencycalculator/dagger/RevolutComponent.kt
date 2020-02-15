package com.revolut.currencycalculator.dagger

import android.content.Context
import com.revolut.currencycalculator.ui.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    RevolutModule::class,
    RevolutViewModelModule::class
])
interface RevolutComponent {
    fun inject(mainActivity: MainActivity)

    object Manager {
        var instance: RevolutComponent? = null
            private set

        fun init(context: Context): RevolutComponent {
            if (instance == null) {
                instance = DaggerRevolutComponent.builder()
                    .revolutModule(RevolutModule(context))
                    .build()
            }

            return instance!!
        }
    }
}
