package com.manmanadmin.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.manmanadmin.R
import com.manmanadmin.main_activity.MainActivity

import java.security.SecureRandom

class MessagingService : FirebaseMessagingService(){

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.data.isNotEmpty()) {

            val isToReview = remoteMessage.data["isToReview"]
            val body = remoteMessage.data["body"]
            val title = remoteMessage.data["title"]


                sendNotification(body.toString(), isToReview, title)

        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {

        }

    }
    override fun onNewToken(token: String) {

        FirebaseAuth.getInstance().currentUser?.uid?.let {
            sendRegistrationToServer(token, it)
        }
    }



    private fun sendNotification(messageBody: String, isToReview: String?, title: String?) {



        val pendingIntent = getPendingIntent(isToReview, messageBody)

        val channelId = getString(R.string.default_notification_channel_id)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Manchas Delivery Admin Notifications",
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = notificationBuilder.build()


        notificationManager.notify(createRandomCode(3), notification)
    }

    private fun getPendingIntent(isToReview: String?, orderId: String): PendingIntent? {
        val intent = if(isToReview == "is to review"){
            Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }

        }else{
         Intent(Intent.ACTION_VIEW).apply {
                val url = "app://deleted/$orderId"
                data = Uri.parse(url)
            }
        }

        return PendingIntent.getActivity(this, createRandomCode(3), intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
    }



    fun createRandomCode(codeLength: Int): Int {
        val chars = "1234567890".toCharArray()
        val sb = StringBuilder()
        val random = SecureRandom()
        for (i in 0 until codeLength) {
            val c = chars[random.nextInt(chars.size)]
            sb.append(c)
        }
        return sb.toString().toInt()
    }

    companion object {

        private const val TAG = "My_FirebaseMsgService"
    }
}