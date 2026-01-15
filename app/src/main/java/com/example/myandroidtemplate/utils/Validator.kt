package com.example.myandroidtemplate.utils
object Validator {
    fun isEmailValid(v: String) = v.contains("@")
    fun isPasswordValid(v: String) = v.length >= 6
}
