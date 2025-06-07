package ru.itis.androidhomework

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.setCustomKeys
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.remoteconfig.remoteConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.itis.androidhomework.databinding.ActivityMainBinding
import ru.itis.androidhomework.navigation.Nav
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Nav.Provider {

    @Inject
    lateinit var nav: Nav

    @Inject
    lateinit var permissionHandler: PermissionHandler

    @Inject
    lateinit var notificationHandler: NotificationHandler


    private val containerId: Int = R.id.fragmentContainer
    private var viewBinding: ActivityMainBinding? = null
    private var navController: NavController? = null


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding!!.root)
        setupNavigation()

        intent?.let {
            val xid = intent.getStringExtra("xid")
            if (xid!= null){
                val bundle = Bundle().apply {
                    putString("xid", xid)
                }
                navController?.navigate(R.id.action_global_feature_details_page, bundle)
                intent.removeExtra("xid")
            }
        }

        notificationHandler.createNotificationChannel()
        permissionHandler.requestWithRationaleIfNeeded(
            activity = this,
            permission = android.Manifest.permission.POST_NOTIFICATIONS,
            rationaleTitle = getString(R.string.permission_notif),
            rationaleMessage = getString(R.string.permission_dialog),
            onGranted = {
                Toast.makeText(this, getString(R.string.vkl_notifications), Toast.LENGTH_SHORT).show()
            },
            onDenied = {
                Toast.makeText(this, getString(R.string.vykl_norifications), Toast.LENGTH_SHORT).show()
            }
        )

        Firebase.crashlytics.setCustomKeys{
            val id = UUID.randomUUID()
            key("userId","$id")
        }


        val config = Firebase.remoteConfig
        config.fetchAndActivate().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("TEST-TAG", "Config params updated")
            } else {
                Log.w("TEST-TAF", "Failed to fetch remote config")
            }
        }

    }


    private fun setupNavigation() {
        if (navController == null) {
            val navHost = supportFragmentManager.findFragmentById(containerId) as NavHostFragment
            navController = navHost.navController
        }
        nav.setNavProvider(navProvider = this)
    }

    override fun getNavController(): NavController? {
        return navController
    }


    override fun onDestroy() {
        super.onDestroy()
        if (this::nav.isInitialized) {
            nav.clearNavProvider(navProvider = this)
        }
    }
}

