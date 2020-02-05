package com.revolut.currencycalculator.dagger

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RevolutModule::class, RevolutViewModelModule::class])
interface RevolutComponent : InjectionGraph
