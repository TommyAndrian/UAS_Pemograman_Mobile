package com.example.uas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.uas.data.AppDatabase
import com.example.uas.data.entity.Dataset
import com.example.uas.data.entity.DatasetColumn
import com.example.uas.data.entity.DatasetWithColumns
import com.example.uas.data.entity.HasilRefiningEntity
import com.example.uas.data.entity.KomunikasiManajemenEntity
import com.example.uas.data.entity.ModelPlanningEntity
import com.example.uas.data.entity.ProblemFraming
import com.example.uas.data.entity.PemrosesanEntity
import com.example.uas.data.entity.RefiningPlanningEntity
import com.example.uas.data.entity.TrainingTestingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UnifiedViewModel(private val db: AppDatabase) : ViewModel() {
    private val _projectList = MutableStateFlow<List<ProblemFraming>>(emptyList())
    val projectList: StateFlow<List<ProblemFraming>> = _projectList

    private val _datasetWithColumnsList = MutableStateFlow<List<DatasetWithColumns>>(emptyList())
    val datasetWithColumnsList: StateFlow<List<DatasetWithColumns>> = _datasetWithColumnsList

    private val _pemrosesanList = MutableStateFlow<List<PemrosesanEntity>>(emptyList())
    val pemrosesanList: StateFlow<List<PemrosesanEntity>> = _pemrosesanList

    private val _modelPlanningList = MutableStateFlow<List<ModelPlanningEntity>>(emptyList())
    val modelPlanningList: StateFlow<List<ModelPlanningEntity>> = _modelPlanningList

    private val _refiningPlanningList = MutableStateFlow<List<RefiningPlanningEntity>>(emptyList())
    val refiningPlanningList: StateFlow<List<RefiningPlanningEntity>> = _refiningPlanningList

    private val _trainingTestingDataList = MutableStateFlow<List<TrainingTestingData>>(emptyList())
    val trainingTestingDataList: StateFlow<List<TrainingTestingData>> = _trainingTestingDataList

    private val _hasilRefiningList = MutableStateFlow<List<HasilRefiningEntity>>(emptyList())
    val hasilRefiningList: StateFlow<List<HasilRefiningEntity>> = _hasilRefiningList

    private val _komunikasiManajemenList = MutableStateFlow<List<KomunikasiManajemenEntity>>(emptyList())
    val komunikasiManajemenList: StateFlow<List<KomunikasiManajemenEntity>> = _komunikasiManajemenList

    init {
        viewModelScope.launch {
            _projectList.value = db.problemFramingDao().getAll()
            _datasetWithColumnsList.value = db.datasetDao().getAllDatasetsWithColumns()
            _pemrosesanList.value = db.pemrosesanDao().getAll()
            _modelPlanningList.value = db.modelPlanningDao().getAll()
            _refiningPlanningList.value = db.refiningPlanningDao().getAll()
            _trainingTestingDataList.value = db.trainingTestingDataDao().getAll()
            _hasilRefiningList.value = db.hasilRefiningDao().getAll()
            _komunikasiManajemenList.value = db.komunikasiManajemenDao().getAll()
        }
    }

    // Project functions
    fun addProblemFraming(problemFraming: ProblemFraming) {
        viewModelScope.launch {
            db.problemFramingDao().insertAll(problemFraming)
            _projectList.value = db.problemFramingDao().getAll()
        }
    }

    fun deleteProject(project: ProblemFraming) {
        viewModelScope.launch {
            db.problemFramingDao().delete(project)
            _projectList.value = db.problemFramingDao().getAll()
        }
    }

    fun updateProject(project: ProblemFraming) {
        viewModelScope.launch {
            db.problemFramingDao().update(project)
            _projectList.value = db.problemFramingDao().getAll()
        }
    }

    // Dataset functions
    fun getDatasetWithColumnsById(datasetId: Int): DatasetWithColumns? {
        return _datasetWithColumnsList.value.find { it.dataset.id == datasetId }
    }

    fun addDataset(dataset: Dataset, columns: List<DatasetColumn>) {
        viewModelScope.launch {
            val datasetId = db.datasetDao().insertDataset(dataset).toInt()
            columns.forEach { column ->
                db.datasetColumnDao().insertAll(column.copy(datasetId = datasetId))
            }
            _datasetWithColumnsList.value = db.datasetDao().getAllDatasetsWithColumns()
        }
    }

    fun updateDataset(dataset: Dataset, columns: List<DatasetColumn>) {
        viewModelScope.launch {
            db.datasetDao().updateDataset(dataset)
            columns.forEach { column ->
                db.datasetColumnDao().update(column)
            }
            _datasetWithColumnsList.value = db.datasetDao().getAllDatasetsWithColumns()
        }
    }

    fun deleteDataset(dataset: Dataset) {
        viewModelScope.launch {
            db.datasetDao().deleteDataset(dataset)
            _datasetWithColumnsList.value = db.datasetDao().getAllDatasetsWithColumns()
        }
    }

    // Pemrosesan functions
    fun addPemrosesan(pemrosesanEntity: PemrosesanEntity) {
        viewModelScope.launch {
            db.pemrosesanDao().insert(pemrosesanEntity)
            _pemrosesanList.value = db.pemrosesanDao().getAll()
        }
    }

    fun getPemrosesanById(id: Int): Flow<PemrosesanEntity?> {
        return db.pemrosesanDao().getById(id)
    }


    fun updatePemrosesan(pemrosesanEntity: PemrosesanEntity) {
        viewModelScope.launch {
            db.pemrosesanDao().update(pemrosesanEntity)
            _pemrosesanList.value = db.pemrosesanDao().getAll()
        }
    }

    fun updatePemrosesanStatus(pemrosesanEntity: PemrosesanEntity) {
        viewModelScope.launch {
            db.pemrosesanDao().update(pemrosesanEntity)
            _pemrosesanList.value = db.pemrosesanDao().getAll()
        }
    }


    fun deletePemrosesan(pemrosesanEntity: PemrosesanEntity) {
        viewModelScope.launch {
            db.pemrosesanDao().delete(pemrosesanEntity)
            _pemrosesanList.value = db.pemrosesanDao().getAll()
        }
    }

    // Model Planning functions
    fun addModelPlanning(modelPlanningEntity: ModelPlanningEntity) {
        viewModelScope.launch {
            db.modelPlanningDao().insert(modelPlanningEntity)
            _modelPlanningList.value = db.modelPlanningDao().getAll()
        }
    }


    fun getModelPlanningById(id: Int): Flow<ModelPlanningEntity?> {
        return db.modelPlanningDao().getById(id)
    }

    fun updateModelPlanning(modelPlanningEntity: ModelPlanningEntity) {
        viewModelScope.launch {
            db.modelPlanningDao().update(modelPlanningEntity)
            _modelPlanningList.value = db.modelPlanningDao().getAll()
        }
    }

    fun deleteModelPlanning(modelPlanningEntity: ModelPlanningEntity) {
        viewModelScope.launch {
            db.modelPlanningDao().delete(modelPlanningEntity)
            _modelPlanningList.value = db.modelPlanningDao().getAll()
        }
    }

    // Refining Planning functions
    fun addRefiningPlanning(refiningPlanning: RefiningPlanningEntity) {
        viewModelScope.launch {
            db.refiningPlanningDao().insert(refiningPlanning)
            _refiningPlanningList.value = db.refiningPlanningDao().getAll()
        }
    }


    fun getRefiningPlanningById(id: Int) : Flow<RefiningPlanningEntity?>{
        return db.refiningPlanningDao().getById(id)
    }

    fun updateRefiningPlanning(refiningPlanning: RefiningPlanningEntity) {
        viewModelScope.launch {
            db.refiningPlanningDao().update(refiningPlanning)
            _refiningPlanningList.value = db.refiningPlanningDao().getAll()
        }
    }

    fun deleteRefiningPlanning(refiningPlanning: RefiningPlanningEntity) {
        viewModelScope.launch {
            db.refiningPlanningDao().delete(refiningPlanning)
            _refiningPlanningList.value = db.refiningPlanningDao().getAll()
        }
    }

    // Training Testing Data functions
    fun addTrainingTestingData(trainingTestingData: TrainingTestingData) {
        viewModelScope.launch {
            db.trainingTestingDataDao().insert(trainingTestingData)
            _trainingTestingDataList.value = db.trainingTestingDataDao().getAll()
        }
    }


    fun getTrainingTestingById(id: Int) : Flow<TrainingTestingData>{
        return db.trainingTestingDataDao().getById(id)
    }

    fun updateTrainingTestingData(trainingTestingData: TrainingTestingData) {
        viewModelScope.launch {
            db.trainingTestingDataDao().update(trainingTestingData)
            _trainingTestingDataList.value = db.trainingTestingDataDao().getAll()
        }
    }

    fun deleteTrainingTestingData(trainingTestingData: TrainingTestingData) {
        viewModelScope.launch {
            db.trainingTestingDataDao().delete(trainingTestingData)
            _trainingTestingDataList.value = db.trainingTestingDataDao().getAll()
        }
    }

    // Hasil Refining functions
    fun addHasilRefining(hasilRefining: HasilRefiningEntity) {
        viewModelScope.launch {
            db.hasilRefiningDao().insert(hasilRefining)
            _hasilRefiningList.value = db.hasilRefiningDao().getAll()  // Tambahkan ini
        }
    }

    fun getHasilRefiningById(id: Int) : Flow<HasilRefiningEntity>{
        return db.hasilRefiningDao().getById(id)
    }

    fun updateHasilRefining(hasilRefining: HasilRefiningEntity) {
        viewModelScope.launch {
            db.hasilRefiningDao().update(hasilRefining)
            _hasilRefiningList.value = db.hasilRefiningDao().getAll()  // Tambahkan ini
        }
    }

    fun deleteHasilRefining(hasilRefining: HasilRefiningEntity) {
        viewModelScope.launch {
            db.hasilRefiningDao().delete(hasilRefining)
            _hasilRefiningList.value = db.hasilRefiningDao().getAll()  // Tambahkan ini
        }
    }

    // Komunikasi Manajemen functions
    fun addKomunikasiManajemen(komunikasiManajemenEntity: KomunikasiManajemenEntity) {
        viewModelScope.launch {
            db.komunikasiManajemenDao().insert(komunikasiManajemenEntity)
            _komunikasiManajemenList.value = db.komunikasiManajemenDao().getAll() // Tambahkan ini
        }
    }

    fun getKomunikasiManajemenById(id: Int) : Flow<KomunikasiManajemenEntity?>{
        return db.komunikasiManajemenDao().getById(id)
    }

    fun updateKomunikasiManajemen(komunikasiManajemenEntity: KomunikasiManajemenEntity) {
        viewModelScope.launch {
            db.komunikasiManajemenDao().update(komunikasiManajemenEntity)
            _komunikasiManajemenList.value = db.komunikasiManajemenDao().getAll() // Tambahkan ini
        }
    }

    fun deleteKomunikasiManajemen(komunikasiManajemenEntity: KomunikasiManajemenEntity) {
        viewModelScope.launch {
            db.komunikasiManajemenDao().delete(komunikasiManajemenEntity)
            _komunikasiManajemenList.value = db.komunikasiManajemenDao().getAll() // Tambahkan ini
        }
    }
}


class UnifiedViewModelFactory(private val db: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UnifiedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UnifiedViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
