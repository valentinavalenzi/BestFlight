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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.bestflight.R

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
                Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 16.dp)
            .height(48.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Search Icon",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp).align(Alignment.CenterVertically)

        )
        Spacer(modifier = Modifier.width(8.dp))

        BasicTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                onChange(it)
            },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically),
            singleLine = true,
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
            decorationBox = { innerTextField ->
                if (searchText.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.search_bar),
                        style = TextStyle(fontSize = 18.sp, color = Color.Gray)
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
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(18.dp)
                        .padding(0.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}
