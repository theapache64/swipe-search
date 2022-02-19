package com.github.theapache64.swipesearch.ui.screen.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.theapache64.swipesearch.R

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Greetings
        Text(
            text = stringResource(id = viewModel.greetingsRes),
            style = MaterialTheme.typography.h3
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Action : Click Me
        Button(onClick = {
            viewModel.onClickMeClicked()
        }) {
            Text(text = stringResource(id = R.string.action_click_me))
        }
    }
}