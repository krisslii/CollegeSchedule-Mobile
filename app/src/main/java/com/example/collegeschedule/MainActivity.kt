package com.example.collegeschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.LaunchedEffect
import com.example.collegeschedule.data.local.AppDatabase
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.data.repository.FavoriteRepository
import com.example.collegeschedule.data.repository.ScheduleRepository
import com.example.collegeschedule.ui.favorites.FavoritesScreen
import com.example.collegeschedule.ui.group.GroupSearchScreen
import com.example.collegeschedule.ui.profile.ProfileScreen
import com.example.collegeschedule.ui.schedule.ScheduleScreen
import com.example.collegeschedule.ui.theme.CollegeScheduleTheme
import com.example.collegeschedule.viewmodel.FavoritesViewModel
import com.example.collegeschedule.viewmodel.GroupViewModel
import com.example.collegeschedule.viewmodel.ScheduleViewModel

class MainActivity : ComponentActivity() {

    private lateinit var scheduleRepository: ScheduleRepository
    private lateinit var favoriteRepository: FavoriteRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scheduleRepository = ScheduleRepository(RetrofitInstance.api)
        favoriteRepository = FavoriteRepository(AppDatabase.getDatabase(application).favoriteDao())

        setContent {
            CollegeScheduleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp(
                        scheduleRepository = scheduleRepository,
                        favoriteRepository = favoriteRepository
                    )
                }
            }
        }
    }
}

@Composable
fun MainApp(
    scheduleRepository: ScheduleRepository,
    favoriteRepository: FavoriteRepository
) {
    val navController = rememberNavController()

    val scheduleViewModel: ScheduleViewModel = viewModel {
        ScheduleViewModel(scheduleRepository)
    }

    val groupViewModel: GroupViewModel = viewModel {
        GroupViewModel(scheduleRepository)
    }

    val favoritesViewModel: FavoritesViewModel = viewModel {
        FavoritesViewModel(favoriteRepository)
    }

    var currentGroup by remember { mutableStateOf<String?>(null) }

    // Загружаем группы при запуске
    LaunchedEffect(Unit) {
        groupViewModel.loadGroups()
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "schedule",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("schedule") {
                ScheduleScreen(
                    currentGroup = currentGroup,
                    onGroupSelect = {
                        navController.navigate("groups")
                    },
                    scheduleViewModel = scheduleViewModel
                )
            }

            composable("groups") {
                GroupSearchScreen(
                    groups = groupViewModel.groups.collectAsState().value,
                    onGroupSelected = { groupName ->
                        currentGroup = groupName
                        navController.popBackStack()
                    }
                )
            }

            composable("favorites") {
                FavoritesScreen(
                    favorites = favoritesViewModel.favorites.collectAsState().value,
                    onGroupClick = { groupName ->
                        currentGroup = groupName
                        navController.navigate("schedule")
                    },
                    onRemoveFavorite = { groupName ->
                        favoritesViewModel.removeFavorite(groupName)
                    }
                )
            }

            composable("profile") {
                ProfileScreen(
                    currentGroup = currentGroup,
                    onLogout = {
                        currentGroup = null
                        navController.navigate("schedule") {
                            popUpTo("schedule") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Schedule") },
            label = { Text("Расписание") },
            selected = currentRoute == "schedule",
            onClick = {
                navController.navigate("schedule") {
                    popUpTo("schedule") { inclusive = true }
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Groups") },
            label = { Text("Группы") },
            selected = currentRoute == "groups",
            onClick = {
                navController.navigate("groups") {
                    popUpTo("groups") { inclusive = true }
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
            label = { Text("Избранное") },
            selected = currentRoute == "favorites",
            onClick = {
                navController.navigate("favorites") {
                    popUpTo("favorites") { inclusive = true }
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Профиль") },
            selected = currentRoute == "profile",
            onClick = {
                navController.navigate("profile") {
                    popUpTo("profile") { inclusive = true }
                }
            }
        )
    }
}