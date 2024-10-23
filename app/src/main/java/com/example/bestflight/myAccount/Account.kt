package com.example.bestflight.myAccount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bestflight.R
import com.example.bestflight.ui.theme.largeText
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.bestflight.ui.theme.DarkBlue
import com.example.bestflight.ui.theme.superLargeText

@Composable
fun Account() {

    val viewModel = hiltViewModel<AccountViewModel>()
    val userName by viewModel.userName.collectAsState()

    AccountView(username = userName)
}

@Composable
fun AccountView(username: String) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.profile),
            fontSize = superLargeText,
            color = DarkBlue,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                lineHeight = 50.sp,
                fontFamily = FontFamily.SansSerif
            ),
            modifier = Modifier.padding(top = 24.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = username,
            fontSize = largeText,
            color = DarkBlue,
            style = TextStyle(
                lineHeight = 50.sp,
                fontFamily = FontFamily.SansSerif
            )
        )
    }
}

@Preview
@Composable
fun AccountPreview() {
    AccountView("Valentina Isabella Valenzi")
}