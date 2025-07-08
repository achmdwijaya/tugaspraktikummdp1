package com.app.praktikum_kel1_2.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.praktikum_kel1_2.components.NoteCard
import com.app.praktikum_kel1_2.model.viewModel.NotesViewModel
import com.app.praktikum_kel1_2.navigation.Screen

@Composable
fun HomeScreen(navController: NavController,viewModel : NotesViewModel = viewModel ()) {

    val notesState by viewModel.notes.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ){
        if(notesState.isEmpty()){
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ){
                items(notesState){
                    note ->
                    NoteCard(note)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenView() {
    HomeScreen(navController = rememberNavController())
}
