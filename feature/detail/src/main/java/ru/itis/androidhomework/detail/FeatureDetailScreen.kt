package ru.itis.androidhomework.detail

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import ru.itis.androidhomework.base.R as baseR
import androidx.core.net.toUri
import com.google.android.material.dialog.MaterialAlertDialogBuilder


@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current
    val viewModel: FeatureDetailViewModel = hiltViewModel()
    val detailUiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is DetailEffect.ShowError -> {
                    val errorMessage = effect.throwable.message ?: context.getString(baseR.string.error_unknown)
                    MaterialAlertDialogBuilder(context)
                        .setTitle(context.getString(baseR.string.error_title))
                        .setMessage(errorMessage)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .systemBarsPadding()
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = if (detailUiState.image.isBlank()) {
                    ImageRequest.Builder(context)
                        .data(R.drawable.sample_image)
                        .build()
                } else {
                    ImageRequest.Builder(context)
                        .data(detailUiState.image)
                        .crossfade(true)
                        .scale(Scale.FILL)
                        .build()
                }
            ),
            contentDescription = context.getString(baseR.string.place_image),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))


        Text(
            text = detailUiState.placeName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primaryContainer
        )


        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = detailUiState.placeAddress,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = detailUiState.desc,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        WikiLink(detailUiState.wikilink)
    }
}

@Composable
fun WikiLink(url: String) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {}
    )

    Text(
        text = stringResource(baseR.string.hyperlink),
        modifier = Modifier
            .clickable {
                launcher.launch(Intent(Intent.ACTION_VIEW, url.toUri()))
            },
        color = MaterialTheme.colorScheme.primaryContainer,
        style = MaterialTheme.typography.headlineSmall
    )
}