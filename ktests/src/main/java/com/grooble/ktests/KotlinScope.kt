package com.grooble.ktests

import sun.text.normalizer.UTF16.append
import java.lang.StringBuilder

internal class ScopeFunctions {

    var `fun`: inline? = null
    fun main(): `fun` {
        val s = createString
        run {
            append(4)
            append("hello")
        }
        println(s)
    }

    init {
        val sb = StringBuilder()
        sb.block()
        return sb.toString()
    }

}