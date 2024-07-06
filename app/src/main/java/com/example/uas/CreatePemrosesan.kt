package com.example.uas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import com.example.uas.data.AppDatabase
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.data.entity.PemrosesanEntity
import com.example.uas.ui.theme.UASTheme
import kotlinx.coroutines.launch

class CreatePemrosesan : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UASTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val viewModel: UnifiedViewModel = viewModel(factory = UnifiedViewModelFactory(database))

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { innerPadding ->
            FormScreen(
                modifier = Modifier.padding(innerPadding),
                snackbarHostState = snackbarHostState,
                viewModel = viewModel
            )
        }
    )
}

@Composable
fun FormScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    viewModel: UnifiedViewModel
) {
    val context = LocalContext.current
    var jenisAktivitas by remember { mutableStateOf(TextFieldValue()) }
    var deskripsiAktivitas by remember { mutableStateOf(TextFieldValue()) }
    var pemrosesan by remember { mutableStateOf(TextFieldValue()) }
    var hasilUri by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf<String?>(null) }
    var selectedStatus by remember { mutableStateOf("") }
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
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF8DA7A8))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Pemrosesan Data",
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
                value = pemrosesan,
                onValueChange = { pemrosesan = it },
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
                            val pemrosesanEntity = PemrosesanEntity(
                                jenisAktivitas = jenisAktivitas.text,
                                deskripsiAktivitas = deskripsiAktivitas.text,
                                pemrosesan = pemrosesan.text,
                                hasilUri = hasilUri?.toString() ?: "",
                                status = selectedStatus
                            )
                            viewModel.addPemrosesan(pemrosesanEntity)
                            snackbarHostState.showSnackbar("Data disimpan")

                            // Reset fields
                            jenisAktivitas = TextFieldValue("")
                            deskripsiAktivitas = TextFieldValue("")
                            pemrosesan = TextFieldValue("")
                            hasilUri = null
                            fileName = null
                            selectedStatus = "Select Completeness Status"

                            // Navigate to MainActivity10
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                ) {
                    Text(text = "Simpan")
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
