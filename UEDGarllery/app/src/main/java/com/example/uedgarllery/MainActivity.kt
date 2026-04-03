package com.example.uedgallery // Thay tên package của bạn tại đây

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// BƯỚC 2: Định nghĩa mô hình dữ liệu (Data Model)
data class UEDArtwork(
    val imageRes: Int,
    val title: String,
    val info: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UEDGalleryApp()
                }
            }
        }
    }
}

@Composable
fun UEDGalleryApp() {
    // 1. Khởi tạo danh sách dữ liệu (Đảm bảo bạn đã có các ảnh này trong res/drawable)
    val artworks = listOf(
        UEDArtwork(R.drawable.pic1, "Cổng trường UED", "60 năm xây dựng và phát triển"),
        UEDArtwork(R.drawable.pic2, "Tòa nhà Hành chính", "Trung tâm điều hành giáo dục"),
        UEDArtwork(R.drawable.pic3, "Thư viện hiện đại", "Không gian tự học lý tưởng"),
        UEDArtwork(R.drawable.pic4, "Giảng đường A1", "Nơi diễn ra các tiết học thú vị"),
        UEDArtwork(R.drawable.pic5, "Sân bóng đá", "Khu thể thao năng động cho sinh viên")
    )

    // 2. Trạng thái (State) quản lý chỉ số ảnh hiện tại
    var currentIndex by remember { mutableStateOf(0) }
    val currentData = artworks[currentIndex]

    // 3. Giao diện chính sử dụng Column
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding() // Tránh đè lên thanh pin/sóng
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Giúp cuộn được khi xoay ngang máy
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TIÊU ĐỀ APP (Theo yêu cầu: UED Gallery Nhóm X)
        Text(
            text = "UED Gallery Nhóm 01",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
        )

        // KHUNG HIỂN THỊ HÌNH ẢNH (Sử dụng Box và Surface)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f), // Giữ khung hình vuông để không bị méo
            shadowElevation = 10.dp,
            border = BorderStroke(2.dp, Color.LightGray)
        ) {
            Image(
                painter = painterResource(id = currentData.imageRes),
                contentDescription = currentData.title,
                contentScale = ContentScale.Crop, // Cắt ảnh cho vừa khung
                modifier = Modifier.padding(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // KHUNG THÔNG TIN CHI TIẾT
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            color = Color(0xFFF5F5F5),
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = currentData.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Light
                )
                Text(
                    text = currentData.info,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Tạo khoảng trống linh hoạt
        Spacer(modifier = Modifier.weight(1f))

        // NÚT ĐIỀU HƯỚNG (Sử dụng Row)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp, top = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Nút Previous
            Button(
                onClick = {
                    currentIndex = if (currentIndex == 0) artworks.size - 1 else currentIndex - 1
                },
                modifier = Modifier.width(140.dp)
            ) {
                Text("Previous")
            }

            // Nút Next
            Button(
                onClick = {
                    currentIndex = (currentIndex + 1) % artworks.size
                },
                modifier = Modifier.width(140.dp)
            ) {
                Text("Next")
            }
        }
    }
}