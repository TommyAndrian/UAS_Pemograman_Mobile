package com.example.uas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas.data.AppDatabase
import com.example.uas.data.entity.KomunikasiManajemenEntity
import com.example.uas.ui.theme.UASTheme
import kotlinx.coroutines.launch

class EditKomunikasiManajemen : ComponentActivity() {
    private val unifiedViewModel: UnifiedViewModel by viewModels {
        UnifiedViewModelFactory(AppDatabase.getDatabase(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val manajemenId = intent.getIntExtra("manajemenId", -1)

        setContent {
            UASTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EditKomunikasiManajemenScreen(viewModel = unifiedViewModel, manajemenId = manajemenId)
                }
            }
        }
    }
}

@Composable
fun EditKomunikasiManajemenScreen(viewModel: UnifiedViewModel, manajemenId: Int) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val manajemen by viewModel.getKomunikasiManajemenById(manajemenId).collectAsState(initial = null)

    var namaProyek by remember { mutableStateOf("") }
    var metodologi by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(manajemen) {
        manajemen?.let {
            namaProyek = it.namaProyek
            metodologi = it.namaManajemen
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8DA7A8))
            .padding(16.dp)
    ) {
        Text(
            text = "Edit Komunikasi Manajemen",
            fontSize = 28.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            label = { Text("Nama Proyek") },
            value = namaProyek,
            onValueChange = { namaProyek = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            label = { Text("Metodologi Pengujian") },
            value = metodologi,
            onValueChange = { metodologi = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (namaProyek.isNotEmpty() && metodologi.isNotEmpty()) {
                        val updatedEntity = KomunikasiManajemenEntity(
                            id = manajemenId,
                            namaProyek = namaProyek,
                            namaManajemen = metodologi
                        )

                        isLoading = true
                        scope.launch {
                            try {
                                viewModel.updateKomunikasiManajemen(updatedEntity)
                                Toast.makeText(context, "Data updated successfully!", Toast.LENGTH_SHORT).show()
                                context.startActivity(Intent(context, KomunikasiManajemen::class.java))
                            } catch (e: Exception) {
                                Toast.makeText(context, "Failed to update data: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally {
                                isLoading = false
                            }
                        }
                    } else {
                        Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f).padding(end = 8.dp),
            ) {
                Text("Update", color = Color.Black)
            }

            Button(
                onClick = {
                    context.startActivity(Intent(context, KomunikasiManajemen::class.java))
                },
                modifier = Modifier.weight(1f).padding(start = 8.dp),
            ) {
                Text("Kembali", color = Color.Black)
            }
        }
    }
}
