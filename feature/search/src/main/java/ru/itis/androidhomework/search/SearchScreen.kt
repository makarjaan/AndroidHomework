package ru.itis.androidhomework.search

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.androidhomework.domain.utils.Constants
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.itis.androidhomework.domain.model.DataSource
import ru.itis.androidhomework.domain.model.FeaturesModel
import ru.itis.androidhomework.base.R as baseR



@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
) {

    val viewModel: SearchViewModel = hiltViewModel()
    val searchUiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is SearchEffect.ShowToast -> {
                    val message = when (effect.source) {
                        DataSource.API -> baseR.string.message_api
                        DataSource.CACHE -> baseR.string.message_cash
                    }
                   Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                is SearchEffect.ShowError -> {
                    val errorMessage = effect.throwable.message ?: context.getString(baseR.string.error_unknown)
                    MaterialAlertDialogBuilder(context)
                        .setTitle(context.getString(baseR.string.error_title))
                        .setMessage(errorMessage)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                }

                is SearchEffect.ShowSnackbar -> {
                    val message = "Функциональность временно недоступна"
                    snackbarHostState.showSnackbar(
                        message = message)
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchUiState.input,
                onValueChange = { viewModel.reduce(SearchScreenEvent.UserInput(input = it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(baseR.string.search)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                },
                trailingIcon = {
                    if (searchUiState.input.isNotEmpty()) {
                        IconButton(onClick = { viewModel.reduce(SearchScreenEvent.UserInput(input = Constants.EMPTY_STRING)) }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primaryContainer
                            )
                        }
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedTextColor = MaterialTheme.colorScheme.background,
                    unfocusedTextColor = MaterialTheme.colorScheme.outline
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.reduce(SearchScreenEvent.GetList)
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.reduce(SearchScreenEvent.OnButtonChartClick) }
            ) {
                Text(stringResource(baseR.string.chart_btn), style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (!searchUiState.isLoading && searchUiState.isDataLoaded && searchUiState.list.isEmpty()) {
                Text(
                    text = stringResource(baseR.string.empty_list_message),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .wrapContentHeight()
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
            }

            if (searchUiState.list.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
                ) {
                    items(searchUiState.list) { feature ->
                        FeatureListItem(
                            feature = feature,
                            onClick = { viewModel.reduce(SearchScreenEvent.OnItemClick(feature.xid)) },
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

            }
        }

        if (searchUiState.isActionInProgress) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                snackbar = { data ->
                    Snackbar(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                        actionColor = MaterialTheme.colorScheme.onError,
                        snackbarData = data
                    )
                }
            )
        }
    }
}



@Composable
fun FeatureListItem(
    feature: FeaturesModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = feature.name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(id = baseR.string.rating, feature.rate),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 1
            )
        }
    }
}