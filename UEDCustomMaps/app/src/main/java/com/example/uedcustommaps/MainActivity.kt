package com.example.uedcustommaps

import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storage = StorageManager(this)

        setContent {
            val navController = rememberNavController()
            val userMaps = remember {
                mutableStateListOf<UserMap>().apply {
                    addAll(storage.loadMaps())
                }
            }

            NavHost(navController, startDestination = "list_screen") {

                composable("list_screen") {
                    var showDialog by remember { mutableStateOf(false) }
                    var newTitle by remember { mutableStateOf("") }

                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = { Text("UED Custom Maps") }
                            )
                        },
                        floatingActionButton = {
                            FloatingActionButton(onClick = { showDialog = true }) {
                                Icon(Icons.Default.Add, null)
                            }
                        }
                    ) { padding ->

                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                title = { Text("Tên bản đồ mới") },
                                text = {
                                    OutlinedTextField(
                                        value = newTitle,
                                        onValueChange = { newTitle = it }
                                    )
                                },
                                confirmButton = {
                                    Button(onClick = {
                                        if (newTitle.isNotBlank()) {
                                            userMaps.add(UserMap(newTitle, emptyList()))
                                            storage.saveMaps(userMaps)
                                            newTitle = ""
                                            showDialog = false
                                        }
                                    }) { Text("Tạo") }
                                }
                            )
                        }

                        LazyColumn(modifier = Modifier.padding(padding)) {
                            itemsIndexed(userMaps) { index, map ->
                                ListItem(
                                    headlineContent = { Text(map.title) },
                                    supportingContent = {
                                        Text("${map.places.size} địa điểm")
                                    },
                                    modifier = Modifier.clickable {
                                        navController.currentBackStackEntry
                                            ?.savedStateHandle
                                            ?.set("index", index)
                                        navController.navigate("map_detail")
                                    }
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }

                composable("map_detail") {
                    val index = navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<Int>("index") ?: 0

                    MapDetailScreen(
                        userMap = userMaps[index],
                        onUpdate = {
                            userMaps[index] = it
                            storage.saveMaps(userMaps)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapDetailScreen(
    userMap: UserMap,
    onUpdate: (UserMap) -> Unit
) {
    val context = LocalContext.current

    val places = remember {
        mutableStateListOf<Place>().apply {
            addAll(userMap.places)
        }
    }

    var longClickPos by remember { mutableStateOf<LatLng?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var mTitle by remember { mutableStateOf("") }
    var mDesc by remember { mutableStateOf("") }
    var mapType by remember { mutableStateOf(MapType.NORMAL) }
    var showMapMenu by remember { mutableStateOf(false) }

    val filteredPlaces = places.filter {
        it.title.contains(searchQuery, ignoreCase = true)
    }

    val cameraPositionState = rememberCameraPositionState {
        val start = places.firstOrNull()
            ?.let { LatLng(it.latitude, it.longitude) }
            ?: showDaNang()
        position = CameraPosition.fromLatLngZoom(start, 12f)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(userMap.title) })
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    zoomGesturesEnabled = true
                ),
                properties = MapProperties(
                    mapType = mapType
                ),
                onMapLongClick = { longClickPos = it }
            ) {
                filteredPlaces.forEach { place ->
                    Marker(
                        state = MarkerState(
                            position = LatLng(place.latitude, place.longitude)
                        ),
                        title = place.title,
                        snippet = place.description
                    )
                }
            }

            // 🔍 SEARCH
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Tìm địa điểm (VD: Da Nang)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        searchLocation(
                            searchQuery,
                            context,
                            cameraPositionState
                        )
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter)
            )

            FloatingActionButton(
                onClick = { showMapMenu = true },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text("Map")
            }

            if (showMapMenu) {
                AlertDialog(
                    onDismissRequest = { showMapMenu = false },
                    title = { Text("Chọn kiểu bản đồ") },
                    text = {
                        Column {

                            Button(onClick = {
                                mapType = MapType.NORMAL
                                showMapMenu = false
                            }) { Text("Normal") }

                            Button(onClick = {
                                mapType =MapType.SATELLITE
                                showMapMenu = false
                            }) { Text("Satellite") }

                            Button(onClick = {
                                mapType = MapType.TERRAIN
                                showMapMenu = false
                            }) { Text("Terrain") }

                            Button(onClick = {
                                mapType =MapType.HYBRID
                                showMapMenu = false
                            }) { Text("Hybrid") }

                        }
                    },
                    confirmButton = {}
                )
            }



            // ➕ ADD PLACE
            if (longClickPos != null) {
                AlertDialog(
                    onDismissRequest = { longClickPos = null },
                    title = { Text("Thêm địa điểm") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = mTitle,
                                onValueChange = { mTitle = it },
                                label = { Text("Tên") }
                            )
                            OutlinedTextField(
                                value = mDesc,
                                onValueChange = { mDesc = it },
                                label = { Text("Mô tả") }
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            val place = Place(
                                mTitle,
                                mDesc,
                                longClickPos!!.latitude,
                                longClickPos!!.longitude
                            )
                            places.add(place)
                            onUpdate(userMap.copy(places = places.toList()))
                            mTitle = ""
                            mDesc = ""
                            longClickPos = null
                        }) {
                            Text("Lưu")
                        }
                    }
                )
            }
        }
    }
}

// =================== GEOCODER SEARCH ===================
fun searchLocation(
    query: String,
    context: Context,
    cameraPositionState: CameraPositionState
) {
    if (query.isBlank()) return

    val geocoder = Geocoder(context, Locale.getDefault())

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocationName(query, 1) { addresses ->
                if (addresses.isNotEmpty()) {
                    val loc = addresses[0]
                    val latLng = LatLng(loc.latitude, loc.longitude)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(
                        latLng,
                        14f
                    )

                }
            }
        } else {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocationName(query, 1)
            if (!addresses.isNullOrEmpty()) {
                val loc = addresses[0]
                val latLng = LatLng(loc.latitude, loc.longitude)
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    latLng,
                    14f
                )

            }
        }
    } catch (_: Exception) {
        // ignore
    }
}

fun showDaNang(): LatLng = LatLng(16.06, 108.21)
