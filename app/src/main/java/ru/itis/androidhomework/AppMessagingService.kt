package ru.itis.androidhomework

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.itis.androidhomework.domain.usecase.CheckAuthUseCase
import ru.itis.androidhomework.domain.usecase.SavePushEventUseCase
import javax.inject.Inject


class AppMessagingService: FirebaseMessagingService() {

    @Inject
    lateinit var notificationHandler: NotificationHandler

    @Inject
    lateinit var savePushEventUseCase: SavePushEventUseCase

    @Inject
    lateinit var checkAuthUseCase: CheckAuthUseCase

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TEST-TAG", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val data = message.data
        val category = data["category"] ?: return

        when (category) {
            "first" -> {
                val title = data["title"]
                val messageText = data["message"]
                notificationHandler.showHighPriorityNotification(title, messageText)
            }

            "second" -> {
                val event = data["event"] ?: "unknown"
                val city = data["city"] ?: "undefined"

                scope.launch {
                    savePushEventUseCase(event, city)
                }
            }

            "third" -> {
                if (isAppInForeground()){
                    val xid = message.data["xid"] ?: return
                    scope.launch {
                        val authorized = checkAuthUseCase.invoke()

                        withContext(Dispatchers.Main) {
                            if (!authorized) {
                                Toast.makeText(
                                    applicationContext,
                                    applicationContext.getString(R.string.sorry),
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@withContext
                            }

                            val intent =
                                Intent(applicationContext, MainActivity::class.java).apply {
                                    flags =
                                        Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    putExtra("xid", xid)
                                }
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun isAppInForeground(): Boolean {
        val appProcessInfo = ActivityManager.RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(appProcessInfo)
        return appProcessInfo.importance == IMPORTANCE_FOREGROUND
    }

}