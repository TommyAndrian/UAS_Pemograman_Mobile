package com.example.uas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.example.uas.data.AppDatabase
import com.example.uas.data.entity.TrainingTestingData
import com.example.uas.ui.theme.UASTheme
import kotlinx.coroutines.launch

class TrainingTesting : ComponentActivity() {
    private val appDatabase by lazy {
        AppDatabase.getDatabase(applicationContext)
    }

    private val unifiedViewModel: UnifiedViewModel by viewModels {
        UnifiedViewModelFactory(appDatabase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UASTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrainingTestingListScreen(unifiedViewModel)
                }
            }
        }
    }
}

@Composable
fun TrainingTestingListScreen(viewModel: UnifiedViewModel) {
    val context = LocalContext.current
    val trainingTestingDataList by viewModel.trainingTestingDataList.collectAsState()

    var trainingTestingDataToDelete by remember { mutableStateOf<TrainingTestingData?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8DA7A8))
            .padding(16.dp)
    ) {
        Text(
            text = "Daftar Training dan Testing",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    context.startActivity(Intent(context, CreateTrainingTesting::class.java))
                },
            ) {
                Text("Create Training Testing")
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
                    Text("Project Name", fontWeight = FontWeight.Bold)
                    Text("Actions", fontWeight = FontWeight.Bold)
                }
            }

            if (trainingTestingDataList.isEmpty()) {
                item {
                    Text(
                        text = "No Data found",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            } else {
                items(trainingTestingDataList) { data ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = data.namaProyek,
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
                                    val intent = Intent(context, ViewTrainingTesting::class.java)
                                    intent.putExtra("trainingTestingId", data.id)
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text("View", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    val intent = Intent(context, EditTrainingTesting::class.java)
                                    intent.putExtra("trainingTestingId", data.id)
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text("Edit", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    trainingTestingDataToDelete = data
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

    if (showDialog && trainingTestingDataToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this data?") },
            confirmButton = {
                Button(
                    onClick = {
                        trainingTestingDataToDelete?.let { data ->
                            viewModel.deleteTrainingTestingData(data)
                            Toast.makeText(context, "Data Deleted Successfully", Toast.LENGTH_SHORT).show()
                            trainingTestingDataToDelete = null
                            showDialog = false
                        }
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
