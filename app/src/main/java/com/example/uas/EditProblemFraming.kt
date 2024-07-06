package com.example.uas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.uas.data.AppDatabase
import com.example.uas.data.entity.ProblemFraming
import com.example.uas.ui.theme.UASTheme
import kotlinx.coroutines.launch

class EditProblemFraming : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val problemFramingId = intent.getIntExtra("PROBLEM_FRAMING_ID", -1)
        setContent {
            UASTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EditProblemFramingScreen(problemFramingId)
                }
            }
        }
    }
}

@Composable
fun EditProblemFramingScreen(problemFramingId: Int) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val problemFramingDao = db.problemFramingDao()

    var problemFraming by remember { mutableStateOf<ProblemFraming?>(null) }
    var namaProyek by remember { mutableStateOf(TextFieldValue("")) }
    var deskripsi by remember { mutableStateOf(TextFieldValue("")) }
    var algoritma by remember { mutableStateOf(TextFieldValue("")) }
    var output by remember { mutableStateOf(TextFieldValue("")) }

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(problemFramingId) {
        problemFraming = problemFramingDao.getById(problemFramingId)
        problemFraming?.let {
            namaProyek = TextFieldValue(it.namaProyek)
            deskripsi = TextFieldValue(it.deskripsi)
            algoritma = TextFieldValue(it.algoritma)
            output = TextFieldValue(it.output)
        }
        isLoading = false
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF8DA7A8))
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Edit Problem Framing",
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
                    label = { Text("Deskripsi") },
                    value = deskripsi,
                    onValueChange = { deskripsi = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    label = { Text("Algoritma") },
                    value = algoritma,
                    onValueChange = { algoritma = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    label = { Text("Output") },
                    value = output,
                    onValueChange = { output = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            val updatedProblemFraming = problemFraming?.copy(
                                namaProyek = namaProyek.text,
                                deskripsi = deskripsi.text,
                                algoritma = algoritma.text,
                                output = output.text
                            )
                            if (updatedProblemFraming != null) {
                                isLoading = true
                                (context as EditProblemFraming).lifecycleScope.launch {
                                    problemFramingDao.update(updatedProblemFraming)
                                    isLoading = false
                                    Toast.makeText(context, "Data berhasil diperbarui", Toast.LENGTH_SHORT)
                                        .show()
                                    context.finish()
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                    ) {
                        Text("Update", color = Color.Black)
                    }

                    Button(
                        onClick = {
                            context.startActivity(Intent(context, com.example.uas.ProblemFraming::class.java))
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
                        Text("Memperbarui Data...", color = Color.White)
                    }
                }
            }
        }
    }
}
