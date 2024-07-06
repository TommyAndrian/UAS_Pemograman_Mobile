package com.example.uas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.uas.data.AppDatabase
import com.example.uas.ui.theme.UASTheme
import kotlinx.coroutines.launch

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.uas.data.entity.TrainingTestingData
import com.example.uas.ui.theme.UASTheme

class CreateTrainingTesting : ComponentActivity() {
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
                    InputTrainingTestingScreen(unifiedViewModel)
                }
            }
        }
    }
}

@Composable
fun InputTrainingTestingScreen(viewModel: UnifiedViewModel) {
    val context = LocalContext.current
    var projectName by remember { mutableStateOf(TextFieldValue("")) }
    var datasetName by remember { mutableStateOf(TextFieldValue("")) }
    var activity by remember { mutableStateOf(TextFieldValue("")) }
    var trainingResultFileUri by remember { mutableStateOf<Uri?>(null) }
    var testingResultFileUri by remember { mutableStateOf<Uri?>(null) }
    var trainingResultFileName by remember { mutableStateOf("No file selected") }
    var testingResultFileName by remember { mutableStateOf("No file selected") }
    var isLoading by remember { mutableStateOf(false) }

    val trainingResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            trainingResultFileUri = it
            trainingResultFileName = it.lastPathSegment ?: "Unnamed File"
        }
    }

    val testingResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            testingResultFileUri = it
            testingResultFileName = it.lastPathSegment ?: "Unnamed File"
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF8DA7A8))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Create New Training and Testing Data",
                fontSize = 28.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                label = { Text("Nama Proyek") },
                value = projectName,
                onValueChange = { projectName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                label = { Text("Nama Dataset") },
                value = datasetName,
                onValueChange = { datasetName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                label = { Text("Aktivitas") },
                value = activity,
                onValueChange = { activity = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { trainingResultLauncher.launch("*/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pilih Hasil Pelatihan File", color = Color.Black)
            }
            Text(text = "File Training: $trainingResultFileName", color = Color.Black)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { testingResultLauncher.launch("*/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pilih Hasil Pengujian File", color = Color.Black)
            }
            Text(text = "File Testing: $testingResultFileName", color = Color.Black)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {

                            val trainingTestingData = TrainingTestingData(
                                namaProyek = projectName.text,
                                namaDataset = datasetName.text,
                                aktivitas = activity.text,
                                trainingResultFileUri = trainingResultFileUri.toString(),
                                testingResultFileUri = testingResultFileUri.toString()
                            )
                            viewModel.addTrainingTestingData(trainingTestingData)
                            Toast.makeText(context, "Training and Testing Data Created Successfully", Toast.LENGTH_SHORT).show()
                            context.startActivity(Intent(context, TrainingTesting::class.java))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text("Save", color = Color.Black)
                }

                Button(
                    onClick = {
                        context.startActivity(Intent(context, TrainingTesting::class.java))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text("Kembali", color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000))
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Menyimpan Data...", color = Color.White)
                }
            }
        }
    }
}
