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

class EditRefiningPlanning : ComponentActivity() {
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
                        EditRefiningForm(viewModel, it)
                    }
                }
            }
        }
    }
}

@Composable
fun EditRefiningForm(viewModel: UnifiedViewModel, refiningPlanId: Int) {
    val context = LocalContext.current
    val refiningPlan by viewModel.getRefiningPlanningById(refiningPlanId).collectAsState(initial = null)

    var namaModel by remember { mutableStateOf("") }
    var identifikasiMasalah by remember { mutableStateOf("") }
    var evaluasiModelAwal by remember { mutableStateOf("") }
    var tujuanRefining by remember { mutableStateOf("") }
    var strategi by remember { mutableStateOf("") }
    var namaAlgoritma by remember { mutableStateOf("") }
    var kebutuhanData by remember { mutableStateOf("") }
    var metodologiPengujian by remember { mutableStateOf("") }
    var metodePengukuran by remember { mutableStateOf("") }

    LaunchedEffect(refiningPlan) {
        refiningPlan?.let {
            namaModel = it.namaModel ?: ""
            identifikasiMasalah = it.identifikasiMasalah ?: ""
            evaluasiModelAwal = it.evaluasiModelAwal ?: ""
            tujuanRefining = it.tujuanRefining ?: ""
            strategi = it.strategi ?: ""
            namaAlgoritma = it.namaAlgoritma ?: ""
            kebutuhanData = it.kebutuhanData ?: ""
            metodologiPengujian = it.metodologiPengujian ?: ""
            metodePengukuran = it.metodePengukuran ?: ""
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8DA7A8))
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "Edit Refining Planning",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        item {
            OutlinedTextField(
                value = namaModel,
                onValueChange = { namaModel = it },
                label = { Text("Nama Model") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = identifikasiMasalah,
                onValueChange = { identifikasiMasalah = it },
                label = { Text("Identifikasi Masalah") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = evaluasiModelAwal,
                onValueChange = { evaluasiModelAwal = it },
                label = { Text("Evaluasi Model Awal") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = tujuanRefining,
                onValueChange = { tujuanRefining = it },
                label = { Text("Tujuan Refining") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = strategi,
                onValueChange = { strategi = it },
                label = { Text("Strategi") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = namaAlgoritma,
                onValueChange = { namaAlgoritma = it },
                label = { Text("Nama Algoritma") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = kebutuhanData,
                onValueChange = { kebutuhanData = it },
                label = { Text("Kebutuhan Data") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = metodologiPengujian,
                onValueChange = { metodologiPengujian = it },
                label = { Text("Metodologi Pengujian") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = metodePengukuran,
                onValueChange = { metodePengukuran = it },
                label = { Text("Metode Pengukuran") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        val updatedPlan = refiningPlan?.copy(
                            namaModel = namaModel,
                            identifikasiMasalah = identifikasiMasalah,
                            evaluasiModelAwal = evaluasiModelAwal,
                            tujuanRefining = tujuanRefining,
                            strategi = strategi,
                            namaAlgoritma = namaAlgoritma,
                            kebutuhanData = kebutuhanData,
                            metodologiPengujian = metodologiPengujian,
                            metodePengukuran = metodePengukuran
                        )

                        updatedPlan?.let {
                            viewModel.updateRefiningPlanning(it)
                            Toast.makeText(context, "Refining Plan updated", Toast.LENGTH_SHORT).show()
                            context.startActivity(Intent(context, RefiningPlanning::class.java))
                        }
                    }
                ) {
                    Text("Update")
                }

                Button(
                    onClick = {
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }
                ) {
                    Text("Kembali")
                }
            }
        }
    }
}
