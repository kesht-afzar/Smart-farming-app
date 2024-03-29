package com.example.smartfarming.ui.gardens.composables

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Compost
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.smartfarming.R
import com.example.smartfarming.data.room.entities.ActivityTypesEnum
import com.example.smartfarming.data.room.entities.Garden
import com.example.smartfarming.data.room.entities.Task
import com.example.smartfarming.data.room.entities.enums.TaskStatusEnum
import com.example.smartfarming.ui.addactivities.ui.theme.*
import com.example.smartfarming.ui.authentication.ui.theme.YellowPesticide
import com.example.smartfarming.ui.gardenprofile.GardenProfileActivity
import com.example.smartfarming.ui.gardens.GardensViewModel

@Composable
fun GardenCard(garden : Garden, viewModel : GardensViewModel){
    val context = LocalContext.current
    var hasIrrigation = remember {
        mutableStateOf(false)
    }
    var hasFertilization = remember {
        mutableStateOf(false)
    }
    var hasPesticide = remember {
        mutableStateOf(false)
    }
    var hasOthers = remember {
        mutableStateOf(false)
    }
    //setNotificationIcons(viewModel)

    val gardenTasks = getGardenTasks(garden.id, viewModel.allTasks)

    setNotificationIcons(
        gardenTasks,
        setIrrigationState = {hasIrrigation.value = it},
        setFertilizationState = {hasFertilization.value = it},
        setPesticideState = {hasPesticide.value = it},
        setOthersState = {hasOthers.value = it}
    )

    Card(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 15.dp)
            .fillMaxWidth()
            .height(110.dp)
            .clip(MaterialTheme.shapes.large)
            .clickable {
                val intent = Intent(context, GardenProfileActivity::class.java)
                intent.putExtra("gardenName", garden.title)
                context.startActivity(intent)
            },
        shape = MaterialTheme.shapes.large,
        elevation = 3.dp,
    ) {
        Row() {
            // Profile pic column
            Column(
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
                    .background(MainGreen),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(painter = painterResource(id = R.drawable.sprout_white),
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp),
                    tint = Color.White
                )
            }

            // Info column
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(LightGreen3),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = garden.title,
                    style = MaterialTheme.typography.h5,
                    color = MainGreen,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row() {
                    if (hasIrrigation.value){
                        ActivityBadge(color = BlueIrrigationDark, icon = Icons.Default.WaterDrop)
                    }
                    if (hasFertilization.value){
                        ActivityBadge(color = Purple500, icon = Icons.Default.Compost)
                    }
                    if (hasPesticide.value){
                        ActivityBadge(color = YellowPesticide, icon = Icons.Default.BugReport)
                    }
                    if (hasOthers.value){
                        ActivityBadge(color = MainGreen, icon = Icons.Default.Agriculture)
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityBadge(color: Color, icon : ImageVector){
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clip(CircleShape)
            .background(color)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White)
    }
}

fun drawTicketPath(size: Size, cornerRadius: Float): Path {
    return Path().apply {
        reset()
        // Top left arc
        arcTo(
            rect = Rect(
                left = -cornerRadius,
                top = -cornerRadius,
                right = cornerRadius,
                bottom = cornerRadius
            ),
            startAngleDegrees = 90.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = size.width - cornerRadius, y = 0f)
        // Top right arc
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius,
                top = -cornerRadius,
                right = size.width + cornerRadius,
                bottom = cornerRadius
            ),
            startAngleDegrees = 180.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = size.width.toFloat(), y = size.height - cornerRadius)
        // Bottom right arc
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius,
                top = size.height - cornerRadius,
                right = size.width + cornerRadius,
                bottom = size.height + cornerRadius
            ),
            startAngleDegrees = 270.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = cornerRadius, y = size.height.toFloat())
        // Bottom left arc
        arcTo(
            rect = Rect(
                left = -cornerRadius,
                top = size.height - cornerRadius,
                right = cornerRadius,
                bottom = size.height + cornerRadius
            ),
            startAngleDegrees = 0.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = 0f, y = cornerRadius)
        close()
    }
}

class TicketShape(private val cornerRadius: Float) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            // Draw your custom path here
            path = drawTicketPath(size = size, cornerRadius = cornerRadius)
        )
    }
}

private fun setNotificationIcons(
    gardenTasks : MutableList<Task>,
    setIrrigationState : (Boolean) -> Unit,
    setPesticideState : (Boolean) -> Unit,
    setFertilizationState : (Boolean) -> Unit,
    setOthersState : (Boolean) -> Unit,
){
    gardenTasks.forEach {
        when(it.activityType){
            ActivityTypesEnum.IRRIGATION.name -> setIrrigationState(true)
            ActivityTypesEnum.FERTILIZATION.name -> setPesticideState(true)
            ActivityTypesEnum.PESTICIDE.name -> setFertilizationState(true)
            ActivityTypesEnum.Other.name -> setOthersState(true)
        }
    }
}

private fun getGardenTasks(gardenId : Int ,allTasks: List<Task>) : MutableList<Task>{
    val gardenTasks = mutableListOf<Task>()

    for (task in allTasks){
        if (task.gardenIds.contains(gardenId) && task.status == TaskStatusEnum.TODO.name){
            gardenTasks.add(task)
        }
    }
    return gardenTasks
}