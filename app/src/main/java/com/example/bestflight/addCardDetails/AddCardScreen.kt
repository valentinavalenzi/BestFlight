package com.example.bestflight.addCardDetails

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.bestflight.R
import com.example.bestflight.ui.theme.White
import com.example.bestflight.ui.theme.largeText
import com.example.bestflight.ui.theme.size16dp
import com.example.bestflight.ui.theme.size24dp
import com.example.bestflight.ui.theme.size48dp
import com.example.bestflight.ui.theme.size50sp
import com.example.bestflight.ui.theme.superLargeText
import androidx.compose.ui.text.input.TextFieldValue
import com.example.bestflight.ui.theme.size36dp
import com.example.bestflight.ui.theme.size8dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bestflight.account.AccountViewModel
import com.example.bestflight.navigation.BestFlightScreen
import com.example.bestflight.ui.theme.MidBlue
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

enum class CardType {
    VISA, MASTERCARD, AMEX, CREDIT, DEBIT, UNKNOWN
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddCardScreen(navController: NavController) {
    val viewModel = hiltViewModel<AccountViewModel>()

    val context = androidx.compose.ui.platform.LocalContext.current
    val initialExpDate = stringResource(id = R.string.mm_yy)

    var cardNumber by remember { mutableStateOf("") }
    var cardType by remember { mutableStateOf<CardType?>(null) }
    var expDate by remember { mutableStateOf(TextFieldValue(text = initialExpDate)) }
    var cvv by remember { mutableStateOf("") }

    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedCardType by remember { mutableStateOf<CardType?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(size16dp)
    ) {
        Text(
            text = stringResource(id = R.string.add_card),
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

        // Card Number
        Text(text = stringResource(id = R.string.card_number), fontSize = largeText, color = White, modifier = Modifier.padding(bottom = size16dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = cardNumber,
                onValueChange = {
                    if (it.length <= 16) {
                        cardNumber = it
                        cardType = detectCardType(it)
                    }
                },
                placeholder = { Text(text = stringResource(id = R.string.card_number_example)) },
                modifier = Modifier
                    .fillMaxWidth(),
                trailingIcon = {
                    if (cardType != null) {
                        when (cardType) {
                            CardType.VISA -> CardLogo(R.drawable.visa_logo)
                            CardType.MASTERCARD -> CardLogo(R.drawable.mastercard_logo)
                            CardType.AMEX -> CardLogo(R.drawable.amex_logo)
                            else -> {
                                CardLogo(logoRes = R.drawable.card)}
                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(size16dp))

        if (cardType == CardType.UNKNOWN) {
            Text(
                text = stringResource(id = R.string.select_card_type),
                fontSize = largeText,
                color = White,
                modifier = Modifier.padding(bottom = size16dp)
            )
            Box {
                Button(onClick = { isDropdownExpanded = !isDropdownExpanded }) {
                    Text(text = selectedCardType?.name ?: stringResource(id = R.string.choose))
                }
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    CardType.entries.filter { it != CardType.UNKNOWN }.forEach { type ->
                        DropdownMenuItem(
                            onClick = {
                                selectedCardType = type
                                isDropdownExpanded = false
                            },
                            text = { Text(type.name) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(size16dp))

        // Expiration Date Input
        Text(text = stringResource(id = R.string.exp_date), fontSize = largeText, color = White, modifier = Modifier.padding(bottom = size16dp))
        TextField(
            value = expDate,
            onValueChange = { newValue ->
                expDate = formatExpiryDate(newValue)
            },
            placeholder = { R.string.mm_yy },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(size16dp))

        // CVV Input
        Text(text = stringResource(id = R.string.cvv), fontSize = largeText, color = White, modifier = Modifier.padding(bottom = size16dp))
        TextField(
            value = cvv,
            onValueChange = { newValue ->
                if (newValue.length <= 4) {
                    cvv = newValue
                }
            },
            placeholder = { Text(text = stringResource(id = R.string.cvv_example)) },
            modifier = Modifier.fillMaxWidth()
        )

        // Confirm Button
        Spacer(modifier = Modifier.height(size16dp))
        Button(
            onClick = {
                val finalCardType = if (cardType != CardType.UNKNOWN) cardType else selectedCardType
                val isExpired = isCardExpired(expDate.text)
                if (cardNumber.isNotEmpty() && expDate.text.isNotEmpty() && cvv.isNotEmpty() && finalCardType != null) {
                    if (isExpired) {
                        Toast.makeText(context, R.string.card_expired, Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.addCard(cardNumber, finalCardType.toString(), expDate.text, cvv)
                        cardNumber = ""
                        expDate = TextFieldValue(text = initialExpDate)
                        cvv = ""
                        Toast.makeText(context, R.string.saved_card_details, Toast.LENGTH_SHORT).show()
                        navController.navigate(BestFlightScreen.Account.name)
                    }
                } else {
                    Toast.makeText(context, R.string.fill_in, Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = White)
        ) {
            Text(text = stringResource(id = R.string.confirm_payment), color = MidBlue)
        }
    }
}

@Composable
fun CardLogo(logoRes: Int) {
    Image(
        painter = painterResource(id = logoRes),
        contentDescription = "Card Logo",
        modifier = Modifier
            .padding(size8dp)
            .size(size36dp)
    )
}

// Card Type Detection Logic
fun detectCardType(cardNumber: String): CardType {
    return when {
        cardNumber.startsWith("4") -> CardType.VISA
        cardNumber.startsWith("5") -> CardType.MASTERCARD
        cardNumber.startsWith("3") -> CardType.AMEX
        else -> CardType.UNKNOWN
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun isCardExpired(expiryDate: String): Boolean {
    return try {
        val formatter = DateTimeFormatter.ofPattern("MM/yy")
        val expDate = LocalDate.parse("01/$expiryDate", DateTimeFormatter.ofPattern("dd/MM/yy"))
        expDate.isBefore(LocalDate.now().withDayOfMonth(1))
    } catch (e: DateTimeParseException) {
        true
    }
}

@Preview(showBackground = true)
@Composable
fun AddCardScreenPreview() {
    val mockNavController = rememberNavController()
    AddCardScreen(mockNavController)
}

fun formatExpiryDate(input: TextFieldValue): TextFieldValue {
    val digitsOnly = input.text.filter { it.isDigit() }
    val month = digitsOnly.take(2)
    val year = digitsOnly.drop(2).take(2)

    val formattedText = when {
        digitsOnly.isEmpty() -> ""
        digitsOnly.length <= 2 -> month
        else -> "$month/$year"
    }
    val cursorPosition = formattedText.length

    return TextFieldValue(
        text = formattedText,
        selection = TextRange(cursorPosition)
    )
}