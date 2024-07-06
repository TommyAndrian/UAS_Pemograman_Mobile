package com.example.uas.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.uas.data.entity.Dataset
import com.example.uas.data.entity.DatasetColumn
import com.example.uas.data.entity.DatasetWithColumns
import com.example.uas.data.entity.HasilRefiningEntity
import com.example.uas.data.entity.KomunikasiManajemenEntity
import com.example.uas.data.entity.ModelPlanningEntity
import com.example.uas.data.entity.PemrosesanEntity
import com.example.uas.data.entity.ProblemFraming
import com.example.uas.data.entity.RefiningPlanningEntity
import com.example.uas.data.entity.TrainingTestingData
import kotlinx.coroutines.flow.Flow

@Dao
interface ProblemFramingDao {
    @Query("SELECT * FROM ProblemFraming")
    suspend fun getAll(): List<ProblemFraming>

    @Query("SELECT * FROM ProblemFraming WHERE id = :projectId")
    suspend fun getById(projectId: Int): ProblemFraming?

    @Insert
    suspend fun insertAll(vararg problemFramings: ProblemFraming)

    @Update
    suspend fun update(problemFraming: ProblemFraming)

    @Delete
    suspend fun delete(problemFraming: ProblemFraming)
}

@Dao
interface DatasetDao {
    @Query("SELECT * FROM Dataset")
    suspend fun getAllDatasetsWithColumns(): List<DatasetWithColumns>

    @Insert
    suspend fun insertDataset(dataset: Dataset): Long

    @Update
    suspend fun updateDataset(dataset: Dataset)

    @Delete
    suspend fun deleteDataset(dataset: Dataset)
}


@Dao
interface DatasetColumnDao {
    @Insert
    suspend fun insertAll(vararg datasetColumns: DatasetColumn)

    @Update
    suspend fun update(datasetColumn: DatasetColumn)
}


@Dao
interface PemrosesanDao {
    @Query("SELECT * FROM Pemrosesan")
    suspend fun getAll(): List<PemrosesanEntity>

    @Query("SELECT * FROM Pemrosesan WHERE id = :id")
    fun getById(id: Int): Flow<PemrosesanEntity?>

    @Insert
    suspend fun insert(pemrosesanEntity: PemrosesanEntity): Long

    @Query("UPDATE pemrosesan SET status = :newStatus WHERE id = :id")
    suspend fun updateStatus(id: Int, newStatus: String)

    @Update
    suspend fun update(pemrosesanEntity: PemrosesanEntity)

    @Delete
    suspend fun delete(pemrosesanEntity: PemrosesanEntity)
}

@Dao
interface ModelPlanningDao {
    @Query("SELECT * FROM ModelPlanning")
    suspend fun getAll(): List<ModelPlanningEntity>

    @Query("SELECT * FROM ModelPlanning WHERE id = :id")
    fun getById(id: Int): Flow<ModelPlanningEntity?>

    @Insert
    suspend fun insert(modelPlanningEntity: ModelPlanningEntity): Long

    @Update
    suspend fun update(modelPlanningEntity: ModelPlanningEntity)

    @Delete
    suspend fun delete(modelPlanningEntity: ModelPlanningEntity)
}

@Dao
interface RefiningPlanningDao {
    @Query("SELECT * FROM RefiningPlanning")
    suspend fun getAll(): List<RefiningPlanningEntity>


    @Query("SELECT * FROM RefiningPlanning WHERE id = :id")
    fun getById(id: Int): Flow<RefiningPlanningEntity?>

    @Insert
    suspend fun insert(refiningPlanningEntity: RefiningPlanningEntity): Long

    @Update
    suspend fun update(refiningPlanningEntity: RefiningPlanningEntity)

    @Delete
    suspend fun delete(refiningPlanningEntity: RefiningPlanningEntity)
}

@Dao
interface TrainingTestingDataDao {
    @Query("SELECT * FROM training_testing_data")
    suspend fun getAll(): List<TrainingTestingData>


    @Query("SELECT * FROM training_testing_data WHERE id = :id")
    fun getById(id: Int): Flow<TrainingTestingData>

    @Insert
    suspend fun insert(trainingTestingData: TrainingTestingData): Long

    @Update
    suspend fun update(trainingTestingData: TrainingTestingData)

    @Delete
    suspend fun delete(trainingTestingData: TrainingTestingData)
}

@Dao
interface HasilRefiningDao {
    @Query("SELECT * FROM HasilRefining")
    suspend fun getAll(): List<HasilRefiningEntity>

    @Query("SELECT * FROM HasilRefining WHERE id = :id")
    fun getById(id: Int): Flow<HasilRefiningEntity>

    @Insert
    suspend fun insert(hasilRefiningEntity: HasilRefiningEntity): Long

    @Update
    suspend fun update(hasilRefiningEntity: HasilRefiningEntity)

    @Delete
    suspend fun delete(hasilRefiningEntity: HasilRefiningEntity)
}

@Dao
interface KomunikasiManajemenDao {
    @Query("SELECT * FROM komunikasi_manajemen")
    suspend fun getAll(): List<KomunikasiManajemenEntity>

    @Query("SELECT * FROM komunikasi_manajemen WHERE id = :id")
    fun getById(id: Int): Flow<KomunikasiManajemenEntity?>

    @Insert
    suspend fun insert(komunikasiManajemenEntity: KomunikasiManajemenEntity): Long

    @Update
    suspend fun update(komunikasiManajemenEntity: KomunikasiManajemenEntity)

    @Delete
    suspend fun delete(komunikasiManajemenEntity: KomunikasiManajemenEntity)
}