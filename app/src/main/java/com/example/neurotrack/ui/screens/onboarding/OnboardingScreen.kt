package com.example.neurotrack.ui.screens.onboarding

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.neurotrack.MainActivity
import com.example.neurotrack.R
import com.example.neurotrack.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = koinViewModel()
) {
    var userName by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo ou ícone do app
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 24.dp)
        )
        
        // Título de boas-vindas
        Text(
            text = "Bem-vindo ao NeuroTracker",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Descrição
        Text(
            text = "Acompanhe comportamentos e emoções de forma simples e eficaz.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // Campo para inserir o nome
        OutlinedTextField(
            value = userName,
            onValueChange = { 
                userName = it
                showError = false
            },
            label = { Text("Nome do paciente") },
            singleLine = true,
            isError = showError,
            supportingText = if (showError) {
                { Text("Por favor, insira o nome do paciente") }
            } else null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
        
        // Botão para começar
        Button(
            onClick = {
                if (userName.isBlank()) {
                    showError = true
                } else {
                    // Use coroutine scope to handle async operations
                    scope.launch {
                        try {
                            // Save user name first
                            viewModel.saveUserName(userName)
                            // Then mark onboarding as completed
                            viewModel.completeOnboarding()
                            
                            // Restart the activity to go to the home screen
                            val intent = Intent(context, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                            (context as? Activity)?.finish()
                        } catch (e: Exception) {
                            // Handle any exceptions
                            e.printStackTrace()
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Começar",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
} 