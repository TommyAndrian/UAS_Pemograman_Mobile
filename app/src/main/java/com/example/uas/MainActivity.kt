package com.example.uas


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas.data.entity.KomunikasiManajemenEntity
import com.example.uas.ui.theme.UASTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UASTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomePage()
                }
            }
        }
    }
}

@Composable
fun HomePage() {
    Row(modifier = Modifier.fillMaxSize()) {
        SidebarMenu()
        MainContent()
    }
}

@Composable
fun SidebarMenu() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(200.dp)
            .background(Color.DarkGray)
            .padding(16.dp)
    ) {
        Text(
            text = "Intelligence Creation",
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Home",
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable { /* Handle click */ }
        )
        Text(
            text = "Camera",
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable {

                }
        )
        Text(
            text = "Problem Framing",
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable {
                    context.startActivity(Intent(context, ProblemFraming::class.java))
                }
        )
        Text(
            text = "Dataset",
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable {
                    context.startActivity(Intent(context, Dataaset::class.java))
                }
        )
        Text(
            text = "Pemrosesan Data",
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable {
                    context.startActivity(Intent(context, PemrosesanData::class.java))
                }
        )
        Text(
            text = "Model Planning",
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable {
                    context.startActivity(Intent(context, ModelPlanning::class.java))
                }
        )
        Text(
            text = "Refining Planning",
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable {
                    context.startActivity(Intent(context, RefiningPlanning::class.java))
                }
        )
        Text(
            text = "Hasil Training Dan Testiing",
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable {
                    context.startActivity(Intent(context, TrainingTesting::class.java))
                }
        )
        Text(
            text = "Hasil Refining",
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable {
                    context.startActivity(Intent(context, HasilRefining::class.java))
                }
        )
        Text(
            text = "Komunikasi Manajemen",
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable {
                    context.startActivity(Intent(context, KomunikasiManajemen::class.java))
                }
        )
    }
}

@Composable
fun MainContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8DA7A8))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Selamat datang di Dashboard Anda",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Mari mulai proyek pertama Anda!",
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}
