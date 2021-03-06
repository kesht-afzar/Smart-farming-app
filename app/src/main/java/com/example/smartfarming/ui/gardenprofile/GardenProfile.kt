package com.example.smartfarming.ui.gardens.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Reviews
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.smartfarming.R
import com.example.smartfarming.data.room.entities.ActivityTypesEnum
import com.example.smartfarming.data.room.entities.Garden
import com.example.smartfarming.data.room.entities.Task
import com.example.smartfarming.ui.AppScreensEnum
import com.example.smartfarming.ui.addactivities.ui.theme.*
import com.example.smartfarming.ui.authentication.ui.theme.RedFertilizer
import com.example.smartfarming.ui.authentication.ui.theme.YellowPesticide
import com.example.smartfarming.ui.gardenprofile.composables.ReportDiagram
import com.example.smartfarming.ui.gardenprofile.composables.ToDos
import com.example.smartfarming.ui.home.composables.MyFAB

@Composable
fun GardenProfile(garden : State<Garden?>, navController: NavHostController){
    val context = LocalContext.current
    var fabExtended by remember{
        mutableStateOf(false)
    }

    val tasks = listOf<Task>(
        Task(0,
            "?????? ????????",
            activity_type = ActivityTypesEnum.FERTILIZATION.name,
            description = "???? ???????? ?????? ?????????? ???????? ????????????",
            start_date = "",
            finish_date = "",
            garden_name = "????????",
            recommendations = "???????? ??????",
            user_id = 5,
            seen = false
        ),
        Task(0,
            "???? ????????",
            activity_type = ActivityTypesEnum.PESTICIDE.name,
            description = "???????????? ???? ????????",
            start_date = "",
            finish_date = "",
            garden_name = "????????",
            recommendations = "???????? ??????",
            user_id = 5,
            seen = false
        )
        ,
        Task(0,
            "???????????? ??????????",
            activity_type = ActivityTypesEnum.IRRIGATION.name,
            description = "???????? ???????????? ??????????",
            start_date = "",
            finish_date = "",
            garden_name = "????????",
            recommendations = "",
            user_id = 5,
            seen = false
        )
        ,
        Task(0,
            "?????? ????????",
            activity_type = ActivityTypesEnum.FERTILIZATION.name,
            description = "???? ???????? ???? ???????? ???????? ?????? ???????? ???? ?????????? ?????? ????????",
            start_date = "",
            finish_date = "",
            garden_name = "??????????",
            recommendations = "?????? ??????",
            user_id = 5,
            seen = false
        )
    )


    Scaffold(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize(1f),
        floatingActionButton = {
            MyFAB(context = context, fabExtended = fabExtended) {
                fabExtended = !fabExtended
            }
        }

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            GardenTitle(gardenName = garden.value!!.name)
            ReportDiagram()
            Report()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                MainIcons(Icons.Default.Thermostat, "???? ?? ??????", waterBlueDark){
                    navController.navigate(route = "${AppScreensEnum.GardenWeatherScreen.name}/${garden.value!!.name}")
                }
                MainIcons(Icons.Outlined.Inventory, "??????????????", YellowPesticide){
                    navController.navigate("${AppScreensEnum.GardenHarvestScreen.name}/${garden.value!!.name}")
                }
                MainIcons(Icons.Outlined.LocationOn, "???????? ??????", RedFertilizer){

                }

            }

            val thisGardenTask = ArrayList<Task>()
            for (task in tasks){
                if (task.garden_name == garden.value!!.name){
                    thisGardenTask.add(task)
                }
            }

            LazyColumn{
                items(thisGardenTask){ task ->
                    ToDos(task = task, navController = navController)
                }
            }
        }
    }
}



@Composable
fun Report(){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(30.dp)
        .graphicsLayer {
            shadowElevation = 4.dp.toPx()
            shape = RoundedCornerShape(20.dp)
            clip = true
        }
        .background(LightGray)
        .clip(MaterialTheme.shapes.large)
        .clickable { }
        .padding(20.dp)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painterResource(id = R.drawable.bar_chart),
            contentDescription = "",
            tint = MainGreen,
            modifier = Modifier
                .padding(8.dp)
                .size(55.dp)
        )
        Text(
            text = "?????????? ???????????? ????",
            style = MaterialTheme.typography.body2,
            color = MainGreen
        )

    }
}





@Composable
fun MainIcons(
    icon : ImageVector,
    text : String,
    color: Color ,
    clickHandler : () -> Unit
){

    Column(
        modifier = Modifier
            .padding(18.dp)
            .clickable { clickHandler() }
            .padding(4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = "",
            modifier = Modifier.size(40.dp),
            tint = color
        )

        Text(text = text, style = MaterialTheme.typography.subtitle1, color = BorderGray)
    }
}

@Composable
fun GardenTitle(gardenName : String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Edit, contentDescription = "", tint = MainGreen, modifier = Modifier
            .clickable { }
            .padding(5.dp))
        Text(text = gardenName, style = MaterialTheme.typography.h3, color = MainGreen, modifier = Modifier.padding(5.dp))

    }
}

@Composable
fun Indicators(){

}

