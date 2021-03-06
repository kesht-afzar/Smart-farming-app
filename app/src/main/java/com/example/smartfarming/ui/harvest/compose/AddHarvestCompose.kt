package com.example.smartfarming.ui.harvest.compose

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.smartfarming.FarmApplication
import com.example.smartfarming.R
import com.example.smartfarming.data.network.resources.weather_response.Current
import com.example.smartfarming.data.room.entities.Harvest
import com.example.smartfarming.ui.AppScreensEnum
import com.example.smartfarming.ui.addactivities.ui.theme.*
import com.example.smartfarming.ui.common_composables.GardenSpinner
import com.example.smartfarming.ui.harvest.HarvestViewModel
import com.example.smartfarming.ui.harvest.HarvestViewModelFactory


@Composable
fun AddHarvestCompose(
    viewModel: HarvestViewModel,
    navController: NavHostController
){

    val gardenList = viewModel.getGardens().observeAsState()
    val gardenNameList = arrayListOf<String>()

    if (gardenList.value != null){
        for (g in gardenList.value!!){
            gardenNameList.add(g.name)
        }
    }

    val harvestDate = viewModel.harvestDate
    val harvestWeight = viewModel.harvestWeight.observeAsState()
    val selectedGarden by viewModel.selectedGarden.observeAsState()
    val harvestType by viewModel.harvestType.observeAsState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray2)
    ) {
        val (pic, body) = createRefs()
        Icon(
            painterResource(id = R.drawable.pistachio),
            contentDescription = null,
            tint = MainGreen,
            modifier = Modifier
                .constrainAs(pic) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .padding(10.dp)
                .size(100.dp)
        )
            AddHarvestBody(
                modifier = Modifier
                    .constrainAs(body) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(20.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp))
                    .border(2.dp, MainGreen, RoundedCornerShape(25.dp))
                    .background(Color.White)
                    .padding(20.dp),
                gardenNameList.ifEmpty { listOf("???????????? ??????") },
                harvestDate = harvestDate,
                navController,
                viewModel = viewModel,
                currentGarden = selectedGarden,
                setGarden = {viewModel.selectedGarden.value = it},
                weight = harvestWeight.value,
                setWeight = {viewModel.harvestWeight.value = it},
                harvestType = harvestType,
                setHarvestType = {viewModel.harvestType.value = it}
            )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddHarvestBody(
    modifier: Modifier,
    gardenList : List<String>,
    harvestDate: MutableState<MutableMap<String, String>>,
    navController: NavHostController,
    viewModel: HarvestViewModel,
    currentGarden: String?,
    setGarden : (String) -> Unit,
    weight : Float?,
    setWeight : (Float?) -> Unit,
    harvestType : String?,
    setHarvestType: (String) -> Unit
){

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var strWeight by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "?????? ??????????",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(15.dp),
            color = MainGreen
        )

        GardenSpinner(gardensList = gardenList, currentGarden = currentGarden ){
            setGarden(it)
        }

        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            HarvestTypeSpinner(harvestType ,setHarvestType = {setHarvestType(it)})

            OutlinedTextField(
                value = strWeight,
                onValueChange = {
                  strWeight = it
                    setWeight(strWeight.toFloatOrNull())
                } ,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    focusedBorderColor = MainGreen,
                    unfocusedBorderColor = MainGreen,
                    focusedLabelColor = MainGreen,
                    unfocusedLabelColor = MainGreen,
                    trailingIconColor = MainGreen
                ),
                label = {
                    Text(text = "?????? (????????)", style = MaterialTheme.typography.subtitle1)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                shape = MaterialTheme.shapes.large,
                maxLines = 1,
                singleLine = true,
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth(1f)
                    .height(60.dp),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                },
                textStyle = MaterialTheme.typography.body2
            )



        }

        DateSelectHarvest(harvestDate = harvestDate){
            viewModel.setDate(it)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 50.dp)
        ) {

            IconButton(
                onClick = {navController.navigate(AppScreensEnum.HarvestHomeScreen.name){
                    popUpTo(AppScreensEnum.AddHarvestScreen.name){
                        inclusive = true
                    }
                } },
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MainGreen)
                    .height(61.dp)
                    .padding(3.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "",
                    tint = Color.White
                )
            }


            Button(
                onClick = {
                    if (weight == null || harvestType.isNullOrEmpty() || harvestDate.value.isEmpty()){
                        Toast.makeText(context, "???????? ?????????????? ???? ???????? ????????", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.addHarvest2DB(
                            Harvest(
                                0,
                                weight,
                                harvestType,
                                year = harvestDate.value["year"]!!,
                                month = harvestDate.value["month"]!!,
                                day = harvestDate.value["day"]!!,
                                gardenName = currentGarden!!
                            )
                        )
                        Toast.makeText(context, "?????????????? ?????? ???? :)", Toast.LENGTH_SHORT).show()
                        navController.navigate(route = AppScreensEnum.HarvestHomeScreen.name)
                    }
                          },
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    backgroundColor = MainGreen
                ),
                modifier = Modifier
                    .width(210.dp)
                    .height(65.dp)
                    .padding(vertical = 2.dp)
            ) {
                Text(text = "??????", style = MaterialTheme.typography.body2)
            }
        }
    }
}

fun submitClickHandler(insertHarvest: (Harvest) -> Unit) {
}


@Composable
fun HarvestTypeSpinner(
    harvestType: String?,
    setHarvestType : (String) -> Unit
){

    val pistachiosTypeList = stringArrayResource(id = R.array.pistachios_type)


    var expanded by remember {
        mutableStateOf(false)
    }
    
    Row(modifier = Modifier
        .padding(end = 8.dp)
        .fillMaxWidth(0.4f)
        .height(53.dp)
        .clip(MaterialTheme.shapes.large)
        .clickable { expanded = !expanded }
        .border(2.dp, color = MainGreen, shape = MaterialTheme.shapes.large)
        .padding(end = 1.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (harvestType.isNullOrBlank()) pistachiosTypeList[0] else harvestType,
            style = MaterialTheme.typography.body1)
    }
    
    DropdownMenu(
        expanded = expanded, 
        onDismissRequest = {  expanded = false }) {
        pistachiosTypeList.forEach { type ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    setHarvestType(type)
                }) {
                    Text(
                        text = type,
                        modifier = Modifier
                            .width(100.dp)
                            .padding(vertical = 5.dp, horizontal = 20.dp),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
    }

}

