package com.example.uas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.lifecycle.ViewModelProvider
import com.example.uas.data.AppDatabase
import com.example.uas.data.entity.HasilRefiningEntity
import com.example.uas.ui.theme.UASTheme
import kotlinx.coroutines.launch

class HasilRefining : ComponentActivity() {
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
                    DisplayRefiningResults(viewModel = viewModel)
                }
            }
        }
    }
}
@Composable
fun DisplayRefiningResults(viewModel: UnifiedViewModel) {
    val context = LocalContext.current

    // Observe the list of HasilRefiningEntity from the ViewModel
    val hasilRefiningResults by viewModel.hasilRefiningList.collectAsState(initial = emptyList())

    var showDialog by remember { mutableStateOf(false) }
    var resultToDelete by remember { mutableStateOf<HasilRefiningEntity?>(null) }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8DA7A8))
            .padding(16.dp)
    ) {
        Text(
            text = "Daftar Hasil Refining",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    context.startActivity(Intent(context, CreateHasilRefining::class.java))
                }
            ) {
                Text("Create Hasil Refining")
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
                    Text("Nama Proyek", fontWeight = FontWeight.Bold)
                    Text("Aksi", fontWeight = FontWeight.Bold)
                }
            }

            if (hasilRefiningResults.isEmpty()) {
                item {
                    Text(
                        text = "No Hasil Refining found",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            } else {
                items(hasilRefiningResults) { result ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = result.namaProyek,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .weight(1f)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Row(
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState()),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    val intent = Intent(context, ViewHasilRefining::class.java)
                                    intent.putExtra("resultId", result.id)
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text("View", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    val intent = Intent(context, EditHasilRefining::class.java)
                                    intent.putExtra("resultId", result.id)
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text("Edit", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    resultToDelete = result
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

    if (showDialog && resultToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this refining result?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteHasilRefining(resultToDelete!!)
                        Toast.makeText(context, "Refining result deleted", Toast.LENGTH_SHORT).show()
                        resultToDelete = null
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
