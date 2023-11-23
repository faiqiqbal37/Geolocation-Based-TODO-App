package com.team2.todo.screens.listing

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.team2.todo.common_ui_components.ReminderAlertCompose
import com.team2.todo.data.RealEstateDatabase
import com.team2.todo.data.repo.TodoRepo
import com.team2.todo.screens.listing.ui_components.BottomNavigationCompose
import com.team2.todo.screens.listing.ui_components.completed_sale.CompletedSaleList
import com.team2.todo.screens.listing.ui_components.in_sale.InSaleList
import com.team2.todo.screens.listing.view_model.ListingViewModel
import com.team2.todo.screens.listing.view_model.PropertyListViewModel
import com.team2.todo.utils.NavigationUtil
import com.team2.todo.utils.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var currentPage by remember { mutableIntStateOf(0) }
    var showReminderAlert by remember { mutableStateOf(false) }

    val database = RealEstateDatabase.getInstance(context = LocalContext.current)
    val repo = TodoRepo(database)
    ListingViewModel.initialize(repo = repo)
    val viewModel = ListingViewModel.instance

    MaterialTheme(typography = Typography()) {
        Scaffold(
            floatingActionButton = {
                if (currentPage == 0) ExtendedFloatingActionButton(
                    onClick = { NavigationUtil.navigateTo(Screen.AddTodos) },
                    icon = { Icon(Icons.Filled.AddCircle, "Extended floating action button.") },
                    text = { Text(text = "Add New Property") },
                )
            },
            bottomBar = {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp,
                    ),
                    modifier = Modifier.border(
                        0.1.dp, color = Color.Gray, shape = RoundedCornerShape(0.dp)
                    )
                ) {
                    BottomNavigationCompose(
                        currentPage = currentPage,
                        onClick = { nextPage -> currentPage = nextPage },
                    )
                }
            }
        ) { it ->
            if (showReminderAlert) {
                ModalBottomSheet(onDismissRequest = { showReminderAlert = false; }) {
                    ReminderAlertCompose()
                }
            }
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (currentPage == 0) {
                    InSaleList(viewModel)
                } else {
                    CompletedSaleList(viewModel)
                }


            }
        }
    }
}