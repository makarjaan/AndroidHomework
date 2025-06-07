package ru.itis.androidhomework

import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import javax.inject.Inject


class PermissionHandler @Inject constructor() {

    private var launcher: ActivityResultLauncher<String>? = null

    fun requestWithRationaleIfNeeded(
        activity: AppCompatActivity,
        permission: String,
        rationaleTitle: String,
        rationaleMessage: String,
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ) {

        val currentLauncher = launcher ?: activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) onGranted() else onDenied()
        }.also {
            launcher = it
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isAlreadyDeniedOnce = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

            if (isAlreadyDeniedOnce) {
                AlertDialog.Builder(activity)
                    .setTitle(rationaleTitle)
                    .setMessage(rationaleMessage)
                    .setPositiveButton(activity.getString(R.string.permission_ok)) { _, _ ->
                        currentLauncher.launch(permission)
                    }
                    .setNegativeButton(activity.getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                        onDenied()
                    }
                    .setCancelable(false)
                    .show()
            } else {
                currentLauncher.launch(permission)
            }
        } else {
            onGranted()
        }
    }
}

