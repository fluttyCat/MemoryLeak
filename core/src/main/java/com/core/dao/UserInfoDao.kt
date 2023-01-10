package com.core.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.core.dto.login.UserInfoDto
import io.reactivex.Completable

@Dao
interface UserInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserInfo(rss: UserInfoDto) : Completable

    @Query("SELECT * FROM userinfodto")
    fun findUserInfo(): LiveData<UserInfoDto>

    @Query("DELETE FROM userinfodto")
    fun deleteUser() : Completable

}