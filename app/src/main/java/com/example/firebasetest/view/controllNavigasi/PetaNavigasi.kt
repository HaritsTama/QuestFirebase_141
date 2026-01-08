package com.example.firebasetest.view.controllNavigasi

import android.R.attr.type
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.firebasetest.view.DetailSiswaScreen
import com.example.firebasetest.view.EditSiswaScreen
import com.example.firebasetest.view.route.DestinasiDetail
import com.example.firebasetest.view.route.DestinasiEntry
import com.example.firebasetest.view.route.DestinasiHome
import com.example.firebasetest.view.EntrySiswaScreen
import com.example.firebasetest.view.HomeScreen
import com.example.firebasetest.view.route.DestinasiEdit

@Composable
fun DataSiswaApp(navController: NavHostController = rememberNavController(), modifier: Modifier){
    HostNavigasi(navController = navController)
}

@Composable
fun HostNavigasi(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(navController = navController, startDestination = DestinasiHome.route,
        modifier = Modifier ){
        composable(DestinasiHome.route){
            HomeScreen(navigateToItemEntry = { navController.navigate(DestinasiEntry.route) },
                navigateToItemUpdate = {
                    navController.navigate("${DestinasiDetail.route}/${it}")
                }
            )
        }
        composable(DestinasiEntry.route){
            EntrySiswaScreen(navigateBack = { navController.navigate(DestinasiHome.route)
            })
        }
        composable(DestinasiDetail.routeWithArgs, arguments = listOf(navArgument(DestinasiDetail.itemIdArg) {
            type = NavType.StringType
        })) {
            DetailSiswaScreen(navigateToEditItem = { navController.navigate("${DestinasiEdit.route}/$it") },
                navigateBack = { navController.navigate(DestinasiHome.route) })
        }
        composable(DestinasiEdit.routeWithArgs, arguments = listOf(navArgument(DestinasiEdit.itemIdArg) {
            type = NavType.StringType
        })) {
            EditSiswaScreen(navigateBack = { navController.navigate(DestinasiHome.route) },
                onNavigateUp = { navController.navigateUp() })
        }
    }
}