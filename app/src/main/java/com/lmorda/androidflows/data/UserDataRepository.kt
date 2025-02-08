package com.lmorda.androidflows.data

import com.lmorda.androidflows.models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor(
    private val dataSource: FakeUserDataSource
) {

    // Use a coroutine for this one-off operation
    suspend fun getUser(): User {
        return dataSource.getUser()
    }

    // Use a coroutine for this one-off operation
    suspend fun updateUser(firstName: String, lastName: String) {
        dataSource.updateUser(firstName, lastName)
    }
}
