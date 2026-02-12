package edu.temple.convoy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import edu.temple.convoy.ui.theme.ConvoyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNav()
        }
    }
}

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val sessionKey by SessionDataStore
        .sessionKeyFlow(context)
        .collectAsState(initial = null)

    LaunchedEffect(sessionKey) {
        if (sessionKey != null) {
            // Optionally verify session with API
            navController.navigate("map") {
                popUpTo("login") { inclusive = true }
            }
        }
    }
    NavHost(
        navController,
        "login"
    ) {
        composable("login") { LoginScreen(navController)}
        composable("register") { RegisterScreen(navController)}
        composable("map") {MapScreen(navController, LatLng(37.7749, -122.4194))}
    }
}

@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val granted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) onPermissionGranted()
    }

    LaunchedEffect(Unit) {
        launcher.launch(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }
}