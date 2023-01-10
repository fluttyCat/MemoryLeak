package com.core.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.base.BaseRepository
import com.core.dao.UserInfoDao
import com.core.dto.login.UserInfoDto

abstract class LocalRepository : BaseRepository {
    abstract fun insertUserInfo(userInfoDto: UserInfoDto): LiveData<Boolean>
    abstract fun getUserInfo(): LiveData<UserInfoDto>
    abstract fun deleteUser(): LiveData<Boolean>
}

class LocalRepositoryImpl(
    private val userInfoDao: UserInfoDao
) : LocalRepository() {

    override fun insertUserInfo(userInfoDto: UserInfoDto): LiveData<Boolean> {
        val TAG = "${this::class.java.simpleName}_insertUserInfo"
        val data = MutableLiveData<Boolean>()
        addExecutorThreads(userInfoDao.insertUserInfo(userInfoDto), onSuccess = {
            data.postValue(true)
        }, onError = {
            data.postValue(false)
        })
        return data
    }

    override fun deleteUser(): LiveData<Boolean> {
        val TAG = "${this::class.java.simpleName}_deleteUser"
        val data = MutableLiveData<Boolean>()
        addExecutorThreads(userInfoDao.deleteUser(), onSuccess = {
            data.postValue(true)
        }, onError = {
            data.postValue(false)
        })
        return data
    }

    override fun getUserInfo(): LiveData<UserInfoDto> =
        userInfoDao.findUserInfo()


}