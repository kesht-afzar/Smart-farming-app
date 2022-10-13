package com.example.smartfarming.ui.addactivity.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.smartfarming.R
import com.example.smartfarming.ui.addactivities.ui.theme.BlueIrrigationDark

@Composable
fun PicturedCard(
    title : String,
    @DrawableRes picId : Int,
    color: Color,
    goTo : () -> Unit
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .clickable { goTo() }
        .padding(10.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = 3.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            Image(painter = painterResource(id = picId), contentDescription = null, modifier = Modifier.matchParentSize(), contentScale = ContentScale.FillWidth)
        }
        
        Box(
            Modifier
                .fillMaxSize()
                .background(color.copy(.5f))) {

        }

        Row(
            Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            Text(text = title, color = Color.White, style = MaterialTheme.typography.h5)
        }
    }
}