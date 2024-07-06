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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas.ui.theme.UASTheme
import com.example.uas.data.AppDatabase
import com.example.uas.data.entity.ModelPlanningEntity

class ModelPlanning : ComponentActivity() {
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
                    ModelPlanningScreen(unifiedViewModel)
                }
            }
        }
    }
}

@Composable
fun ModelPlanningScreen(unifiedViewModel: UnifiedViewModel) {
    val context = LocalContext.current
    val modelList by unifiedViewModel.modelPlanningList.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var modelToDelete by remember { mutableStateOf<ModelPlanningEntity?>(null) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8DA7A8))
            .padding(16.dp)
    ) {
        Text(
            text = "Daftar Perencanaan Model",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    context.startActivity(Intent(context, CreateModelPlanning::class.java))
                },
            ) {
                Text("Create Model Planning")
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
                    Text("Nama Model", fontWeight = FontWeight.Bold)
                    Text("Aksi", fontWeight = FontWeight.Bold)
                }
            }

            if (modelList.isEmpty()) {
                item {
                    Text(
                        text = "No Models found",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            } else {
                items(modelList) { model ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = model.namaModel ?: "Unnamed Model",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .weight(1f)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    val intent = Intent(context, ViewModelPlanning::class.java)
                                    intent.putExtra("MODEL_ID", model.id)
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text("View", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    val intent = Intent(context, EditModelPlanning::class.java)
                                    intent.putExtra("MODEL_ID", model.id)
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text("Edit", color = Color.White)
                            }

                            Button(
                                onClick = {
                                    modelToDelete = model
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

    if (showDialog && modelToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this model?") },
            confirmButton = {
                Button(
                    onClick = {
                        unifiedViewModel.deleteModelPlanning(modelToDelete!!)
                        Toast.makeText(context, "Model deleted", Toast.LENGTH_SHORT).show()
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
