package com.example.heybooks.components


import androidx.compose.foundation.text.ClickableText

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.heybooks.navigation.MainActions

@Composable
fun ClickTextLogin(actions: MainActions) {
    val textOne = "Bạn đã có tài khoản ? "
    val textTwo = "Đăng nhập"
    val textString = buildAnnotatedString {
        append(textOne)
        withStyle(style = SpanStyle(color = Color.Black)) {
            pushStringAnnotation(tag = textTwo, annotation = textTwo)
            append(textTwo)
        }
    }
    ClickableText(text = textString, onClick = {
        actions.gotoLogin()
//        openSignup()
    })
}

@Composable
fun ClickTextSignUp(actions: MainActions) {
    val textOne = "Bạn chưa có tài khoản ? "
    val textTwo = "Đăng ký"
    val textString = buildAnnotatedString {
        append(textOne)
        withStyle(style = SpanStyle(color = Color.Black)) {
            pushStringAnnotation(tag = textTwo, annotation = textTwo)
            append(textTwo)
        }
    }
    ClickableText(text = textString, onClick = {
        actions.gotoSignUp()
//        openSignup()
    })
}