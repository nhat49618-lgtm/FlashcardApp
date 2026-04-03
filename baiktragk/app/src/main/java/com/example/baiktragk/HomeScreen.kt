package com.example.baiktragk

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {

    var name by remember { mutableStateOf("") }
    var credits by remember { mutableStateOf("") }
    var isTheory by remember { mutableStateOf(true) }

    val subjectList = viewModel.subjects
    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text("Quản lý môn học")
                },

                actions = {

                    IconButton(onClick = {

                        val lt = viewModel.getTheoryCredits()
                        val th = viewModel.getPracticeCredits()

                        navController.navigate("invoice/$lt/$th")

                    }) {

                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Checkout",
                            tint = Color.Red
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

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Tên môn học") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = credits,
                onValueChange = { credits = it },
                label = { Text("Số tín chỉ") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text("Lý thuyết")

                Spacer(modifier = Modifier.width(8.dp))

                Switch(
                    checked = isTheory,
                    onCheckedChange = { isTheory = it }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {

                if (name.isNotEmpty() && credits.isNotEmpty()) {

                    viewModel.addSubject(
                        Subject(
                            name = name,
                            credits = credits.toInt(),
                            isTheory = isTheory
                        )
                    )

                    name = ""
                    credits = ""
                }

            }) {
                Text("Thêm")
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn {

                items(subjectList) { subject ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),

                        horizontalArrangement =
                            Arrangement.SpaceBetween,

                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            "${subject.name} - ${subject.credits} tín chỉ"
                        )

                        IconButton(onClick = {

                            viewModel.removeSubject(subject)

                        }) {

                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                }
            }
        }
    }
}