package com.example.uas

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.data.AppDatabase
import com.example.uas.data.entity.PemrosesanEntity
import com.example.uas.ui.theme.UASTheme

class PemrosesanData : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UASTheme {
                val db = AppDatabase.getDatabase(applicationContext)
                val pemrosesanViewModel: UnifiedViewModel = viewModel(
                    factory = UnifiedViewModelFactory(db)
                )
                DisplayPemrosesan(pemrosesanViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayPemrosesan(viewModel: UnifiedViewModel) {
    val context = LocalContext.current
    val pemrosesanList by viewModel.pemrosesanList.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var pemrosesanToDelete by remember { mutableStateOf<PemrosesanEntity?>(null) }
    var searchText by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Sedang berlangsung", "Ditunda", "Selesai", "Gagal")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8DA7A8))
            .padding(16.dp)
    ) {
        Text(
            text = "Daftar Pemrosesan Data",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                context.startActivity(Intent(context, CreatePemrosesan::class.java))
            },
        ) {
            Text("Create Pemrosesan Data")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Jenis Aktivitas", fontWeight = FontWeight.Bold)
                    Text("Aksi", fontWeight = FontWeight.Bold)
                    Text("Status", fontWeight = FontWeight.Bold)
                }
            }

            val filteredPemrosesanList = pemrosesanList.filter {
                it.jenisAktivitas.contains(searchText, ignoreCase = true) ||
                        it.deskripsiAktivitas.contains(searchText, ignoreCase = true) ||
                        it.pemrosesan.contains(searchText, ignoreCase = true)
            }

            if (filteredPemrosesanList.isEmpty()) {
                item {
                    Text(
                        text = "No Pemrosesan found",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            } else {
                items(filteredPemrosesanList) { pemrosesan ->
                    var dropdownExpanded by remember { mutableStateOf(false) }
                    var currentStatus by remember { mutableStateOf(pemrosesan.status) }

                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = pemrosesan.jenisAktivitas,
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .weight(1f)
                            )

                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .horizontalScroll(rememberScrollState()),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        val intent = Intent(context, ViewPemrosesan::class.java).apply {
                                            putExtra("pemrosesanId", pemrosesan.id)
                                        }
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Text("View", color = Color.White)
                                }
                                Button(
                                    onClick = {
                                        val intent = Intent(context, EditPemrosesan::class.java).apply {
                                            putExtra("PEMROSESAN_ID", pemrosesan.id)
                                        }
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Text("Edit", color = Color.White)
                                }
                                Button(
                                    onClick = {
                                        pemrosesanToDelete = pemrosesan
                                        showDialog = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Text("Delete", color = Color.White)
                                }

                                // Dropdown Menu for Status
                                ExposedDropdownMenuBox(
                                    expanded = dropdownExpanded,
                                    onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                                ) {
                                    TextField(
                                        value = currentStatus,
                                        onValueChange = { },
                                        readOnly = true,
                                        label = { Text("Status") },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                                        },
                                        modifier = Modifier
                                            .menuAnchor()
                                            .padding(horizontal = 4.dp)
                                    )
                                    ExposedDropdownMenu(
                                        expanded = dropdownExpanded,
                                        onDismissRequest = { dropdownExpanded = false }
                                    ) {
                                        options.forEach { option ->
                                            DropdownMenuItem(
                                                text = { Text(option) },
                                                onClick = {
                                                    currentStatus = option
                                                    dropdownExpanded = false
                                                    // Update the status in Room
                                                    viewModel.updatePemrosesan(pemrosesan.copy(status = option))
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                context.startActivity(Intent(context, MainActivity::class.java))
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Back to Home")
        }
    }

    if (showDialog && pemrosesanToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Konfirmasi") },
            text = { Text("Apakah Anda yakin ingin menghapus pemrosesan ini?") },
            confirmButton = {
                Button(onClick = {
                    pemrosesanToDelete?.let {
                        viewModel.deletePemrosesan(it)
                        showDialog = false
                        pemrosesanToDelete = null
                    }
                }) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                    pemrosesanToDelete = null
                }) {
                    Text("Batal")
                }
            }
        )
    }
}

