package com.example.bestflight.myAccount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bestflight.R
import com.example.bestflight.ui.theme.largeText
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview

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
            .fillMaxSize()
            .padding(top = 62.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = username,
            fontSize = largeText,
            color = colorResource(id = R.color.black)
        )
    }
}

@Preview
@Composable
fun AccountPreview() {
    AccountView("Valentina Isabella Valenzi")
}