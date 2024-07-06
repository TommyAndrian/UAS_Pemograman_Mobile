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
import androidx.compose.foundation.lazy.items
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
import com.example.uas.data.entity.KomunikasiManajemenEntity
import com.example.uas.ui.theme.UASTheme

class KomunikasiManajemen : ComponentActivity() {
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
                    DisplayKomunikasiManajemenList(viewModel = unifiedViewModel)
                }
            }
        }
    }
}
@Composable
fun DisplayKomunikasiManajemenList(viewModel: UnifiedViewModel) {
    val context = LocalContext.current
    val komunikasiList by viewModel.komunikasiManajemenList.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var komunikasiToDelete by remember { mutableStateOf<KomunikasiManajemenEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8DA7A8))
            .padding(16.dp)
    ) {
        Text(
            text = "Daftar Komunikasi Manajemen",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    context.startActivity(Intent(context, CreateKomunikasiManajemen::class.java))
                },
            ) {
                Text("Create Komunikasi Manajemen")
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
                    Text("Nama Proyek", fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Aksi", fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }

            if (komunikasiList.isEmpty()) {
                item {
                    Text(
                        text = "No result found",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = Color.White
                    )
                }
            } else {
                items(komunikasiList) { komunikasi ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = komunikasi.namaProyek,
                            modifier = Modifier.weight(1f),
                            color = Color.Black
                        )
                        Row(
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {
                                    val intent = Intent(context, ViewKomunikasiManajemen::class.java)
                                    intent.putExtra("manajemenId", komunikasi.id)
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text("View", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    val intent = Intent(context, EditKomunikasiManajemen::class.java)
                                    intent.putExtra("manajemenId", komunikasi.id)
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text("Edit", color = Color.White  )
                            }
                            Button(
                                onClick = {
                                    komunikasiToDelete = komunikasi
                                    showDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text("Delete", color = Color.White)
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

    if (showDialog && komunikasiToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this data?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteKomunikasiManajemen(komunikasiToDelete!!)
                        Toast.makeText(context, "Data deleted", Toast.LENGTH_SHORT).show()
                        showDialog = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
