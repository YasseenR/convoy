package edu.temple.convoy

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


val JSON = "application/json; charset=utf-8".toMediaType()
@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var username by remember {
        mutableStateOf("")
    }
    var firstName by remember {
        mutableStateOf("")
    }
    var lastName by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    val state = remember { TextFieldState() }
    LaunchedEffect(state.text) {
        password = state.text as String
    }
    var showPassword by remember { mutableStateOf(false)}
    var loginResult by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign In",
            style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier
            .height(48.dp))



        Text("Username")
        OutlinedTextField(
            value = username,
            onValueChange = { text ->
                username = text
            },
            modifier = Modifier.fillMaxWidth(0.75f)
        )

        Text("First Name")
        OutlinedTextField(
            value = firstName,
            onValueChange = { text ->
                firstName = text
            },
            modifier = Modifier.fillMaxWidth(0.75f)
        )

        Text("Last Name")
        OutlinedTextField(
            value = lastName,
            onValueChange = { text ->
                lastName = text
            },
            modifier = Modifier.fillMaxWidth(0.75f)
        )

        Text("Password")
        BasicSecureTextField(
            state = state,
            textObfuscationMode =
                if (showPassword) {
                    TextObfuscationMode.Visible
                } else {
                    TextObfuscationMode.RevealLastTyped
                },
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(6.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                .padding(6.dp),
            decorator = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 16.dp, end = 48.dp)
                    ) {
                        innerTextField()
                    }
                    Icon(
                        if (showPassword) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        },
                        "Toggle password visibility",
                        Modifier
                            .align(Alignment.CenterEnd)
                            .requiredSize(48.dp).padding(16.dp)
                            .clickable { showPassword = !showPassword}
                    )
                }
            }
        )

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Button(onClick = {
                scope.launch {
                    try {
                        val response = register(username, firstName, lastName, password)
                        if (response.status == "SUCCESS") {
                            loginResult = "Logged in! Session: ${response.session_key}"
                        }
                        Log.d("LOGIN", response.message ?: "")
                    } catch (e: Exception) {
                        Log.e("LOGIN", "Failed", e)
                    }
                }
            }) {
                Text("Register")
            }

            Spacer(modifier = Modifier
                .width(12.dp))

            OutlinedButton(onClick = {
                navController.popBackStack()
            }) {
                Text("Login")
            }

            Text(loginResult)
        }


    }
}

suspend fun register(username: String, firstName: String, lastName: String, password: String): ApiResponse =
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