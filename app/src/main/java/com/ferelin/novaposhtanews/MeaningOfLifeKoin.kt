package com.ferelin.novaposhtanews

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val meaningOfLifeModule = module {
    factoryOf(::B)
    factoryOf(::A) bind MeaningOfLifeKoin::class
}

interface MeaningOfLifeKoin {
    fun meaningOfLife()
}

class B

class A(val b: B) : MeaningOfLifeKoin {
    override fun meaningOfLife() = Unit
}
