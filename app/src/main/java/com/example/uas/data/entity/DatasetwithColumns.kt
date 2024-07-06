package com.example.uas.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class DatasetWithColumns(
    @Embedded val dataset: Dataset,
    @Relation(
        parentColumn = "id",
        entityColumn = "dataset_id"
    )
    val columns: List<DatasetColumn>
)
