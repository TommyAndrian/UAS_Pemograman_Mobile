package com.example.uas

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.uas.data.AppDatabase
import com.example.uas.data.entity.ProblemFraming
import com.example.uas.ui.theme.UASTheme
import kotlinx.coroutines.launch

class ViewProblemFraming : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val projectId = intent.getIntExtra("projectId", -1)
        setContent {
            UASTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ViewFramingScreen(projectId = projectId)
                }
            }
        }
    }
}

@Composable
fun ViewFramingScreen(projectId: Int) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val problemFramingDao = db.problemFramingDao()

    var project by remember { mutableStateOf<ProblemFraming?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(projectId) {
        project = problemFramingDao.getById(projectId)
        isLoading = false
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        project?.let { project ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF8DA7A8))
                    .padding(32.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Project Details",
                    fontSize = 24.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Nama Proyek: ${project.namaProyek}",
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Deskripsi: ${project.deskripsi}",
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Algoritma: ${project.algoritma}",
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Output: ${project.output}",
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        context.startActivity(Intent(context, ProblemFraming::class.java))
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 16.dp)
                ) {
                    Text("Kembali", color = Color.White)
                }
            }
        } ?: run {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF8DA7A8))
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Project not found",
                    fontSize = 24.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = {
                        context.startActivity(Intent(context, com.example.uas.ProblemFraming::class.java))
                    }
                ) {
                    Text("Kembali", color = Color.White)
                }
            }
        }
    }
}
