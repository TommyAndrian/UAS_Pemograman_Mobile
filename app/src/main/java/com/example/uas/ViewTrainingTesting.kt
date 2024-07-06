package com.example.uas

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas.data.AppDatabase
import com.example.uas.data.entity.TrainingTestingData
import com.example.uas.ui.theme.UASTheme

class ViewTrainingTesting : ComponentActivity() {
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
                        ViewTrainingTestingScreen(viewModel, it)
                    }
                }
            }
        }
    }
}

@Composable
fun ViewTrainingTestingScreen(viewModel: UnifiedViewModel, trainingTestingId: Int) {
    val context = LocalContext.current
    val trainingTestingData by viewModel.getTrainingTestingById(trainingTestingId).collectAsState(initial = null)

    trainingTestingData?.let { data ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF8DA7A8))
                .padding(16.dp)
        ) {
            Text(
                text = "Training and Testing Data Detail",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TrainingTestingDetail(data)
            Button(
                onClick = {
                    context.startActivity(Intent(context, TrainingTesting::class.java))
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Kembali")
            }
        }
    } ?: run {
        Text(
            text = "Data not found",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun TrainingTestingDetail(data: TrainingTestingData) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Project Name: ${data.namaProyek}", fontSize = 18.sp)
        Text("Dataset Name: ${data.namaDataset}", fontSize = 18.sp)
        Text("Activity: ${data.aktivitas}", fontSize = 18.sp)
        Text("Training Result File URI: ${data.trainingResultFileUri}", fontSize = 18.sp)
        Text("Testing Result File URI: ${data.testingResultFileUri}", fontSize = 18.sp)
    }
}
