package com.example.uas

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.uas.data.entity.Dataset
import com.example.uas.data.entity.DatasetWithColumns
import com.example.uas.ui.theme.UASTheme
import kotlinx.coroutines.launch

class Dataaset : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UASTheme {
                val db = AppDatabase.getDatabase(applicationContext)
                val datasetViewModel: UnifiedViewModel = viewModel(
                    factory = UnifiedViewModelFactory(db)
                )
                DisplayDatasets(datasetViewModel)
            }
        }
    }
}

@Composable
fun DisplayDatasets(viewModel: UnifiedViewModel) {
    val context = LocalContext.current
    val datasetList by viewModel.datasetWithColumnsList.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var datasetToDelete by remember { mutableStateOf<Dataset?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8DA7A8))
            .padding(16.dp)
    ) {
        Text(
            text = "Daftar Dataset",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                context.startActivity(Intent(context, CreateDataset::class.java))
            },

        ) {
            Text("Create Dataset")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Nama Dataset", fontWeight = FontWeight.Bold)
                    Text("Aksi", fontWeight = FontWeight.Bold)
                }
            }

            if (datasetList.isEmpty()) {
                item {
                    Text(
                        text = "No Dataset found",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            } else {
                items(datasetList) { datasetWithColumns ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = datasetWithColumns.dataset.namaDataset,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .weight(1f)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Row(
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState()),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    val intent = Intent(context, ViewDataset::class.java)
                                    intent.putExtra("DATASET_ID", datasetWithColumns.dataset.id)
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text("View", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    val intent = Intent(context, EditDataset::class.java)
                                    intent.putExtra("DATASET_ID", datasetWithColumns.dataset.id)
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text("Edit", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    datasetToDelete = datasetWithColumns.dataset
                                    showDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text("Delete", color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                context.startActivity(Intent(context, MainActivity::class.java))
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Back to Home")
        }
    }

    if (showDialog && datasetToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this dataset?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteDataset(datasetToDelete!!)
                        showDialog = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
