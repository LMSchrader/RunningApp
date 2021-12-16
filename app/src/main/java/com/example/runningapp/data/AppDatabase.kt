package com.example.runningapp.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [RunningScheduleEntry::class, RunHistoryEntry::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun runningScheduleDao(): RunningScheduleDao
    abstract fun runHistoryDao(): RunHistoryDao

    private class RunHistoryDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val runHistoryDao = database.runHistoryDao()

                    // Delete all content here.
                    runHistoryDao.deleteAll()

                    // Add sample words.
                    val dummyEntries = RunHistoryEntry.StaticFunctions.getDummyData()
                    runHistoryDao.insert(dummyEntries[0])
                    runHistoryDao.insert(dummyEntries[1])
                }
            }
        }
    }


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addCallback(RunHistoryDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
