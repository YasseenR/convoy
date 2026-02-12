package edu.temple.convoy

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

@Composable
fun MapScreen(navController: NavController, userLocation: LatLng?) {
    val context = LocalContext.current
    val sessionKey by SessionDataStore
        .sessionKeyFlow(context)
        .collectAsState(initial = null)
    if (userLocation == null) {
        Text("Getting Location...")
        return
    }
    Box(modifier = Modifier.fillMaxSize()){
        GoogleMap(modifier = Modifier.fillMaxSize(),
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(userLocation, 15f)
            }) {
            Marker(
                state = rememberUpdatedMarkerState(position = userLocation),
                title = "You are here"
            )
        }

        FloatingActionButton(
            onClick = {  },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text("Join a convoy $sessionKey")
        }

        FloatingActionButton(
            onClick = {  },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Create a convoy")
        }

        FloatingActionButton(
            onClick = {  },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("Leave convoy")
        }
    }

}

suspend fun joinConvoy(username: String, firstName: String, lastName: String, password: String): ApiResponse =
    withContext(Dispatchers.IO){
        val client = OkHttpClient()

        Log.d("U", username)
        Log.d("F", firstName)
        Log.d("L", lastName)
        Log.d("P", password)

        val requestBody = FormBody.Builder()
            .add("action", "REGISTER")
            .add("username", username)
            .add("firstname", firstName)
            .add("lastname", lastName)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url("https://kamorris.com/lab/convoy/account.php")
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->

            val responseBody = response.body?.string() ?: ""
            Log.d("REGISTER", "Response body: $responseBody")
            if (!response.isSuccessful) {
                throw Exception("Network Error: $responseBody")
            }

            Json.decodeFromString<ApiResponse>(responseBody)
        }
    }

suspend fun createConvoy(username: String, sessionKey: String): ApiResponse =
    withContext(Dispatchers.IO){
        val client = OkHttpClient()

        Log.d("U", username)

        val requestBody = FormBody.Builder()
            .add("action", "REGISTER")
            .add("username", username)
            .add("session_key", sessionKey)
            .build()

        val request = Request.Builder()
            .url("https://kamorris.com/lab/convoy/account.php")
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->

            val responseBody = response.body?.string() ?: ""
            Log.d("REGISTER", "Response body: $responseBody")
            if (!response.isSuccessful) {
                throw Exception("Network Error: $responseBody")
            }

            Json.decodeFromString<ApiResponse>(responseBody)
        }
    }

suspend fun leaveConvoy(username: String, firstName: String, lastName: String, password: String): ApiResponse =
    withContext(Dispatchers.IO){
        val client = OkHttpClient()

        Log.d("U", username)
        Log.d("F", firstName)
        Log.d("L", lastName)
        Log.d("P", password)

        val requestBody = FormBody.Builder()
            .add("action", "REGISTER")
            .add("username", username)
            .add("firstname", firstName)
            .add("lastname", lastName)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url("https://kamorris.com/lab/convoy/account.php")
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->

            val responseBody = response.body?.string() ?: ""
            Log.d("REGISTER", "Response body: $responseBody")
            if (!response.isSuccessful) {
                throw Exception("Network Error: $responseBody")
            }

            Json.decodeFromString<ApiResponse>(responseBody)
        }
    }