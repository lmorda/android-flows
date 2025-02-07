package com.lmorda.androidflows.data

import com.lmorda.androidflows.models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor(
    private val dataSource: FakeUserDataSource
) {

    suspend fun getUser(): User {
        return dataSource.getUser()
    }

    suspend fun updateUser(firstName: String, lastName: String) {
        dataSource.updateUser(firstName, lastName)
    }
}
