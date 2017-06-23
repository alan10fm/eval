package com.noj.eval.data

import com.noj.eval.data.local.LocalData
import com.noj.eval.data.remote.RemoteData
import com.noj.eval.data.sharedpreferences.SharedPreferencesData
import com.noj.eval.model.Group
import com.noj.eval.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EvalRepository @Inject internal constructor(
        private val remoteDataSource: RemoteData,
        private val localRemoteDataSource: LocalData,
        private val sharedPreferences: SharedPreferencesData
) : EvalDataSource {

    override var groupsCreated: List<Group>
        get() = remoteDataSource.groupsCreated
        set(value) {
            error("You should not try to invoke this method")
        }

    override var groupsAccepted: List<Group>
        get() = remoteDataSource.groupsAccepted
        set(value) {
            error("You should not try to invoke this method")
        }

    override fun createUser(user: User) {
        if (sharedPreferences.userId == 0L) {
            val newUser = remoteDataSource.createUser(user)
            sharedPreferences.storeUserData(newUser)
        }
    }

    override fun createGroup(group: Group): Group {
        return remoteDataSource.createGroup(
                group.copy(
                        creator = User(id = sharedPreferences.userId)
                )
        )
    }

}
