package com.example.heybooks.components

import android.widget.Toast
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.heybooks.R


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun OptionMenu(){

    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val search = remember {
        mutableStateOf("")
    }

    TopAppBar(
        title = { Text("") },
        actions = {

//            IconButton(onClick = { Toast.makeText(context, "Favorite", Toast.LENGTH_SHORT).show() }) {
//                Icon(Icons.Default.Favorite, "")
//            }
            TextInputField(
                label = stringResource(R.string.text_search),
                value = search.value,
                onValueChanged = {
                    search.value = it
                })


            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Default.Menu, "")
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                modifier = Modifier
                    .width(IntrinsicSize.Min)

            ) {
                DropdownMenuItem({ Text(text = "Settings") }, onClick = { Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show() })
                DropdownMenuItem({ Text(text = "Settings") }, onClick = { Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show() })
            }


        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    OptionMenu()
}