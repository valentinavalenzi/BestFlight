package com.example.bestflight.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bestflight.R
import com.example.bestflight.data.Card
import com.example.bestflight.navigation.BestFlightScreen
import com.example.bestflight.ui.theme.DeepBlue
import com.example.bestflight.ui.theme.Red
import com.example.bestflight.ui.theme.White
import com.example.bestflight.ui.theme.largeText
import com.example.bestflight.ui.theme.size10dp
import com.example.bestflight.ui.theme.size16dp
import com.example.bestflight.ui.theme.size24dp
import com.example.bestflight.ui.theme.size48dp
import com.example.bestflight.ui.theme.size50sp
import com.example.bestflight.ui.theme.superLargeText
import androidx.compose.foundation.lazy.items
import com.example.bestflight.ui.theme.size20dp


@Composable
fun Account(navController: NavController) {
    val viewModel = hiltViewModel<AccountViewModel>()
    val userName by viewModel.userName.collectAsState()
    val cardsList by viewModel.cardsList.collectAsState(initial = listOf())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = userName,
            fontSize = superLargeText,
            color = White,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                lineHeight = size50sp,
                fontFamily = FontFamily.SansSerif
            ),
            modifier = Modifier.padding(top = size24dp)
        )
        Spacer(modifier = Modifier.height(size48dp))

        Text(
            text = stringResource(id = R.string.payment_methods),
            fontSize = largeText,
            color = White,
        )
        Spacer(modifier = Modifier.height(size48dp))

        if (cardsList.isEmpty()) {
            Text(text = stringResource(id = R.string.no_cards), color = White)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.5f),
                verticalArrangement = Arrangement.spacedBy(size10dp)
            ) {
                items(cardsList) { card ->
                    CardInfo(
                        card = card,
                        onDelete = { viewModel.deleteCard(card) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(size20dp))

        Button(
            onClick = {
                navController.navigate(BestFlightScreen.AddCard.name)
            },
            modifier = Modifier.padding(top = size24dp)
        ) {
            Text(text = stringResource(id = R.string.add_payment_method), color = White)
        }

        Spacer(modifier = Modifier.height(size20dp))
    }
}

@Preview
@Composable
fun AccountPreview() {
    val mockNavController = rememberNavController()
    Account(navController = mockNavController)
}

@Composable
fun CardInfo(card: Card, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = size10dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(
            modifier = Modifier
                .padding(size16dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = card.cardType,
                    fontSize = largeText,
                    color = DeepBlue
                )
                Text(
                    text = stringResource(id = R.string.ending_in) + " ${card.cardNumber.takeLast(4)}",
                    color = DeepBlue
                )
            }
            IconButton(onClick = { onDelete() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete Card",
                    tint = Red
                )
            }
        }
    }
}