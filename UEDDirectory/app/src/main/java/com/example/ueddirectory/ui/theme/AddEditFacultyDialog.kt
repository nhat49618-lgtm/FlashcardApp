package com.example.ueddirectory.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ueddirectory.model.Faculty

@Composable
fun AddEditFacultyDialog(
    faculty: Faculty?,
    imageList: List<Int>,
    onSave: (Faculty) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(faculty?.name ?: "") }
    var desc by remember { mutableStateOf(faculty?.description ?: "") }
    var contact by remember { mutableStateOf(faculty?.contact ?: "") }
    var image by remember { mutableStateOf(faculty?.imageRes ?: imageList[0]) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onSave(
                    Faculty(
                        id = faculty?.id ?: System.currentTimeMillis().toInt(),
                        name = name,
                        description = desc,
                        imageRes = image,
                        contact = contact
                    )
                )
            }) { Text("Lưu") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Huỷ") }
        },
        title = { Text(if (faculty == null) "Thêm khoa" else "Sửa khoa") },
        text = {
            Column {
                OutlinedTextField(name, { name = it }, label = { Text("Tên khoa") })
                OutlinedTextField(desc, { desc = it }, label = { Text("Mô tả") })
                OutlinedTextField(contact, { contact = it }, label = { Text("Liên hệ") })
                Spacer(Modifier.height(8.dp))
                LazyRow {
                    items(imageList) {
                        Image(
                            painter = painterResource(it),
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .padding(4.dp)
                                .clickable { image = it }
                        )
                    }
                }
            }
        }
    )
}
