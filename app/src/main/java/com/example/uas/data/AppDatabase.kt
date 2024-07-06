package com.example.uas.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.uas.data.dao.*
import com.example.uas.data.entity.*

@Database(
    entities = [
        ProblemFraming::class,
        Dataset::class,
        DatasetColumn::class,
        PemrosesanEntity::class,
        ModelPlanningEntity::class,
        RefiningPlanningEntity::class,
        TrainingTestingData::class,
        HasilRefiningEntity::class,
        KomunikasiManajemenEntity::class
    ],
    version = 2, // Update the version
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun problemFramingDao(): ProblemFramingDao
    abstract fun datasetDao(): DatasetDao
    abstract fun datasetColumnDao(): DatasetColumnDao
    abstract fun pemrosesanDao(): PemrosesanDao
    abstract fun modelPlanningDao(): ModelPlanningDao
    abstract fun refiningPlanningDao(): RefiningPlanningDao
    abstract fun trainingTestingDataDao(): TrainingTestingDataDao
    abstract fun hasilRefiningDao(): HasilRefiningDao
    abstract fun komunikasiManajemenDao(): KomunikasiManajemenDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Add this to handle database version changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
