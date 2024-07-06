package com.example.uas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

class CreateRefiningPlanning : ComponentActivity() {
    private lateinit var viewModel: UnifiedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(applicationContext)
        viewModel = UnifiedViewModelFactory(db).create(UnifiedViewModel::class.java)
        setContent {
            UASTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CreateRefiningForm(viewModel)
                }
            }
        }
    }
}

@Composable
fun CreateRefiningForm(viewModel: UnifiedViewModel) {
    val context = LocalContext.current

    var namaModel by remember { mutableStateOf("") }
    var identifikasiMasalah by remember { mutableStateOf("") }
    var evaluasiModelAwal by remember { mutableStateOf("") }
    var tujuanRefining by remember { mutableStateOf("") }
    var strategi by remember { mutableStateOf("") }
    var namaAlgoritma by remember { mutableStateOf("") }
    var kebutuhanData by remember { mutableStateOf("") }
    var metodologiPengujian by remember { mutableStateOf("") }
    var metodologiPengukuran by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8DA7A8))
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "Create Refining Planning",
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
                value = metodologiPengukuran,
                onValueChange = { metodologiPengukuran = it },
                label = { Text("Metodologi Pengukuran") },
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
                        val refiningPlan = RefiningPlanningEntity(
                            namaModel = namaModel,
                            identifikasiMasalah = identifikasiMasalah,
                            evaluasiModelAwal = evaluasiModelAwal,
                            tujuanRefining = tujuanRefining,
                            strategi = strategi,
                            namaAlgoritma = namaAlgoritma,
                            kebutuhanData = kebutuhanData,
                            metodologiPengujian = metodologiPengujian,
                            metodePengukuran = metodologiPengukuran
                        )

                        viewModel.addRefiningPlanning(refiningPlan)
                        Toast.makeText(context, "Refining Plan saved successfully", Toast.LENGTH_SHORT).show()
                        context.startActivity(Intent(context, RefiningPlanning::class.java))
                    },
                ) {
                    Text("Simpan", color = Color.White)
                }

                Button(
                    onClick = {
                        context.startActivity(Intent(context, RefiningPlanning::class.java))
                    },
                ) {
                    Text("Kembali", color = Color.White)
                }
            }
        }
    }
}
