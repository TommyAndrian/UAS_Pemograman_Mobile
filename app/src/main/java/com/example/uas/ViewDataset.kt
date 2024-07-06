package com.example.uas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.data.AppDatabase
import com.example.uas.data.entity.DatasetWithColumns
import com.example.uas.ui.theme.UASTheme

class ViewDataset : ComponentActivity() {
    private val db by lazy { AppDatabase.getDatabase(this) }
    private val unifiedViewModel: UnifiedViewModel by viewModels { UnifiedViewModelFactory(db) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UASTheme {
                val datasetId = intent.getIntExtra("DATASET_ID", -1)
                val datasetWithColumns by unifiedViewModel.datasetWithColumnsList.collectAsState()
                val dataset = datasetWithColumns.find { it.dataset.id == datasetId }
                dataset?.let {
                    ViewDatasetScreen(datasetWithColumns = it) {
                        finish()
                    }
                }
            }
        }
    }
}
@Composable
fun ViewDatasetScreen(datasetWithColumns: DatasetWithColumns, onBack: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8DA7A8))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "View Dataset",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = datasetWithColumns.dataset.namaDataset,
            onValueChange = {},
            label = { Text("Nama Dataset") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = datasetWithColumns.dataset.deskripsiDataset,
            onValueChange = {},
            label = { Text("Deskripsi Dataset") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp)
        ) {
            items(datasetWithColumns.columns) { column ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        value = column.namaKolom,
                        onValueChange = {},
                        label = { Text("Nama Kolom") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = column.tipeData,
                        onValueChange = {},
                        label = { Text("Tipe Data") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text(text = "Kembali", color = Color.White)
        }
    }
}
