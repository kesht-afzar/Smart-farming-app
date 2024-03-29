package com.example.smartfarming.ui.gardens.composables

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Beenhere
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.smartfarming.R
import com.example.smartfarming.data.room.entities.ActivityTypesEnum
import com.example.smartfarming.data.room.entities.Garden
import com.example.smartfarming.data.room.entities.Task
import com.example.smartfarming.data.room.entities.enums.TaskStatusEnum
import com.example.smartfarming.ui.AppScreensEnum
import com.example.smartfarming.ui.addactivities.ui.theme.*
import com.example.smartfarming.ui.authentication.ui.theme.RedFertilizer
import com.example.smartfarming.ui.authentication.ui.theme.YellowPesticide
import com.example.smartfarming.ui.gardenprofile.GardenProfileViewModel
import com.example.smartfarming.ui.gardenprofile.composables.ReportDiagram
import com.example.smartfarming.ui.gardenprofile.composables.ToDos
import com.example.smartfarming.ui.home.composables.MyFAB
import com.example.smartfarming.ui.home.composables.TaskCard2
import com.example.smartfarming.utils.getTaskList

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GardenProfile(garden : State<Garden?>, navController: NavHostController, viewModel: GardenProfileViewModel){
    val context = LocalContext.current
    var fabExtended by remember{
        mutableStateOf(false)
    }

    viewModel.getAllGardens()

    var tasks = listOf<Task>()

    LaunchedEffect(key1 = viewModel.load.value){
        viewModel.load.value = false
    }

    if (viewModel.garden.value != null){
        viewModel.calculatePercentage()
    }

    Scaffold(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize(1f),
        floatingActionButton = {
            MyFAB(context = context, fabExtended = fabExtended) {
                fabExtended = !fabExtended
            }
        },
        backgroundColor = LightBackground
    ) {
        if (viewModel.garden.value != null){
            viewModel.getGardenTasks()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .scrollable(rememberScrollState(), Orientation.Vertical)
                    .background(LightGray2),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GardenTitle(gardenName = garden.value!!.title, navController, viewModel)
                Column(
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())) {
                    ReportDiagram(
                        irrigationProgress = viewModel.irrigationPercentage.value,

                    )

                    Report(navController, garden.value!!.title)

                    Divider(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 25.dp), thickness = 1.dp, color = Color.Gray.copy(.3f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        MainIcons(Icons.Default.Thermostat, "آب و هوا", Blue700){
                            navController.navigate(route = "${AppScreensEnum.GardenWeatherScreen.name}/${garden.value!!.title}")
                        }

                        MainIcons(Icons.Outlined.Inventory, "محصولات", YellowPesticide){
                            navController.navigate("${AppScreensEnum.GardenHarvestScreen.name}/${garden.value!!.title}")
                        }

                        MainIcons(Icons.Outlined.LocationOn, "مکان نما", RedFertilizer){
                            navController.navigate("${AppScreensEnum.GardenMapScreen.name}/${garden.value!!.title}")
                        }

                    }

                    val thisGardenTask = ArrayList<Task>()
                    for (task in tasks){
                        if (task.gardenIds.contains(garden.value!!.id)){
                            thisGardenTask.add(task)
                        }
                    }

                    //List of tasks
                    Column(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Divider(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp), thickness = 1.dp, color = Color.Gray.copy(.3f))

                        Column {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 30.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Icon(Icons.Default.Add,
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.primary,
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clickable {
                                            navController.navigate("${AppScreensEnum.AddTaskScreen.name}/${garden.value}")
                                        }
                                )

                                Row(
                                    modifier = Modifier
                                        .clickable { navController.navigate(route = AppScreensEnum.GardenTasksScreen.name) },
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "یادآور فعالیت ها",
                                        style = MaterialTheme.typography.body1,
                                        color = Color.Black,
                                        modifier = Modifier.padding(5.dp)
                                    )

                                    Icon(Icons.Default.Alarm, contentDescription = null, modifier = Modifier
                                        .size(40.dp)
                                        .padding(5.dp), tint = Color.Black)
                                }
                            }
                            TasksFilter(viewModel)
                        }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding =  PaddingValues(10.dp),
                            modifier = Modifier
                                .height(550.dp)
                                .padding(horizontal = 12.dp),
                        ){
                            items(viewModel.gardenTasks){ item ->
                                TaskCard2(
                                    task = item,
                                    navController = navController,
                                    setTaskStatus = {stat -> viewModel.updateTaskStatus(item.id, stat)},
                                    deleteTask = {viewModel.deleteTask(it)}) {
                                }
                            }
                        }
                    }

    //                LazyColumn(
    //                    modifier = Modifier.height(400.dp)
    //                ){
    //                    items(thisGardenTask){ task ->
    //                        ToDos(task = task, navController = navController)
    //                    }
    //                }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun Report(navController: NavHostController, gardenName: String){
    Card(
        modifier = Modifier.padding(horizontal = 30.dp, vertical = 8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(20.dp),
        backgroundColor = LightGray
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .clickable {
                navController.navigate(route = "${AppScreensEnum.GardenReportScreen.name}/${gardenName}")
            }
            .padding(20.dp),
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
                text = "گزارش فعالیت ها",
                style = MaterialTheme.typography.body1,
                color = MainGreen
            )
        }
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
            .padding(horizontal = 20.dp)
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
private fun GardenTitle(gardenName : String, navController: NavHostController, viewModel : GardenProfileViewModel){
    var clicked by remember {
        mutableStateOf(false)
    }
    val cardHeight by animateDpAsState( if (clicked) 350.dp else 75.dp, animationSpec = tween(durationMillis = 1000))
    val cardRadius by animateDpAsState(if (clicked) 25.dp else 40.dp, animationSpec = tween(durationMillis = 1000))

    Box(
        Modifier
            .padding(bottom = 55.dp, start = 0.dp, end = 0.dp)
            .fillMaxWidth()
            .height(140.dp)
            .background(MainGreen)
            .padding(0.dp)
            .zIndex(100f)){

        Image(painterResource(id = R.drawable.background_pic), contentDescription = null, modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth)

        Box(modifier = Modifier
            .fillMaxSize()
            .background(MainGreen.copy(.4f)))

        Card( modifier = Modifier
            .offset(y = 105.dp)
            .padding(horizontal = 15.dp)
            .clickable { clicked = !clicked }
            .fillMaxWidth()
            .height(cardHeight),
            shape = RoundedCornerShape(cardRadius),
            elevation = 4.dp
        ) {
            if (!clicked){
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(horizontal = 30.dp, vertical = 5.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "", tint = MainGreen, modifier = Modifier
                        .clickable {
                            navController.navigate(route = "${AppScreensEnum.GardenEditScreen.name}/$gardenName")
                        }
                        .padding(5.dp))
                    Text(text = gardenName, style = MaterialTheme.typography.h5, color = MainGreen, modifier = Modifier.padding(5.dp))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 5.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.End){
                    items(viewModel.allGardensName){gardenName ->
                        Text(text = gardenName, style = MaterialTheme.typography.h5, modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.setCurrentGarden(gardenName)
                                clicked = !clicked
                            }
                            .padding(5.dp), color = MaterialTheme.colors.primary)
                    }
                }
            }
        }
    }
}

