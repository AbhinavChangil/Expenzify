package com.example.expenzify

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expenzify.features.add_expense.AddExpenseScreen
import com.example.expenzify.features.home.HomeScreen
import com.example.expenzify.features.stats.StatsScreen
import com.example.expenzify.ui.theme.DarkBlue
import com.example.expenzify.ui.theme.lightBlue

@Composable
fun NavHostScreen() {
    val navController = rememberNavController()

    var bottomNavBarVisibility by remember{
        mutableStateOf(true)
    }

    Scaffold(
        bottomBar = {

            AnimatedVisibility(visible = bottomNavBarVisibility) {
                BottomAppBarWithFAB(
                    navController = navController,
                    items = listOf(
                        NavItem("Home", "/home", R.drawable.ic_home),
                        NavItem("Add", "/add", R.drawable.add_icon),
                        NavItem("Stats", "/stats", R.drawable.ic_stats)
                    )
                )
            }

        },
        content = { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "/home",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("/home") {
                    bottomNavBarVisibility = true
                    HomeScreen(navController)
                }
                composable("/add") {
                    bottomNavBarVisibility = false
                    AddExpenseScreen(navController)
                }
                composable("/stats") {
                    bottomNavBarVisibility = true
                    StatsScreen(navController)
                }
            }
        }
    )
}

data class NavItem(
    val title: String,
    val route: String,
    val icon: Int
)

@Composable
fun BottomAppBarWithFAB(navController: NavController, items: List<NavItem>) {
    var selectedRoute by remember { mutableStateOf("/home") }

    Box {
        BottomAppBar(
            modifier = Modifier.height(55.dp),
            containerColor = Color.White,
            contentColor = Color.Black,
            tonalElevation = 4.dp
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    if (index == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    } else {
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                selectedRoute = item.route
                                if (item.route == "/home") {
                                    // Clear the back stack before navigating to the Home screen
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            inclusive = true
                                        }
                                    }
                                } else {
                                    navController.navigate(item.route) {
                                        // Handle back stack management for other screens
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                // Custom Row for icon and label
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = item.icon),
                                        contentDescription = null,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    if (selectedRoute == item.route) {
                                        Text(text = item.title)
                                    }
                                }
                            },
                            alwaysShowLabel = false,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = DarkBlue,
                                indicatorColor = DarkBlue,
                                unselectedIconColor = Color.Black,
                                unselectedTextColor = Color.Black
                            )
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                navController.navigate("/add") {
                    // Ensure proper back stack management when navigating to Add
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-25).dp) // Move the FAB upwards
                .size(55.dp)
                .clip(CircleShape),
            containerColor = lightBlue,
            contentColor = DarkBlue,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add_icon),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}
