package com.example.uas

import android.content.Intent
import android.os.Bundle
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
import com.example.uas.ui.theme.UASTheme

class ViewKomunikasiManajemen : ComponentActivity() {
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
                    ViewKomunikasiManajemenScreen(viewModel = unifiedViewModel, manajemenId = manajemenId)
                }
            }
        }
    }
}

@Composable
fun ViewKomunikasiManajemenScreen(viewModel: UnifiedViewModel, manajemenId: Int) {
    val context = LocalContext.current
    val manajemen by viewModel.getKomunikasiManajemenById(manajemenId).collectAsState(initial = null)

    manajemen?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF8DA7A8))
                .padding(16.dp)
        ) {
            Text(
                text = "Komunikasi Manajemen Details",
                fontSize = 28.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text("Nama Proyek: ${it.namaProyek}", fontSize = 18.sp, color = Color.Black, modifier = Modifier.padding(bottom = 8.dp))
            Text("Metodologi Pengujian: ${it.namaManajemen}", fontSize = 18.sp, color = Color.Black, modifier = Modifier.padding(bottom = 8.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    context.startActivity(Intent(context, KomunikasiManajemen::class.java))
                },
            ) {
                Text("Kembali")
            }
        }
    }
}
