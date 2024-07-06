// EditPemrosesan.kt
package com.example.uas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.data.AppDatabase
import com.example.uas.data.entity.PemrosesanEntity
import com.example.uas.ui.theme.UASTheme
import kotlinx.coroutines.launch

class EditPemrosesan : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pemrosesanId = intent.getIntExtra("PEMROSESAN_ID", -1)
        setContent {
            UASTheme {
                val db = AppDatabase.getDatabase(applicationContext)
                val viewModel: UnifiedViewModel = viewModel(factory = UnifiedViewModelFactory(db))
                val pemrosesan = viewModel.getPemrosesanById(pemrosesanId).collectAsState(initial = null)
                pemrosesan.value?.let {
                    EditPemrosesanScreen(viewModel = viewModel, pemrosesan = it)
                }
            }
        }
    }
}

@Composable
fun EditPemrosesanScreen(viewModel: UnifiedViewModel, pemrosesan: PemrosesanEntity) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var jenisAktivitas by remember { mutableStateOf(TextFieldValue(pemrosesan.jenisAktivitas)) }
    var deskripsiAktivitas by remember { mutableStateOf(TextFieldValue(pemrosesan.deskripsiAktivitas)) }
    var pemrosesanValue by remember { mutableStateOf(TextFieldValue(pemrosesan.pemrosesan)) }
    var hasilUri by remember { mutableStateOf<Uri?>(Uri.parse(pemrosesan.hasilUri)) }
    var fileName by remember { mutableStateOf<String?>(hasilUri?.lastPathSegment) }
    var selectedStatus by remember { mutableStateOf(pemrosesan.status) }
    val scope = rememberCoroutineScope()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        hasilUri = uri
        fileName = uri?.lastPathSegment
        scope.launch {
            snackbarHostState.showSnackbar("File diunggah: $fileName")
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8DA7A8))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Edit Pemrosesan Data",
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            OutlinedTextField(
                label = { Text("Jenis aktivitas") },
                value = jenisAktivitas,
                onValueChange = { jenisAktivitas = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            OutlinedTextField(
                label = { Text("Deskripsi aktivitas") },
                value = deskripsiAktivitas,
                onValueChange = { deskripsiAktivitas = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            OutlinedTextField(
                label = { Text("Dataset yang digunakan") },
                value = pemrosesanValue,
                onValueChange = { pemrosesanValue = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            fileName?.let {
                Text(text = "File yang diunggah: $it", color = Color.Black)
                Spacer(modifier = Modifier.height(10.dp))
            }
            Button(
                onClick = { filePickerLauncher.launch("*/*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Text(text = "Upload File Hasil")
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            val updatedPemrosesan = pemrosesan.copy(
                                jenisAktivitas = jenisAktivitas.text,
                                deskripsiAktivitas = deskripsiAktivitas.text,
                                pemrosesan = pemrosesanValue.text,
                                hasilUri = hasilUri?.toString() ?: "",
                                status = selectedStatus
                            )
                            viewModel.updatePemrosesan(updatedPemrosesan)
                            snackbarHostState.showSnackbar("Data diperbarui")

                            // Navigate back to PemrosesanData
                            val intent = Intent(context, PemrosesanData::class.java)
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                ) {
                    Text(text = "Perbarui")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        context.startActivity(Intent(context, PemrosesanData::class.java))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                ) {
                    Text(text = "Kembali")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
