package com.example.runningapp.database

import android.content.Context
import androidx.room.*
import com.example.runningapp.model.RunningScheduleEntry

@Database(entities = [RunningScheduleEntry::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    //abstract fun runHistoryDao(): RunHistoryDao
    abstract fun runningScheduleDao(): RunningScheduleDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
