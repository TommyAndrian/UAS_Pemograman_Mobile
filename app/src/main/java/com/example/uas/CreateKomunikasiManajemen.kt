package com.example.uas

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas.data.AppDatabase
import com.example.uas.data.entity.KomunikasiManajemenEntity
import com.example.uas.ui.theme.UASTheme
import kotlinx.coroutines.launch

class CreateKomunikasiManajemen : ComponentActivity() {
    private val unifiedViewModel: UnifiedViewModel by viewModels {
        UnifiedViewModelFactory(AppDatabase.getDatabase(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UASTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CreateManajemenForm(
                        modifier = Modifier.fillMaxSize(),
                        viewModel = unifiedViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun CreateManajemenForm(modifier: Modifier = Modifier, viewModel: UnifiedViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // State variables for form fields
    val namaProyekState = remember { mutableStateOf("") }
    val textState = remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF8DA7A8))
            .padding(16.dp)
    ) {
        Text(
            text = "Komunikasi Manajemen",
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )


        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            label = {Text(text = "Nama Proyek")},
            value = namaProyekState.value,
            onValueChange = { namaProyekState.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            label = {Text(text = "Metodologi Pengujian")},
            value = textState.value,
            onValueChange = { textState.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                if (namaProyekState.value.isNotEmpty() && textState.value.isNotEmpty()) {
                    val newEntity = KomunikasiManajemenEntity(
                        namaProyek = namaProyekState.value,
                        namaManajemen = textState.value
                    )

                    scope.launch {
                        try {
                            viewModel.addKomunikasiManajemen(newEntity)
                            Toast.makeText(context, "Data disimpan dengan ID: ${newEntity.id}", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Save")
            }

            Button(onClick = {
                (context as? ComponentActivity)?.finish()
            }) {
                Text("Kembali")
            }
        }
    }
}
