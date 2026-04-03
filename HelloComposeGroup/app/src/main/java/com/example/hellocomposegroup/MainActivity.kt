package com.example.hellocomposegroup
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Hiển thị giao diện chính
            GroupProfileScreen()
        }
    }
}

@Composable
fun GroupProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo_nhom),
            contentDescription = "Group Logo",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "NHÓM 9 ",
            style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        MemberText(name = "Trần Nguyễn Hoàng Nhật", color = Color.Blue)
        MemberText(name = "Trần Công Nghĩa", color = Color.Red)
        MemberText(name = "Hoàng Xuân Mai", color = Color.Green)
        Spacer(modifier = Modifier.height(24.dp))

        val context = LocalContext.current
        Button(onClick = {
            Toast.makeText(context, "Xin Chào Android", Toast.LENGTH_SHORT).show()
        }) {
            Text("Say Hello")
        }
    }
}

@Composable
fun MemberText(name: String, color: Color) {
    Text(
        text = name,
        color = color,
        fontSize = 20.sp,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GroupProfileScreen()
}