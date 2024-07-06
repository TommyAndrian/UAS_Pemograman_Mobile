package com.example.uas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.input.TextFieldValue

import com.example.uas.data.entity.HasilRefiningEntity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.uas.data.AppDatabase
import com.example.uas.ui.theme.UASTheme
import kotlinx.coroutines.launch

class CreateHasilRefining : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the AppDatabase and ViewModel
        val db = AppDatabase.getDatabase(applicationContext)
        val viewModel = ViewModelProvider(
            this,
            UnifiedViewModelFactory(db)
        ).get(UnifiedViewModel::class.java)

        setContent {
            UASTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CreateHasilRefiningScreen(viewModel = viewModel)
                }
            }
        }
    }
}



@Composable
fun CreateHasilRefiningScreen(viewModel: UnifiedViewModel) {
    val context = LocalContext.current

    var projectName by remember { mutableStateOf(TextFieldValue("")) }
    var datasetName by remember { mutableStateOf(TextFieldValue("")) }
    var activity by remember { mutableStateOf(TextFieldValue("")) }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedFileUri = uri
        fileName = uri?.lastPathSegment
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF8DA7A8))
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Hasil Refining",
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

            Text("Hasil Refining:")
            Button(
                onClick = { launcher.launch("*/*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("Pilih File")
            }

            fileName?.let {
                Text("Selected File: $it", color = Color.White, modifier = Modifier.padding(bottom = 16.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        val hasilRefining = HasilRefiningEntity(
                            namaProyek = projectName.text,
                            namaDataset = datasetName.text,
                            aktivitas = activity.text,
                            hasilRefining = selectedFileUri.toString(),
                            namaFile = fileName ?: ""
                        )

                        isLoading = true

                        viewModel.addHasilRefining(hasilRefining)
                        isLoading = false
                        Toast.makeText(context, "Hasil Refining saved!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                ) {
                    Text("Simpan", color = Color.Black)
                }

                Button(
                    onClick = {
                        context.startActivity(Intent(context, HasilRefining::class.java))
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
