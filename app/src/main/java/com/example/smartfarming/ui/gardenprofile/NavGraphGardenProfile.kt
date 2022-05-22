package com.example.smartfarming.ui.gardenprofile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.smartfarming.data.room.entities.Garden
import com.example.smartfarming.ui.AppScreensEnum
import com.example.smartfarming.ui.addactivity.activityscreens.Fertilization
import com.example.smartfarming.ui.addactivity.activityscreens.Irrigation
import com.example.smartfarming.ui.addactivity.activityscreens.Others
import com.example.smartfarming.ui.addactivity.activityscreens.Pesticides
import com.example.smartfarming.ui.addactivity.AddActivity
import com.example.smartfarming.ui.gardens.composables.GardenProfile

@Composable
fun NavGraphGardenProfile(
    navController: NavHostController,
    garden : State<Garden?>,
    viewModel: GardenProfileViewModel
){
    val irrigation = AppScreensEnum.IrrigationScreen.name
    val fertilization = AppScreensEnum.FertilizationScreen.name
    val pesticide = AppScreensEnum.PesticideScreen.name
    val otherActivity = AppScreensEnum.OtherActivitiesScreen.name
    val addActivity = AppScreensEnum.AddActivitiesScreen.name

    NavHost(navController = navController,
        startDestination = ScreensEnumGardenProfile.MainScreen.name
    ) {


        composable(route = ScreensEnumGardenProfile.MainScreen.name){
            GardenProfile(garden, navController)
        }


        composable(
            route = "${irrigation}/{gardenName}",
            arguments = listOf(
                navArgument("gardenName"){
                    type = NavType.StringType
                }
            )
        ){ entry ->
            val gardenName = entry.arguments?.getString("gardenName")
            Irrigation(gardenName = gardenName!!, navController)
        }


        composable(
            route = "${fertilization}/{gardenName}",
            arguments = listOf(
                navArgument("gardenName"){
                    type = NavType.StringType
                }
            )
        ){ entry ->
            val gardenName = entry.arguments?.getString("gardenName")
            Fertilization(
                gardenName = gardenName!!,
                navController = navController
            )
        }


        composable(
            route = "${AppScreensEnum.PesticideScreen.name}/{gardenName}",
            arguments = listOf(
                navArgument("gardenName"){
                    type = NavType.StringType
                }
            )
        ){ entry ->
            val gardenName = entry.arguments?.getString("gardenName")
            Pesticides(gardenName = gardenName!!)
        }


        composable(
            route = "${otherActivity}/{gardenName}",
            arguments = listOf(
                navArgument("gardenName"){
                    type = NavType.StringType
                }
            )
        ){ entry ->
            val gardenName = entry.arguments?.getString("gardenName")
            Others(gardenName = gardenName!!, viewModel = viewModel)
        }

        composable(
            route = "${addActivity}"
        ){
            //AddActivity(navController = navController)
        }

    }
}