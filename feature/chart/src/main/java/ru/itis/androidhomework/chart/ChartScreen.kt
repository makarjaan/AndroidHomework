package ru.itis.androidhomework.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import ru.itis.androidhomework.base.R
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext

@Composable
fun ChartScreen(modifier: Modifier = Modifier) {

    val viewModel: ChartViewModel = hiltViewModel()
    val chartUiState by viewModel.uiState.collectAsState()

    val secondFieldFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ChatEffect.ShowError -> {
                    snackbarHostState.showSnackbar(message = context.getString(R.string.error_check))
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        OutlinedTextField(
            value = chartUiState.count,
            onValueChange = { viewModel.reduce(ChartScreenEvent.CountInput(it)) },
            label = { Text(text = stringResource(R.string.pointCount)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    secondFieldFocusRequester.requestFocus()
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.background,
                unfocusedTextColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = chartUiState.counts,
            onValueChange = { viewModel.reduce(ChartScreenEvent.InputCounts(it)) },
            label = { Text(text = stringResource(R.string.points)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.reduce(ChartScreenEvent.SubmitData)
                    focusManager.clearFocus()
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.background,
                unfocusedTextColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(secondFieldFocusRequester)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.reduce(ChartScreenEvent.SubmitData)
            focusManager.clearFocus()
        }) {
            Text(stringResource(R.string.chart_draw))
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (chartUiState.isValid) {
            Graph(
                data = chartUiState.graphData,
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                snackbar = { data ->
                    Snackbar(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                        snackbarData = data,
                    )
                }
            )
        }
    }
}

@Composable
fun Graph(data: List<Float>){

    val primaryContainerColor = MaterialTheme.colorScheme.primaryContainer
    val primaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
    ) {
        val canvasHeight = size.height
        val canvasWidth = size.width
        val maxValue = data.maxOrNull() ?: 1f

        val spacingX = canvasWidth / (data.size - 1)
        val spacingY = canvasHeight / data.size

        for (i in 0..data.size) {
            val y = i * spacingY
            drawLine(
                color = Color.LightGray,
                start = Offset(0f, y),
                end = Offset(canvasWidth, y),
                strokeWidth = 1f
            )
        }

        for (i in data.indices) {
            val x = i * spacingX
            drawLine(
                color = Color.LightGray,
                start = Offset(x, 0f),
                end = Offset(x, canvasHeight),
                strokeWidth = 1f
            )
        }

        val points = data.mapIndexed { index, value ->
            Offset(
                x = index * spacingX,
                y = canvasHeight - (value / maxValue) * canvasHeight
            )
        }

        val gradientPath = Path().apply {
            moveTo(points.first().x, canvasHeight)
            for (point in points) {
                lineTo(point.x, point.y)
            }
            lineTo(points.last().x, canvasHeight)
            close()
        }


        drawPath(
            path = gradientPath,
            brush = Brush.verticalGradient(
                colors = listOf(primaryColor.copy(alpha = 0.4f), Color.Transparent),
                startY = 0f,
                endY = canvasHeight
            )
        )

        for (i in 0 until points.size - 1) {
            drawLine(
                color = primaryColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 4f
            )
        }

        points.forEach { point ->
            drawCircle(
                color = primaryContainerColor,
                radius = 16f,
                center = point
            )
        }
    }
}

