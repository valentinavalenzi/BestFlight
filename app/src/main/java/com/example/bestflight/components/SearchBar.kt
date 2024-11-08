package com.example.bestflight.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.bestflight.R
import com.example.bestflight.ui.theme.Black
import com.example.bestflight.ui.theme.BlueGrey40
import com.example.bestflight.ui.theme.BlueGrey80
import com.example.bestflight.ui.theme.White
import com.example.bestflight.ui.theme.size10dp
import com.example.bestflight.ui.theme.size16dp
import com.example.bestflight.ui.theme.size18dp
import com.example.bestflight.ui.theme.size18sp
import com.example.bestflight.ui.theme.size24dp
import com.example.bestflight.ui.theme.size48dp
import com.example.bestflight.ui.theme.size8dp

@Composable
fun SearchBar(
    onChange: (String) -> Unit,
    onClear: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                White,
                shape = RoundedCornerShape(size10dp)
            )
            .padding(horizontal = size16dp)
            .height(size48dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Search Icon",
            tint = BlueGrey40,
            modifier = Modifier
                .size(size24dp)
                .align(Alignment.CenterVertically)

        )
        Spacer(modifier = Modifier.width(size8dp))

        BasicTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                onChange(it)
            },
            modifier = Modifier
                .weight(1f)
                .padding(end = size8dp)
                .align(Alignment.CenterVertically),
            singleLine = true,
            textStyle = TextStyle(fontSize = size18sp, color = Black),
            decorationBox = { innerTextField ->
                if (searchText.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.search_bar),
                        style = TextStyle(fontSize = size18sp, color = BlueGrey80)
                    )
                }
                innerTextField()
            }
        )
        if (searchText.isNotEmpty()) {
            IconButton(onClick = {
                searchText = ""
                onClear()
            }, modifier = Modifier.padding(0.dp)) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Clear Search",
                    tint = BlueGrey80,
                    modifier = Modifier
                        .size(size18dp)
                        .padding(0.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar(onChange = {}) {
    }
}