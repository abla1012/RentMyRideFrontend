package com.example.androidapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


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
        } catch (e: Exception) {
            Log.d("onCreate", "${e.message}")
        }
        return fahrzeuge
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO Prüfen ob die bilder schon existieren (bei jedem start werden die bilder in den speicher geschrieben)
        runBlocking {
            //db.dao.deleteAllFahrzeuge()

            // deviceManager -> explorer -> storage -> emulated -> 0 -> pictures -> RoomGuideAndroid
            val pfad =
                "${Environment.getExternalStorageDirectory()}/Pictures/${getString(R.string.app_name)}"
            var number = 1
            loadFahrzeugeFromBackend().forEach {

                // TODO !!Nach dem ersten start auskommentieren
                // saveBitmapImage(ConvertPicture().encodedStringToBitmap(it.fotoURL, applicationContext), number)

                // TODO Naming of pictures not optimal (-> name after id and delete all old pictures on start)
                Log.d("saveBitmapImage", "$pfad/$number.png")
                number++
                val id = db.dao.upsertFahrzeug(it.copy(fotoURL = "$pfad/$number.png"))
                Log.d("Start: ", "$id || $it")
            }
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

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun FavoritesScreen() {

        val state by viewModel.state.collectAsState()
        FavoritenScreen(state = state, onEvent = viewModel::onEvent)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun SearchScreen(state: FahrzeugState, navController: NavHostController) {

        val state by viewModel.state.collectAsState()
        FahrzeugScreen(state = state, onEvent = viewModel::onEvent, navController = navController)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun SettingsScreen() {
        val state by viewModel.state.collectAsState()
        com.example.androidapp.Screens.SettingsScreen(state = state, onEvent = viewModel::onEvent)
    }

    /**
     * Save Bitmap To Gallery
     * @param bitmap The bitmap to be saved in Storage/Gallery
     *
     */
    private fun saveBitmapImage(bitmap: Bitmap?, number: Int): String {
        if (bitmap == null) return "Nope"

        val timestamp = System.currentTimeMillis()

        //Tell the media scanner about the new file so that it is immediately available to the user.
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, timestamp)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, number)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
            values.put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "Pictures/" + getString(R.string.app_name)
            )
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                try {
                    val outputStream = contentResolver.openOutputStream(uri)
                    if (outputStream != null) {
                        try {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                            outputStream.close()
                        } catch (e: Exception) {
                            Log.e(TAG, "saveBitmapImage: ", e)
                        }
                    }
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    contentResolver.update(uri, values, null, null)

                    Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e(TAG, "saveBitmapImage: ", e)
                }
            }
        } else {
            val imageFileFolder = File(
                Environment.getExternalStorageDirectory()
                    .toString() + '/' + getString(R.string.app_name)
            )
            if (!imageFileFolder.exists()) {
                imageFileFolder.mkdirs()
            }
            val mImageName = "$timestamp.png"
            val imageFile = File(imageFileFolder, mImageName)
            try {
                val outputStream: OutputStream = FileOutputStream(imageFile)
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                } catch (e: Exception) {
                    Log.e(TAG, "saveBitmapImage: ", e)
                }
                values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e(TAG, "saveBitmapImage: ", e)
            }

            return mImageName
        }
        return "$timestamp.png"
    }
}