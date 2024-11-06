package com.example.bestflight.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bestflight.R
import com.example.bestflight.home.FlightModel
import com.example.bestflight.ui.theme.DarkBlue
import com.example.bestflight.ui.theme.White

@Composable
fun FlightCard(
    flight: FlightModel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(White, shape = RoundedCornerShape(10.dp))
            .padding(16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // From -> To and Duration
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = flight.from,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(id = R.string.arrow),
                    fontSize = 16.sp,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = flight.to,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.duration) + flight.flight_duration,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // Destination Image
        Box(
            modifier = Modifier
                .size(70.dp)
                .padding(2.dp)
        ) {
            AsyncImage(
                model = flight.destination_img,
                contentDescription = "Destination Image",
                modifier = Modifier
                    .size(500.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        // Flight Price
        Text(
            text = stringResource(id = R.string.money_sign) + flight.price,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )
    }
}

@Preview
@Composable
fun FlightCardPreview() {
    val flight = FlightModel(
        id = "1",
        from = "EZE",
        to = "MIA",
        from_name = "Buenos Aires",
        to_name = "Miami",
        departure_time = "10/10/24 02:30pm",
        arrival_time = "11/10/24 01:00am",
        flight_duration = "10:30hs",
        stops_number = "0",
        flight_number = "AA135",
        destination_img = "https://www.expedia.com.ar/Miami-Centro-De-Miami.dx800070",
        included_baggage = "23kg",
        price = "1563.21"
    )
    FlightCard(flight, onClick = {})
}
