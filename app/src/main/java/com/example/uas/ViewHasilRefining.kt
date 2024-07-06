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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.uas.data.AppDatabase
import com.example.uas.data.entity.HasilRefiningEntity
import com.example.uas.ui.theme.UASTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ViewHasilRefining : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the AppDatabase and ViewModel
        val db = AppDatabase.getDatabase(applicationContext)
        val viewModel = ViewModelProvider(
            this,
            UnifiedViewModelFactory(db)
        ).get(UnifiedViewModel::class.java)

        val resultId = intent.getIntExtra("resultId", -1)

        setContent {
            UASTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ViewHasilRefiningScreen(viewModel = viewModel, resultId = resultId)
                }
            }
        }
    }
}

@Composable
fun ViewHasilRefiningScreen(viewModel: UnifiedViewModel, resultId: Int) {
    val context = LocalContext.current
    val result by viewModel.getHasilRefiningById(resultId).collectAsState(initial = null)

    // Function to open the file in a given Uri
    fun openFile(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, context.contentResolver.getType(uri))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Open File"))
    }

    result?.let { hasilRefining ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF8DA7A8))
                .padding(16.dp)
        ) {
            Text(
                text = "Hasil Refining Details",
                fontSize = 28.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text("Nama Proyek: ${hasilRefining.namaProyek}", fontSize = 18.sp, color = Color.Black, modifier = Modifier.padding(bottom = 8.dp))
            Text("Nama Dataset: ${hasilRefining.namaDataset}", fontSize = 18.sp, color = Color.Black, modifier = Modifier.padding(bottom = 8.dp))
            Text("Aktivitas: ${hasilRefining.aktivitas}", fontSize = 18.sp, color = Color.Black, modifier = Modifier.padding(bottom = 8.dp))
            Text("Hasil Refining: ${hasilRefining.namaFile}", fontSize = 18.sp, color = Color.Black, modifier = Modifier.padding(bottom = 8.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    context.startActivity(Intent(context, HasilRefining::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Kembali")
            }
        }
    }
}
