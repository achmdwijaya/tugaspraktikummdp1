package com.app.praktikum_kel1_2.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.praktikum_kel1_2.model.request.LoginRequest
import com.app.praktikum_kel1_2.navigation.Screen
import com.app.praktikum_kel1_2.service.api.ApiClient
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFe0eafc), Color(0xFFcfdef3))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Selamat Datang, Para Rakyat Jelata",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF333333),
                )

                Text(
                    text = "mohon masukkan...",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF333333),
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        usernameError = false
                    },
                    isError = usernameError,
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (usernameError) {
                    Text(
                        "Username tidak boleh kosong",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = false
                    },
                    isError = passwordError,
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (passwordError) {
                    Text(
                        "Password tidak boleh kosong",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        usernameError = username.isBlank()
                        passwordError = password.isBlank()

                        if (!usernameError && !passwordError) {
                            isLoading = true
                            coroutineScope.launch {
                                try {
                                    val response = ApiClient.instance.login(
                                        LoginRequest(username = username, password = password)
                                    )
                                    isLoading = false
                                    val body = response.body()

                                    if (response.isSuccessful && body?.code == 200) {
                                        Toast.makeText(context, "Login berhasil!", Toast.LENGTH_SHORT).show()
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.Login.route) { inclusive = true }
                                        }
                                    } else {
                                        val errorMessage = body?.message ?: response.message()
                                        Toast.makeText(context, "Gagal: $errorMessage", Toast.LENGTH_LONG).show()
                                    }
                                } catch (e: Exception) {
                                    isLoading = false
                                    Toast.makeText(context, "Terjadi kesalahan: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3F51B5),
                        contentColor = Color.White
                    )
                ) {
                    Text("Login")
                }

                TextButton(
                    onClick = { navController.navigate(Screen.Register.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Belum punya akun? Daftar", color = Color(0xFF3F51B5))
                }
            }
        }

        if (isLoading) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {},
                title = { Text("Loading") },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Sedang login...")
                    }
                }
            )
        }
    }
}
