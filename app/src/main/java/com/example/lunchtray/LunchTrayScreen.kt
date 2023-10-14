/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunchtray

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.model.OrderUiState
import com.example.lunchtray.ui.AccompanimentMenuScreen
import com.example.lunchtray.ui.CheckoutScreen
import com.example.lunchtray.ui.EntreeMenuScreen
import com.example.lunchtray.ui.OrderViewModel
import com.example.lunchtray.ui.SideDishMenuScreen
import com.example.lunchtray.ui.StartOrderScreen
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

// TODO: Screen enum

enum class LunchOrderScreen() {
    Start,
    Entree_Menu,
    Side_Dish_Menu,
    Accomplishment_Menu,
    Checkout
}
// TODO: AppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayApp() {
    // TODO: Create Controller and initialization
    val navController = rememberNavController()
    // Create ViewModel
    val viewModel: OrderViewModel = viewModel()

    Scaffold(
        topBar = {
            // TODO: AppBar

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize(1f)
            ) {
                LunchTrayAppBar()
            }
        }

    ) { modifier ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,  // Provide a valid NavController
            startDestination = LunchOrderScreen.Start.name,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            composable(route = LunchOrderScreen.Start.name) {
                Column (
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    StartOrderScreen(
                    onStartOrderButtonClicked = { navController.navigate(LunchOrderScreen.Entree_Menu.name) },
                    )
                }
            }
            composable(route = LunchOrderScreen.Entree_Menu.name) {
                val context = LocalContext.current

                EntreeMenuScreen(
                    options = DataSource.entreeMenuItems,
                    onCancelButtonClicked = { navController.navigate(LunchOrderScreen.Start.name)},
                    onNextButtonClicked = { navController.navigate(LunchOrderScreen.Side_Dish_Menu.name) },
                    onSelectionChanged = { viewModel.updateEntree(it)},
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium))
                        .fillMaxSize()
                )
            }
            composable(route = LunchOrderScreen.Side_Dish_Menu.name) {
                SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onCancelButtonClicked = { navController.navigate(LunchOrderScreen.Start.name)},
                    onNextButtonClicked = {navController.navigate(LunchOrderScreen.Accomplishment_Menu.name)},
                    onSelectionChanged = { viewModel.updateSideDish(it) },
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium))
                        .fillMaxSize(),
                )
            }
            composable(route = LunchOrderScreen.Accomplishment_Menu.name) {
                AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onCancelButtonClicked = { navController.navigate(LunchOrderScreen.Start.name)},
                    onNextButtonClicked = { navController.navigate(LunchOrderScreen.Checkout.name) },
                    onSelectionChanged = { viewModel.updateAccompaniment(it) } ,
                )
            }
            composable(route = LunchOrderScreen.Checkout.name) {
                val context = LocalContext.current
                CheckoutScreen(
                    orderUiState = OrderUiState(),
                    onNextButtonClicked = { navController.navigate(LunchOrderScreen.Start.name) },
                    onCancelButtonClicked = { navController.navigate(LunchOrderScreen.Start.name) }
                )
            }
        }

        // TODO: Navigation host
    }
}

@Composable
fun LunchTrayAppBar() {

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top// Center the child horizontally // Center the child vertically
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxHeight(1f),
        )
    }

}

@Preview
@Composable
fun LunchOrderScreenPreview() {
    LunchTrayApp()
}
