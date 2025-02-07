package com.lmorda.androidflows.data

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val KEY_FIRST_NAME = "key_first_name"
        private const val KEY_LAST_NAME = "key_last_name"
    }

    var firstName: String
        get() = sharedPreferences.getString(KEY_FIRST_NAME, "John") ?: "John"
        set(value) = sharedPreferences.edit().putString(KEY_FIRST_NAME, value).apply()

    var lastName: String
        get() = sharedPreferences.getString(KEY_LAST_NAME, "Doe") ?: "Doe"
        set(value) = sharedPreferences.edit().putString(KEY_LAST_NAME, value).apply()
}
