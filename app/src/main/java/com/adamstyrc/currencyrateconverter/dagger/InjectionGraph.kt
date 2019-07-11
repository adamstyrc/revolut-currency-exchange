package com.adamstyrc.currencyrateconverter.dagger

import android.content.Context
import com.adamstyrc.currencyrateconverter.ui.activity.MainActivity

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
