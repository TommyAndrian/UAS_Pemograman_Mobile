package com.example.uas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.runtime.collectAsState

class ViewPemrosesan : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pemrosesanId = intent.getIntExtra("pemrosesanId", -1)
        setContent {
            UASTheme {
                val db = AppDatabase.getDatabase(applicationContext)
                val viewModel: UnifiedViewModel = viewModel(factory = UnifiedViewModelFactory(db))
                val pemrosesan = viewModel.getPemrosesanById(pemrosesanId).collectAsState(initial = null)
                pemrosesan.value?.let {
                    DetailPemrosesanScreen(viewModel = viewModel, pemrosesan = it)
                }
            }
        }
    }
}

@Composable
fun DetailPemrosesanScreen(viewModel: UnifiedViewModel, pemrosesan: PemrosesanEntity) {
    val context = LocalContext.current
    var showLoading by remember { mutableStateOf(false) }

    // Function to open the file in a given Uri
    fun openFile(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, context.contentResolver.getType(uri))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Open File"))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8DA7A8))
            .padding(16.dp)
    ) {
        Text(
            text = "Detail Pemrosesan",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Jenis Aktivitas: ${pemrosesan.jenisAktivitas}",
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Deskripsi Aktivitas: ${pemrosesan.deskripsiAktivitas}",
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Dataset yang digunakan: ${pemrosesan.pemrosesan}",
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        pemrosesan.hasilUri?.let {
            Text(
                text = "File Hasil: $it",
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Text(
            text = "Status: ${pemrosesan.status}",
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                context.startActivity(Intent(context, PemrosesanData::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Kembali")
        }
    }
}
