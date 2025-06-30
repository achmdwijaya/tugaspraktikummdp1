package com.app.praktikum_kel1_2.screen

import android.util.Patterns
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
import com.app.praktikum_kel1_2.model.request.RegisterRequest
import com.app.praktikum_kel1_2.navigation.Screen
import com.app.praktikum_kel1_2.service.api.ApiClient
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavHostController) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var fullNameError by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }

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
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Buat Akun Baru",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF333333)
                )

                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        fullNameError = false
                    },
                    isError = fullNameError,
                    label = { Text("Nama Lengkap") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                if (fullNameError) Text("Nama lengkap wajib diisi", color = MaterialTheme.colorScheme.error)

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
                if (usernameError) Text("Username wajib diisi", color = MaterialTheme.colorScheme.error)

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = false
                    },
                    isError = emailError,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                if (emailError) Text("Email tidak valid", color = MaterialTheme.colorScheme.error)

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
                if (passwordError) Text("Password wajib diisi", color = MaterialTheme.colorScheme.error)

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = false
                    },
                    isError = confirmPasswordError,
                    label = { Text("Konfirmasi Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                if (confirmPasswordError) Text("Password tidak cocok", color = MaterialTheme.colorScheme.error)

                Button(
                    onClick = {
                        focusManager.clearFocus()

                        fullNameError = fullName.isBlank()
                        usernameError = username.isBlank()
                        emailError = email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        passwordError = password.isBlank()
                        confirmPasswordError = confirmPassword != password

                        if (!fullNameError && !usernameError && !emailError && !passwordError && !confirmPasswordError) {
                            isLoading = true
                            coroutineScope.launch {
                                try {
                                    val response = ApiClient.instance.register(
                                        RegisterRequest(
                                            nm_lengkap = fullName,
                                            email = email,
                                            username = username,
                                            password = password
                                        )
                                    )
                                    isLoading = false
                                    val body = response.body()

                                    if (response.isSuccessful && body != null) {
                                        Toast.makeText(context, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                                        navController.navigate(Screen.Login.route) {
                                            popUpTo(Screen.Register.route) { inclusive = true }
                                        }
                                    } else {
                                        val errorMsg = body?.message ?: response.message()
                                        Toast.makeText(context, "Gagal: $errorMsg", Toast.LENGTH_LONG).show()
                                    }
                                } catch (e: Exception) {
                                    isLoading = false
                                    Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
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
                    Text("Register")
                }

                TextButton(
                    onClick = { navController.navigate(Screen.Login.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sudah punya akun? Login", color = Color(0xFF3F51B5))
                }
            }
        }

        if (isLoading) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {},
                title = { Text("Mohon tunggu") },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Sedang mengirim data...")
                    }
                }
            )
        }
    }
}
