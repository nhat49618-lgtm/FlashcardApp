package com.example.baiktragk

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceScreen(
    navController: NavController,
    lt: Int,
    th: Int
) {

    val x = 9

    val theoryPrice = 500000 + (x * 10000)

    val practicePrice = theoryPrice + 50000

    val theoryTotal = lt * theoryPrice

    val practiceTotal = th * practicePrice

    val total = theoryTotal + practiceTotal

    Scaffold(

        topBar = {

            TopAppBar(

                title = { Text("Hóa đơn học phí") },

                navigationIcon = {

                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Text("Tín chỉ lý thuyết: $lt")

            Spacer(modifier = Modifier.height(8.dp))

            Text("Tín chỉ thực hành: $th")

            Spacer(modifier = Modifier.height(20.dp))

            Text("Đơn giá lý thuyết: $theoryPrice VND")

            Spacer(modifier = Modifier.height(8.dp))

            Text("Đơn giá thực hành: $practicePrice VND")

            Spacer(modifier = Modifier.height(20.dp))

            Text("Tiền lý thuyết: $theoryTotal VND")

            Spacer(modifier = Modifier.height(8.dp))

            Text("Tiền thực hành: $practiceTotal VND")

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "TỔNG HỌC PHÍ: $total VND",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(40.dp))

            CertificationBadge(x)

        }
    }
}

@Composable
fun CertificationBadge(x: Int) {

    val radius = (36 + x).dp

    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.size(radius * 2)
        ) {

            drawCircle(
                color = Color.Blue,
                style = Stroke(width = 8f)
            )

            val textLayoutResult = textMeasurer.measure(
                text = x.toString()
            )

            drawText(
                textLayoutResult,
                topLeft = Offset(
                    (size.width - textLayoutResult.size.width) / 2,
                    (size.height - textLayoutResult.size.height) / 2
                )
            )
        }
    }
}