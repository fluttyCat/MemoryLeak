package com.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.core.dao.UserInfoDao
import com.core.dto.login.UserInfoDto

/**
 * Main database description.
 */
@Database(
    entities =
    [
        UserInfoDto::class
    ],
    version = 1, exportSchema = false
)
abstract class MyTestDb : RoomDatabase() {

    abstract fun userInfoDao(): UserInfoDao

}
