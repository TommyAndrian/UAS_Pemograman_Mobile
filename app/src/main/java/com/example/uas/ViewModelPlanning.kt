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
import com.example.uas.data.entity.ModelPlanningEntity
import com.example.uas.ui.theme.UASTheme

class ViewModelPlanning : ComponentActivity() {
    private val unifiedViewModel: UnifiedViewModel by viewModels {
        UnifiedViewModelFactory(AppDatabase.getDatabase(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val modelId = intent.getIntExtra("MODEL_ID", -1)
        setContent {
            UASTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    modelId.takeIf { it != -1 }?.let {
                        ViewModelPlanningScreen(unifiedViewModel, it)
                    }
                }
            }
        }
    }
}

@Composable
fun ViewModelPlanningScreen(unifiedViewModel: UnifiedViewModel, modelId: Int) {
    val model by unifiedViewModel.getModelPlanningById(modelId).collectAsState(initial = null)

    model?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF8DA7A8))
                .padding(16.dp)
        ) {
            Text(
                text = "Detail Perencanaan Model",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            ModelPlanningDetail(model = it)
        }
    } ?: run {
        Text(
            text = "Model not found",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun ModelPlanningDetail(model: ModelPlanningEntity) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Nama Model: ${model.namaModel ?: ""}", fontSize = 18.sp)
        Text("Deskripsi Model: ${model.deskripsiModel ?: ""}", fontSize = 18.sp)
        Text("Tujuan Model: ${model.tujuanModel ?: ""}", fontSize = 18.sp)
        Text("Nama Algoritma: ${model.namaAlgoritma ?: ""}", fontSize = 18.sp)
        Text("Kebutuhan Data: ${model.kebutuhanData ?: ""}", fontSize = 18.sp)
        Text("Metodologi Pengujian: ${model.metodologiPengujian ?: ""}", fontSize = 18.sp)
        Text("Metode Pengukuran: ${model.metodePengukuran ?: ""}", fontSize = 18.sp)
    }
    Button(
        onClick = {
            context.startActivity(Intent(context, ModelPlanning::class.java))
        },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text("Kembali")
    }
}