@Composable
private fun TasksFilter(viewModel: GardenProfileViewModel) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Surface(modifier = Modifier
            .padding(3.dp)
            .clickable {
                viewModel.setSelectedTaskStatus(TaskStatusEnum.TODO.name)
                       },
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, Yellow500)
        ) {
            Row(Modifier
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.Center) {
                AnimatedVisibility(visible = viewModel.selectedTaskStatus.value == TaskStatusEnum.TODO.name) {
                    Text(text = "در انتظار انجام", style = MaterialTheme.typography.body2, color = Yellow700, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 12.dp))
                }
                Icon(Icons.Default.HourglassTop, tint = Yellow500, contentDescription = null, modifier = Modifier.padding(vertical = 8.dp, horizontal = 13.dp))
            }
        }

        Surface(modifier = Modifier
            .padding(horizontal = 3.dp)
            .clickable {
                viewModel.setSelectedTaskStatus(TaskStatusEnum.DONE.name)

                       },
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, MainGreen)
        ) {
            Row(
                Modifier
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.Center) {

                AnimatedVisibility(visible = viewModel.selectedTaskStatus.value == TaskStatusEnum.DONE.name) {
                    Text(text = "انجام شده", style = MaterialTheme.typography.body2, color = MainGreen,modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 12.dp))
                }

                Icon(Icons.Default.Done, contentDescription = null, tint = MainGreen, modifier = Modifier.padding(vertical = 8.dp, horizontal = 13.dp))
            }
        }

        Surface(modifier = Modifier
            .padding(3.dp)
            .clickable {
                viewModel.setSelectedTaskStatus(TaskStatusEnum.IGNORED.name)
                       },
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, Color.Red)
        ) {
            Row(
                Modifier.padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.Center) {

                AnimatedVisibility(visible = viewModel.selectedTaskStatus.value == TaskStatusEnum.IGNORED.name) {
                    Text(text = "منقضی", style = MaterialTheme.typography.body2, color = Color.Red,modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 12.dp))
                }

                Icon(Icons.Default.Close, contentDescription = null, tint = Color.Red,modifier = Modifier.padding(vertical = 8.dp, horizontal = 13.dp))
            }
        }
    }
}
