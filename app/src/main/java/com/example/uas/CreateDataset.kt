package com.example.uas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import com.example.uas.data.AppDatabase
import com.example.uas.data.entity.Dataset
import com.example.uas.data.entity.DatasetColumn
import com.example.uas.ui.theme.UASTheme


class CreateDataset : ComponentActivity() {
    private val db by lazy { AppDatabase.getDatabase(this) }
    private val unifiedViewModel: UnifiedViewModel by viewModels { UnifiedViewModelFactory(db) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UASTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DatasetScreen(unifiedViewModel)
                }
            }
        }
    }
}

@Composable
fun DatasetScreen(viewModel: UnifiedViewModel) {
    val context = LocalContext.current
    var datasetName by remember { mutableStateOf("") }
    var datasetDescription by remember { mutableStateOf("") }
    val columns = remember { mutableStateListOf<DatasetColumn>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Dataset",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = datasetName,
            onValueChange = { datasetName = it },
            label = { Text("Nama Dataset") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = datasetDescription,
            onValueChange = { datasetDescription = it },
            label = { Text("Deskripsi Dataset") },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp)
        ) {
            items(columns) { column ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        value = column.namaKolom,
                        onValueChange = { newValue ->
                            columns[columns.indexOf(column)] = column.copy(namaKolom = newValue)
                        },
                        label = { Text("Nama Kolom") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = column.tipeData,
                        onValueChange = { newValue ->
                            columns[columns.indexOf(column)] = column.copy(tipeData = newValue)
                        },
                        label = { Text("Tipe Data") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    columns.add(DatasetColumn(0, 0, "", ""))
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = "Tambah Kolom", color = Color.White)
            }

            Button(
                onClick = {
                    val newDataset = Dataset(
                        namaDataset = datasetName,
                        deskripsiDataset = datasetDescription
                    )
                    viewModel.addDataset(newDataset, columns)
                    Toast.makeText(context, "Refining Plan saved successfully", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = "Simpan", color = Color.White)
            }
        }

        Button(
            onClick = {
                context.startActivity(Intent(context, Dataaset::class.java))
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text(text = "Kembali", color = Color.White)
        }
    }
}
