package com.example.smartfarming.ui.addactivities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.smartfarming.R
import com.example.smartfarming.ui.addactivities.ui.theme.SmartFarmingTheme
import com.example.smartfarming.ui.addactivities.viewModel.AddActivitiesViewModel
import com.example.smartfarming.ui.authentication.ui.theme.RedFertilizer
import com.example.smartfarming.ui.authentication.ui.theme.YellowPesticide
import com.example.smartfarming.ui.authentication.ui.theme.BlueWatering
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.example.smartfarming.FarmApplication
import com.example.smartfarming.data.room.entities.ActivityTypesEnum
import com.example.smartfarming.ui.addactivities.viewModel.AddActivitiesViewModelFactory
import com.example.smartfarming.ui.addactivity.AddActivity
import com.example.smartfarming.ui.common_composables.GardenSpinner


class AddActivities : ComponentActivity() {

    private val viewModel : AddActivitiesViewModel by viewModels{
        AddActivitiesViewModelFactory((application as FarmApplication).repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartFarmingTheme {
                val navController = rememberNavController()
                //SetupNavGraph(navController = navController, viewModel)
                AddActivity(navController = navController, viewModel)
            }
        }
    }
}

@Composable
fun HostComposable(viewModel : AddActivitiesViewModel){

}

@Composable
fun AddActivitiesMain(navController : NavHostController, viewModel : AddActivitiesViewModel){

    val gardensList = viewModel.getGardens().observeAsState()
    val gardensNameList = arrayListOf<String>()

    if (gardensList.value != null){
        for (garden in gardensList.value!!){
            gardensNameList.add(garden.name)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Cards(
            navController,
            gardensNameList
        )
    }
}



@Composable
fun Cards(
    navController: NavHostController,
    garensList : List<String>
){

    var currentGarden by remember {
        if (garensList.size != 0){
            mutableStateOf(garensList[0])
        }
        else {
            mutableStateOf("???????????? ??????")
        }
    }

    val context = LocalContext.current
    Toast.makeText(context, "fff", Toast.LENGTH_SHORT).show()


    var clicked = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 50.dp)
    ) {
        Text(
            text = "???????????? ???????????? ????????",
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(35.dp)
            ,
            color = MaterialTheme.colors.primary
        )
        Row(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Card("????????????", R.drawable.watering, BlueWatering, clicked) {
                clicked.value = !clicked.value
                navController.navigate("${ScreensEnumActivities.Irrigation.name}/$currentGarden")
            }
            Card("??????????", R.drawable.npk, RedFertilizer, clicked) {
                clicked.value = !clicked.value
                navController.navigate("${ActivityTypesEnum.FERTILIZATION.name}/${currentGarden}")
            }
        }
        Row(
            modifier = Modifier
                .padding(2.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Card("?????????? ????????", R.drawable.pesticide1, YellowPesticide,clicked ){
                clicked.value = !clicked.value
                navigateToScreens(navController, currentGarden, ScreensEnumActivities.Pesticide.name)
            }
            Card("????????", R.drawable.shovel, MaterialTheme.colors.primary, clicked) {
                clicked.value = !clicked.value
                navigateToScreens(navController, currentGarden, ScreensEnumActivities.Pesticide.name)
            }
        }
        Row(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.CenterHorizontally)
        ){
            Column() {
                Text(
                    text = "?????? ???????? ?????? ???? ???????????? ????????",
                    modifier = Modifier
                        .padding(2.dp)
                        .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.body1
                    )
                GardenSpinner(garensList, currentGarden){
                    currentGarden = it
                }

            }
        }
    }
}


@Composable
fun Card(
    text : String,
    iconId : Int,
    color: Color,
    clicked: MutableState<Boolean>,
    action : () -> Unit
){
    val cardSize by animateDpAsState(
        if (clicked.value) 0.dp else 160.dp
    )

    Column(
        modifier = Modifier
            .padding(10.dp)
            .size(cardSize)
            .clip(shape = MaterialTheme.shapes.large)
            .shadow(3.dp)
            .background(color)
            .clickable { action() }
            .padding(30.dp)
        

        ,
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "Icon",
            tint = Color.White,
            modifier = Modifier
                .padding(5.dp)
                .size(55.dp)
                .align(Alignment.CenterHorizontally)

        )
        Text(
            text = text,
            color = Color(0xFFFFFFFF),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 5.dp),
            style = MaterialTheme.typography.body2
        )
    }
}



// Navigation
private fun navigateToScreens(
    navController: NavHostController,
    gardenName: String,
    activityName: String
){
    navController.navigate("$activityName/$gardenName")
}

