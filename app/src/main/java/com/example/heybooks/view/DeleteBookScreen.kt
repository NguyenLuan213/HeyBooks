package com.example.heybooks.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.viewmodel.MainViewModel

@Composable
fun DeleteBookScreen(viewModel: MainViewModel,
                     actions: MainActions,
                     isbn: String,
                     showDelDialog: Int,
                     onDismiss: () -> Unit){
    val content = LocalContext.current
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {Text("Confirm deletion",style = TextStyle(color = Color(0xFF2196F3), fontSize = 20.sp))

        },
        text = {
            androidx.compose.material3.Text("Are you sure delete ?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.deleteBook(isbn, content)
                    onDismiss()
                }
            ) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(text = "Cancel")
            }
        }
    )
}