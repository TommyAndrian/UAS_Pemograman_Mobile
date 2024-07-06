package com.example.uas.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProblemFraming(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nama_proyek") val namaProyek: String,
    @ColumnInfo(name = "deskripsi") val deskripsi: String,
    @ColumnInfo(name = "algoritma") val algoritma: String,
    @ColumnInfo(name = "output") val output: String
)

@Entity(tableName = "Dataset")
data class Dataset(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nama_dataset") val namaDataset: String,
    @ColumnInfo(name = "deskripsi_dataset") val deskripsiDataset: String
)

@Entity(tableName = "DatasetColumn")
data class DatasetColumn(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "dataset_id") val datasetId: Int,
    @ColumnInfo(name = "nama_kolom") val namaKolom: String,
    @ColumnInfo(name = "tipe_data") val tipeData: String
)

@Entity(tableName = "Pemrosesan")
data class PemrosesanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "jenis_aktivitas") val jenisAktivitas: String,
    @ColumnInfo(name = "deskripsi_aktivitas") val deskripsiAktivitas: String,
    @ColumnInfo(name = "pemrosesan") val pemrosesan: String,
    @ColumnInfo(name = "hasil_uri") val hasilUri: String,
    @ColumnInfo(name = "status") val status: String
)

@Entity(tableName = "ModelPlanning")
data class ModelPlanningEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nama_model") val namaModel: String,
    @ColumnInfo(name = "deskripsi_model") val deskripsiModel: String,
    @ColumnInfo(name = "tujuan_model") val tujuanModel: String,
    @ColumnInfo(name = "nama_algoritma") val namaAlgoritma: String,
    @ColumnInfo(name = "kebutuhan_data") val kebutuhanData: String,
    @ColumnInfo(name = "metodologi_pengujian") val metodologiPengujian: String,
    @ColumnInfo(name = "metode_pengukuran") val metodePengukuran: String,
)

@Entity(tableName = "RefiningPlanning")
data class RefiningPlanningEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nama_model") val namaModel: String,
    @ColumnInfo(name = "identifikasi_masalah") val identifikasiMasalah: String,
    @ColumnInfo(name = "evaluasi_model_awal") val evaluasiModelAwal: String,
    @ColumnInfo(name = "tujuan_refining") val tujuanRefining: String,
    @ColumnInfo(name = "strategi") val strategi: String,
    @ColumnInfo(name = "nama_algoritma") val namaAlgoritma: String,
    @ColumnInfo(name = "kebutuhan_data") val kebutuhanData: String,
    @ColumnInfo(name = "metodologi_pengujian") val metodologiPengujian: String,
    @ColumnInfo(name = "metodologi_pengukuran") val metodePengukuran: String
)

@Entity(tableName = "training_testing_data")
data class TrainingTestingData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nama_proyek") val namaProyek: String,
    @ColumnInfo(name = "nama_dataset") val namaDataset: String,
    @ColumnInfo(name = "aktivitas") val aktivitas: String,
    @ColumnInfo(name = "training_result_file_uri") val trainingResultFileUri: String,
    @ColumnInfo(name = "testing_result_file_uri") val testingResultFileUri: String
)

@Entity(tableName = "HasilRefining")
data class HasilRefiningEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nama_proyek") val namaProyek: String,
    @ColumnInfo(name = "nama_dataset") val namaDataset: String,
    @ColumnInfo(name = "aktivitas") val aktivitas: String,
    @ColumnInfo(name = "hasil_refining") val hasilRefining: String,
    @ColumnInfo(name = "nama_file") val namaFile: String
)

@Entity(tableName = "komunikasi_manajemen")
data class KomunikasiManajemenEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nama_proyek") val namaProyek: String,
    @ColumnInfo(name = "nama_manajemen") val namaManajemen: String
)