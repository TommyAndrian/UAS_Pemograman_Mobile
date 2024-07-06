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
import androidx.compose.ui.unit.sp
import com.example.uas.data.AppDatabase
import com.example.uas.data.entity.TrainingTestingData
import com.example.uas.ui.theme.UASTheme
import kotlinx.coroutines.launch

class EditTrainingTesting : ComponentActivity() {
    private val viewModel: UnifiedViewModel by viewModels {
        UnifiedViewModelFactory(AppDatabase.getDatabase(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val trainingTestingId = intent.getIntExtra("trainingTestingId", -1)
        setContent {
            UASTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    trainingTestingId.takeIf { it != -1 }?.let {
                        EditTrainingTestingScreen(viewModel, it)
                    }
                }
            }
        }
    }
}

@Composable
fun EditTrainingTestingScreen(viewModel: UnifiedViewModel, trainingTestingId: Int) {
    val context = LocalContext.current
    val trainingTestingData by viewModel.getTrainingTestingById(trainingTestingId).collectAsState(initial = null)
    var projectName by remember { mutableStateOf(TextFieldValue("")) }
    var datasetName by remember { mutableStateOf(TextFieldValue("")) }
    var activity by remember { mutableStateOf(TextFieldValue("")) }
    var trainingResultFileUri by remember { mutableStateOf<Uri?>(null) }
    var testingResultFileUri by remember { mutableStateOf<Uri?>(null) }
    var trainingResultFileName by remember { mutableStateOf("No file selected") }
    var testingResultFileName by remember { mutableStateOf("No file selected") }

    LaunchedEffect(trainingTestingData) {
        trainingTestingData?.let { data ->
            projectName = TextFieldValue(data.namaProyek)
            datasetName = TextFieldValue(data.namaDataset)
            activity = TextFieldValue(data.aktivitas)
            trainingResultFileUri = Uri.parse(data.trainingResultFileUri)
            testingResultFileUri = Uri.parse(data.testingResultFileUri)
            trainingResultFileName = trainingResultFileUri?.lastPathSegment ?: "No file selected"
            testingResultFileName = testingResultFileUri?.lastPathSegment ?: "No file selected"
        }
    }

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
                text = "Edit Training and Testing Data",
                fontSize = 28.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                label = { Text("Project Name") },
                value = projectName,
                onValueChange = { projectName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                label = { Text("Dataset Name") },
                value = datasetName,
                onValueChange = { datasetName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                label = { Text("Activity") },
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
                Text("Select Training Result File", color = Color.Black)
            }
            Text(text = "Training File: $trainingResultFileName", color = Color.Black)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { testingResultLauncher.launch("*/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Testing Result File", color = Color.Black)
            }
            Text(text = "Testing File: $testingResultFileName", color = Color.Black)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        trainingTestingData?.let { data ->
                            val updatedData = data.copy(
                                namaProyek = projectName.text,
                                namaDataset = datasetName.text,
                                aktivitas = activity.text,
                                trainingResultFileUri = trainingResultFileUri.toString(),
                                testingResultFileUri = testingResultFileUri.toString()
                            )
                            viewModel.updateTrainingTestingData(updatedData)
                            Toast.makeText(context, "Training and Testing Data Updated Successfully", Toast.LENGTH_SHORT).show()
                            context.startActivity(Intent(context, TrainingTesting::class.java))
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text("Update", color = Color.Black)
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
        }
    }
}
