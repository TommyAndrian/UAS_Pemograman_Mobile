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
import com.example.uas.data.entity.RefiningPlanningEntity
import com.example.uas.ui.theme.UASTheme

class ViewRefiningPlanning : ComponentActivity() {
    private val viewModel: UnifiedViewModel by viewModels {
        UnifiedViewModelFactory(AppDatabase.getDatabase(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val refiningPlanId = intent.getIntExtra("refiningPlanId", -1)
        setContent {
            UASTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    refiningPlanId.takeIf { it != -1 }?.let {
                        ViewRefiningPlanningScreen(viewModel, it)
                    }
                }
            }
        }
    }
}

@Composable
fun ViewRefiningPlanningScreen(viewModel: UnifiedViewModel, refiningPlanId: Int) {
    val context = LocalContext.current
    val refiningPlan by viewModel.getRefiningPlanningById(refiningPlanId).collectAsState(initial = null)

    refiningPlan?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF8DA7A8))
                .padding(16.dp)
        ) {
            Text(
                text = "Detail Refining Planning",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            RefiningPlanningDetail(refiningPlan = it)
            Button(
                onClick = {
                    context.startActivity(Intent(context, RefiningPlanning::class.java))
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Kembali")
            }
        }
    } ?: run {
        Text(
            text = "Refining Plan not found",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun RefiningPlanningDetail(refiningPlan: RefiningPlanningEntity) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Nama Model: ${refiningPlan.namaModel ?: ""}", fontSize = 18.sp)
        Text("Identifikasi Masalah: ${refiningPlan.identifikasiMasalah ?: ""}", fontSize = 18.sp)
        Text("Evaluasi Model Awal: ${refiningPlan.evaluasiModelAwal ?: ""}", fontSize = 18.sp)
        Text("Tujuan Refining: ${refiningPlan.tujuanRefining ?: ""}", fontSize = 18.sp)
        Text("Strategi: ${refiningPlan.strategi ?: ""}", fontSize = 18.sp)
        Text("Nama Algoritma: ${refiningPlan.namaAlgoritma ?: ""}", fontSize = 18.sp)
        Text("Kebutuhan Data: ${refiningPlan.kebutuhanData ?: ""}", fontSize = 18.sp)
        Text("Metodologi Pengujian: ${refiningPlan.metodologiPengujian ?: ""}", fontSize = 18.sp)
        Text("Metode Pengukuran: ${refiningPlan.metodePengukuran ?: ""}", fontSize = 18.sp)
    }
}
