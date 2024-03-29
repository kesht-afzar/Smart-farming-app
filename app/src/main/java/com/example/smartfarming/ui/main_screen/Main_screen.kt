package com.example.smartfarming.ui.main_screen

import UserProfileScreen
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartfarming.ui.gardens.composables.GardenCompose
import com.example.smartfarming.ui.harvest.HarvestNavGraph
import com.example.smartfarming.ui.harvest.compose.HarvestCompose
import com.example.smartfarming.ui.home.HomeNavGraph
import com.example.smartfarming.ui.home.composables.HomeCompose
import com.example.smartfarming.ui.home.composables.MyFAB
import com.example.smartfarming.ui.main_screen.bottom_navigation.*
import com.example.smartfarming.utils.isOnline

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(mainNavHostController: NavHostController){
    val navController = rememberNavController()

    val context = LocalContext.current

    // FAB
    var fabExtended by remember {
        mutableStateOf(false)
    }

    var currentPage by remember {
        mutableStateOf<String?>(null)
    }

    var showFAB by remember {
        mutableStateOf(true)
    }

    if (!isOnline(context)){
        Toast.makeText(context, "اینترنت در درسترس نیست", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            //TopAppBar()
        },
        bottomBar = {
            AppBottomNavigation(navController = navController){
                currentPage = it
            }
                    },
        floatingActionButton = {
            if (currentPage != null && (currentPage == NAV_HOME || currentPage == NAV_GARDENS) && showFAB){
                MyFAB(context = context, fabExtended = fabExtended, type = if (currentPage == NAV_HOME) "add activity" else "add garden") {
                    fabExtended =! fabExtended
                }
            }
        }
    ) {
        Column(modifier = Modifier.padding(bottom = 90.dp)) {
            NavHost(navController = navController, startDestination = NAV_HOME ){
                composable(NAV_HOME) {
                    val homeNavController = rememberNavController()
                    HomeNavGraph(navController = homeNavController, mainNavHostController){ showFAB = it }
                }

                composable(NAV_GARDENS) { GardenCompose() }

                composable(NAV_HARVEST) {
                    val harvestNavController = rememberNavController()
                    HarvestNavGraph(
                        navController = harvestNavController
                    )
                }

                composable(NAV_PROFILE) { UserProfileScreen() }
            }
        }
    }


}

@Composable
fun AppScreen(text: String) = Surface(modifier = Modifier.fillMaxSize()) {

}