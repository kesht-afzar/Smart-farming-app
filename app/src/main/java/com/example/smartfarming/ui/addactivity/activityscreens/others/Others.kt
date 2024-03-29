package com.example.smartfarming.ui.addactivity.activityscreens.others

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Agriculture
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.smartfarming.R
import com.example.smartfarming.ui.addactivities.ui.theme.*
import com.example.smartfarming.ui.addactivity.activityscreens.common_compose.ActivityTitle
import com.example.smartfarming.ui.addactivity.activityscreens.common_compose.DateSelector
import com.example.smartfarming.ui.addactivity.activityscreens.common_compose.SuccessCompose
import com.example.smartfarming.ui.authentication.ui.theme.sina
import com.example.smartfarming.ui.common_composables.ActivitiesStepBars
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Others(gardenName : String, navHostController : NavHostController){

    val activity = LocalContext.current as Activity
    //val viewModel : OthersViewModel = viewModel(factory = OthersViewModelFactory((activity.application as FarmApplication).repo))
    val viewModel : OthersViewModel = hiltViewModel()
    var startup by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = null) {
        delay(100)
        startup = true
    }

    Scaffold(
        Modifier
            .background(MainGreen100)
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(LightGreen3),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActivityTitle(
                gardenName = gardenName,
                activityName = "سایر" ,
                icon = Icons.Default.Agriculture,
                color = MainGreen
            )
            ActivitiesStepBars(step = viewModel.step.value, colorDark = MainGreen, colorLight = LightGreen1)
            AnimatedVisibility(visible = startup) {
                OthersBody(viewModel, navHostController, gardenName)
            }
        }
    }
}

@Composable
fun OthersBody(viewModel: OthersViewModel, navHostController: NavHostController, gardenName: String){
    Card(
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 15.dp)
            .fillMaxHeight(1f)
            .fillMaxWidth()
        ,
        shape = RoundedCornerShape(20.dp),
        elevation = 3.dp
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            val (bottomRow, body, backgroundPic) = createRefs()

            Icon(
                Icons.Outlined.Agriculture,
                contentDescription = null,
                tint = LightGreen1.copy(.1f),
                modifier = Modifier
                    .padding(bottom = 1.dp)
                    .size(300.dp)
                    .constrainAs(backgroundPic) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Column(
                Modifier
                    .fillMaxSize()
                    .constrainAs(body) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(bottomRow.top)
                    }
            ) {
                Crossfade(viewModel.step.value, animationSpec = tween(500)) {
                    when (it) {
                        0 ->
                            Column(
                                Modifier
                                    .fillMaxSize()
                                    .padding(30.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                DateSelector(
                                    title = "فعالیت",
                                    date = viewModel.date.value,
                                    color = MainGreen,
                                    colorLight = MainGreen100
                                ) {
                                    viewModel.date.value = it
                                }
                                ActivityName(viewModel)

                                AnimatedVisibility(visible = (viewModel.activityName.value == "سایر")) {
                                    ActivityNameSpecify(viewModel = viewModel)
                                }
                            }
                        1 -> OtherActivityPage2(viewModel)
                        2 -> SuccessCompose(navHostController)
                    }
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .constrainAs(bottomRow) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (viewModel.step.value == 0){
                            navHostController.popBackStack()
                        }
                        viewModel.decreaseStep() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MainGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .padding(6.dp)
                        .height(55.dp)
                        .fillMaxWidth(0.2f)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }

                Button(
                    onClick = { viewModel.submitClickHandler(gardenName) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MainGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth(0.9f)
                        .height(55.dp)
                ) {
                    Text(
                        text = if (viewModel.step.value == 0) "بعدی" else "ثبت اطلاعات",
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityName(viewModel: OthersViewModel){
    
    val otherActvitiesList = stringArrayResource(id = R.array.other_activities)
    
    var clicked by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "نوع فعالیت",
                style = MaterialTheme.typography.subtitle1,
                color = MainGreen
            )
            Icon(
                Icons.Outlined.Agriculture,
                contentDescription = null,
                tint = MainGreen,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        Row(
            modifier = Modifier
                .height(55.dp)
                .fillMaxWidth()
                .background(MainGreen100, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .clickable {
                    clicked = !clicked
                }
                .padding(end = 30.dp, start = 15.dp)

            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = MainGreen, modifier = Modifier.size(40.dp))
            Text(text = viewModel.activityName.value, color = MainGreen, style = MaterialTheme.typography.body1)
        }
        DropdownMenu(
            expanded = clicked,
            onDismissRequest = { clicked = false }
        ) {
            otherActvitiesList.forEach {
                DropdownMenuItem(
                    onClick = {
                        clicked = false
                        viewModel.activityName.value = it
                    }) {
                    Text(text = it, style = MaterialTheme.typography.body1, color = Color.Black)
                }
            }
        }

    }
}

@Composable
fun ActivityNameSpecify(viewModel: OthersViewModel){

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "نام فعالیت فعالیت",
                style = MaterialTheme.typography.subtitle1,
                color = MainGreen
            )
            Icon(
                Icons.Outlined.Agriculture,
                contentDescription = null,
                tint = MainGreen,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        OutlinedTextField(
            value = viewModel.activityName.value,
            onValueChange = {
                viewModel.activityNameSpecify.value = it},
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(horizontal = 0.dp)
                .background(Purple100, RoundedCornerShape(20.dp))
            ,
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = MainGreen100,
                textColor = MainGreen,
                focusedBorderColor = MainGreen100,
                unfocusedBorderColor = MainGreen100),
            textStyle = TextStyle(
                fontFamily = sina,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                textDirection = TextDirection.ContentOrRtl,
                textAlign = TextAlign.Center
            ),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {focusManager.clearFocus()}
            )
        )

    }
}

@Composable
fun OtherActivityPage2(viewModel: OthersViewModel) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Cause(viewModel)
        ActivityDescriptor(viewModel)
    }
}

