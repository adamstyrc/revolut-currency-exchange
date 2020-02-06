package com.revolut.currencycalculator.dagger

import android.content.Context
import com.revolut.currencycalculator.ui.activity.MainActivity

interface InjectionGraph {

    fun inject(mainActivity: MainActivity)

    object Manager {
        var instance: InjectionGraph? = null
            private set

        fun init(context: Context): InjectionGraph {
            if (instance == null) {
                instance = DaggerRevolutComponent.builder()
                    .revolutModule(RevolutModule(context))
                    .build()
            }

            return instance!!
        }
    }
}
