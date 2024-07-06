package com.example.uas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas.data.AppDatabase
import com.example.uas.data.entity.ModelPlanningEntity
import com.example.uas.ui.theme.UASTheme

class EditModelPlanning : ComponentActivity() {
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
                        EditModelPlanningScreen(unifiedViewModel, it)
                    }
                }
            }
        }
    }
}

@Composable
fun EditModelPlanningScreen(unifiedViewModel: UnifiedViewModel, modelId: Int) {
    val model by unifiedViewModel.getModelPlanningById(modelId).collectAsState(initial = null)

    model?.let { nonNullModel ->
        var namaModel by remember { mutableStateOf(nonNullModel.namaModel ?: "") }
        var deskripsiModel by remember { mutableStateOf(nonNullModel.deskripsiModel ?: "") }
        var tujuanModel by remember { mutableStateOf(nonNullModel.tujuanModel ?: "") }
        var namaAlgoritma by remember { mutableStateOf(nonNullModel.namaAlgoritma ?: "") }
        var kebutuhanData by remember { mutableStateOf(nonNullModel.kebutuhanData ?: "") }
        var metodologiPengujian by remember { mutableStateOf(nonNullModel.metodologiPengujian ?: "") }
        var metodePengukuran by remember { mutableStateOf(nonNullModel.metodePengukuran ?: "") }

        val context = LocalContext.current
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF8DA7A8))
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Edit Perencanaan Model",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                label = { Text("Nama Model") },
                value = namaModel,
                onValueChange = { newValue: String -> namaModel = newValue },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                label = { Text("Deskripsi Model") },
                value = deskripsiModel,
                onValueChange = { newValue: String -> deskripsiModel = newValue },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 4
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                label = { Text("Tujuan Model") },
                value = tujuanModel,
                onValueChange = { newValue: String -> tujuanModel = newValue },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 4
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                label = { Text("Nama Algoritma") },
                value = namaAlgoritma,
                onValueChange = { newValue: String -> namaAlgoritma = newValue },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                label = { Text("Kebutuhan Data") },
                value = kebutuhanData,
                onValueChange = { newValue: String -> kebutuhanData = newValue },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 4
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                label = { Text("Metodologi Pengujian") },
                value = metodologiPengujian,
                onValueChange = { newValue: String -> metodologiPengujian = newValue },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 4
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                label = { Text("Metode Pengukuran") },
                value = metodePengukuran,
                onValueChange = { newValue: String -> metodePengukuran = newValue },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 4
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val updatedModel = nonNullModel.copy(
                            namaModel = namaModel,
                            deskripsiModel = deskripsiModel,
                            tujuanModel = tujuanModel,
                            namaAlgoritma = namaAlgoritma,
                            kebutuhanData = kebutuhanData,
                            metodologiPengujian = metodologiPengujian,
                            metodePengukuran = metodePengukuran
                        )

                        unifiedViewModel.updateModelPlanning(updatedModel)

                        Toast.makeText(context, "Data updated", Toast.LENGTH_SHORT).show()
                        context.startActivity(Intent(context, ModelPlanning::class.java))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                ) {
                    Text("Simpan", color = Color.Black)
                }

                Button(
                    onClick = {
                        context.startActivity(Intent(context, ModelPlanning::class.java))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                ) {
                    Text("Kembali ke Daftar", color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
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