@Composable
fun Cause(viewModel: OthersViewModel) {

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "علت فعالیت",
                style = MaterialTheme.typography.subtitle1,
                color = MainGreen
            )
            Icon(
                Icons.Outlined.Agriculture,
                contentDescription = null,
                tint = MainGreen,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        OutlinedTextField(
            value = viewModel.activityCause.value,
            onValueChange = {
                viewModel.setActivityCause(it)},
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(horizontal = 0.dp)
                .background(Purple100, RoundedCornerShape(20.dp))
            ,
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = MainGreen100,
                textColor = MainGreen,
                focusedBorderColor = MainGreen100,
                unfocusedBorderColor = MainGreen100),
            textStyle = TextStyle(
                fontFamily = sina,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                textDirection = TextDirection.ContentOrRtl,
                textAlign = TextAlign.Center
            ),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {focusManager.clearFocus()}
            )
        )
    }
}

@Composable
fun ActivityDescriptor(viewModel: OthersViewModel) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "توضیحات",
                style = MaterialTheme.typography.subtitle1,
                color = MainGreen
            )
            Icon(
                Icons.Outlined.Agriculture,
                contentDescription = null,
                tint = MainGreen,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        OutlinedTextField(
            value = viewModel.activityDescription.value,
            onValueChange = {
                viewModel.setActivityDescription(it)},
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(horizontal = 0.dp)
                .background(Purple100, RoundedCornerShape(20.dp))
            ,
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = MainGreen100,
                textColor = MainGreen,
                focusedBorderColor = MainGreen100,
                unfocusedBorderColor = MainGreen100),
            textStyle = TextStyle(
                fontFamily = sina,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                textDirection = TextDirection.ContentOrRtl,
                textAlign = TextAlign.Center
            ),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {focusManager.clearFocus()}
            )
        )
    }
}















