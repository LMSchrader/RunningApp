package com.example.runningapp.data

import android.content.Context
import androidx.room.*

@Database(entities = [RunningScheduleEntry::class, RunHistoryEntryMetaData::class, RunHistoryMeasurement::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun runningScheduleDao(): RunningScheduleDao
    abstract fun runHistoryDao(): RunHistoryDao

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

    //private class RunHistoryDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
//
    //    override fun onCreate(db: SupportSQLiteDatabase) {
    //        super.onCreate(db)
    //        INSTANCE?.let { database ->
    //            scope.launch {
    //                val runHistoryDao = database.runHistoryDao()
//
    //                // Delete all content here.
    //                runHistoryDao.deleteAll()
//
    //                // Add sample words.
    //                val dummyEntries = RunHistoryEntry.StaticFunctions.getDummyData()
    //                runHistoryDao.insert(dummyEntries[0])
    //                runHistoryDao.insert(dummyEntries[1])
    //            }
    //        }
    //    }
    //}

    //companion object {
    //    // Singleton prevents multiple instances of database opening at the
    //    // same time.
    //    @Volatile
    //    private var INSTANCE: AppDatabase? = null
//
    //    fun getDatabase(context: Context,
    //                    scope: CoroutineScope): AppDatabase {
    //        return INSTANCE ?: synchronized(this) {
    //            val instance = Room.databaseBuilder(
    //                context.applicationContext,
    //                AppDatabase::class.java,
    //                "app_database"
    //            ).addCallback(RunHistoryDatabaseCallback(scope)).build()
    //            INSTANCE = instance
    //            instance
    //        }
    //    }
    //}
}
