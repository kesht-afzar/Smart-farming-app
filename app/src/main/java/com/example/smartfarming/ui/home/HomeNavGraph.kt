package com.example.smartfarming.ui.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.smartfarming.ui.AppScreensEnum
import com.example.smartfarming.ui.addactivity.activityscreens.Pesticides
import com.example.smartfarming.ui.addactivity.activityscreens.fertilization.Fertilization
import com.example.smartfarming.ui.addactivity.activityscreens.irrigation.Irrigation
import com.example.smartfarming.ui.addactivity.activityscreens.others.Others
import com.example.smartfarming.ui.home.composables.HomeCompose

@Composable
fun HomeNavGraph(navController : NavHostController, setShowFAB : (Boolean) -> Unit){

    val homeScreen = AppScreensEnum.HomeScreen.name
    val irrigation = AppScreensEnum.IrrigationScreen.name
    val fertilization = AppScreensEnum.FertilizationScreen.name
    val others = AppScreensEnum.OtherActivitiesScreen.name
    val pesticide = AppScreensEnum.PesticideScreen.name

    NavHost(navController = navController, startDestination = homeScreen){
        composable(route = homeScreen){
            HomeCompose(navController){setShowFAB(it)}
        }

        composable(route = "$others/{name}",
            arguments = listOf(navArgument("name"){
                type = NavType.StringType
            })
        ) { entry ->
            val gardenName = entry.arguments?.getString("name")
            Others(gardenName!!, navController)
        }

        composable(route = "$fertilization/{name}",
            arguments = listOf(navArgument("name"){
                type = NavType.StringType
            })
        ) { entry ->
            val gardenName = entry.arguments?.getString("name")
            //Fertilization(gardenName!!, navController)
        }

        composable(route = "$irrigation/{name}",
            arguments = listOf(navArgument("name"){
                type = NavType.StringType
            })
        ) { entry ->
            val gardenName = entry.arguments?.getString("name")
            Irrigation(gardenName!!, navController)
        }

        composable(route = "$pesticide/{name}",
            arguments = listOf(navArgument("name"){
                type = NavType.StringType
            })
        ) { entry ->
            val gardenName = entry.arguments?.getString("name")
            Pesticides(gardenName!!, navController)
        }






    }
}