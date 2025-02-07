package com.lmorda.androidflows.data

import com.lmorda.androidflows.models.User
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeUserDataSource @Inject constructor(
    private val userPreferences: UserPreferences
) {

    suspend fun getUser(): User {
        delay(50)
        return User(
            firstName = userPreferences.firstName,
            lastName = userPreferences.lastName
        )
    }

    suspend fun updateUser(firstName: String, lastName: String) {
        delay(50)
        userPreferences.firstName = firstName
        userPreferences.lastName = lastName
    }
}
