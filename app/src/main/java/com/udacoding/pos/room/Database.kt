package com.udacoding.pos.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacoding.pos.room.dao.DaoCart
import com.udacoding.pos.room.model.EntityCart

@Database(entities = [EntityCart::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun daoCart(): DaoCart

    companion object {
        private var mInstance: com.udacoding.pos.room.Database? = null

        fun getInstance(context: Context): com.udacoding.pos.room.Database{
            if (mInstance == null)
                mInstance = Room.databaseBuilder(context, com.udacoding.pos.room.Database::class.java, "UDAPOS_KOTLIN")
                    .fallbackToDestructiveMigration()
                    .build()
            return mInstance!!
        }
    }

}