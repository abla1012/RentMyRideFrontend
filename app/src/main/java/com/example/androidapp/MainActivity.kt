package com.example.androidapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.androidapp.Entity.Fahrzeug
import com.example.androidapp.Room.FahrzeugDatabase
import com.example.androidapp.Room.FahrzeugState
import com.example.androidapp.Room.FahrzeugViewModel
import com.example.androidapp.Screens.CreateScreen
import com.example.androidapp.Screens.DetailScreen
import com.example.androidapp.Screens.FahrzeugScreen
import com.example.androidapp.Screens.FavoritenScreen
import com.example.androidapp.Screens.NachrichtenScreen
import com.example.androidapp.retrofit.FahrzeugRepository
import com.example.androidapp.ui.theme.RoomGuideAndroidTheme
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            FahrzeugDatabase::class.java,
            "fahrzeuge.db"
        ).build()
    }

    private val viewModel by viewModels<FahrzeugViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FahrzeugViewModel(db.dao) as T
                }
            }
        }
    )

    suspend fun loadFahrzeugeFromBackend(): List<Fahrzeug> {
        val repository = FahrzeugRepository()
        repository.getFahrzeuge()
        var fahrzeuge = emptyList<Fahrzeug>()
        try {
            fahrzeuge = repository.getFahrzeuge().flatMapConcat { it.asFlow() }.toList()
        }catch (e:Exception) {
            Log.d("onCreate", "${e.message}")
        }
        return fahrzeuge
    }


    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking {
            db.dao.deleteAllFahrzeuge()
            loadFahrzeugeFromBackend().forEach { db.dao.upsertFahrzeug(it) }
        }

        setContent {
            RoomGuideAndroidTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(navController)
                    },
                    content = { innerPadding ->
                        NavigationHost(
                            navController,
                            modifier = Modifier.padding(innerPadding),
                            state = viewModel.state.collectAsState().value
                        )
                    }
                )
            }
        }
    }

    @Composable
    fun BottomNavigationBar(navController: NavHostController) {
        val items = listOf(
            Screen.Favorites,
            Screen.Search,
            Screen.Post,
            Screen.Messages,
            Screen.Settings
        )
        BottomNavigation {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { screen ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(screen.title)
                    },
                    selected = currentRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun NavigationHost(
        navController: NavHostController,
        state: FahrzeugState,
        modifier: Modifier = Modifier
    ) {
        Box(modifier) {
            NavHost(navController, startDestination = Screen.Search.route) {
                composable(Screen.Favorites.route) { FavoritesScreen() }
                composable(Screen.Search.route) {
                    SearchScreen(
                        viewModel.state.collectAsState().value,
                        navController = navController
                    )
                }
                composable(Screen.Post.route) { PostScreen() }
                composable(Screen.Messages.route) { MessagesScreen() }
                composable(Screen.Settings.route) { SettingsScreen() }
                composable("details/{fahrzeugId}") { backStackEntry ->
                    val fahrzeugId =
                        backStackEntry.arguments?.getString("fahrzeugId")?.toIntOrNull()
                    val fahrzeug = fahrzeugId?.let { id ->
                        state.fahrzeuge.find { it.id == id }
                    }
                    if (fahrzeug != null) {
                        DetailScreen(fahrzeug = fahrzeug, navController = navController)
                    } else {
                        // Handle invalid fahrzeugId or show error screen
                    }
                }
            }
        }
    }

    @Composable
    fun FavoritesScreen() {

        val state by viewModel.state.collectAsState()
        FavoritenScreen(state = state, onEvent = viewModel::onEvent)
    }


    @Composable
    fun SearchScreen(state: FahrzeugState, navController: NavHostController) {

        val state by viewModel.state.collectAsState()
        FahrzeugScreen(state = state, onEvent = viewModel::onEvent, navController = navController)
    }

    @Composable
    fun PostScreen() {

        val state by viewModel.state.collectAsState()
        CreateScreen(state = state, onEvent = viewModel::onEvent)
    }

    @Composable
    fun MessagesScreen() {
        val state by viewModel.state.collectAsState()
        NachrichtenScreen()
    }

    @Composable
    fun SettingsScreen() {
        val state by viewModel.state.collectAsState()
        com.example.androidapp.Screens.SettingsScreen(state = state, onEvent = viewModel::onEvent)
    }
}