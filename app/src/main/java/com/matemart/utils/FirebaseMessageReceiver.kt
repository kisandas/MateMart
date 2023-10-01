package com.matemart.utils


import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.matemart.R
import com.matemart.activities.SplashActivity



class FirebaseMessageReceiver : FirebaseMessagingService() {


	companion object{
		var FCMToken = ""
	}
	var title =""
	var content =""
	var imageUrl =""

	override fun onNewToken(token: String) {
		super.onNewToken(token)
		FCMToken = token
		Log.e("cccccccccc", "onNewToke----n: "+token )

		if(token.isNotEmpty()){
			Log.e("cccccccccc", "onNewToken: "+token )
			SharedPrefHelper.getInstance(MyApplication.getInstance())
				.write(SharedPrefHelper.FIREBASE_TOKEN, token)
		}
	}




	override fun onMessageReceived(remoteMessage: RemoteMessage) {

		getImage(remoteMessage);

	}

	private fun getImage(remoteMessage: RemoteMessage) {
		val data = remoteMessage.notification

		title = data?.title.toString()
		content = data?.body.toString()
		imageUrl = data?.imageUrl.toString()
		//Create thread to fetch image from notification
		if (data != null) {
			val uiHandler = Handler(Looper.getMainLooper())
			uiHandler.post(Runnable { // Get image from data Notification
				Glide.with(this)
					.asBitmap()
					.load(imageUrl)
					.into(object : CustomTarget<Bitmap>(){
						override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
							sendNotification(resource)
						}
						override fun onLoadCleared(placeholder: Drawable?) {
							sendNotification()
							// this is called when imageView is cleared on lifecycle call or for
							// some other reason.
							// if you are referencing the bitmap somewhere else too other than this imageView
							// clear it here as you can no longer have the bitmap
						}
					})
			})
		}
	}

	private fun sendNotification(bitmap: Bitmap) {
		val style: NotificationCompat.BigPictureStyle = NotificationCompat.BigPictureStyle()
		style.bigPicture(bitmap)
		val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
		val intent = Intent(applicationContext, SplashActivity::class.java)
		intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
		val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
		val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
		val NOTIFICATION_CHANNEL_ID = "101"
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			@SuppressLint("WrongConstant") val notificationChannel = NotificationChannel(
				NOTIFICATION_CHANNEL_ID,
				"Notification",
				NotificationManager.IMPORTANCE_MAX
			)

			//Configure Notification Channel
			notificationChannel.description = "Game Notifications"
			notificationChannel.enableLights(true)
			notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
			notificationChannel.enableVibration(true)
			notificationManager.createNotificationChannel(notificationChannel)
		}
		val notificationBuilder: NotificationCompat.Builder? = NotificationCompat.Builder(
			this,
			NOTIFICATION_CHANNEL_ID
		)
			.setSmallIcon(R.mipmap.ic_launcher)
			.setContentTitle(title)
			.setAutoCancel(true)
			.setSound(defaultSound)
			.setContentText(content)
			.setContentIntent(pendingIntent)
			.setStyle(style)
			.setLargeIcon(bitmap)
			.setWhen(System.currentTimeMillis())
			.setPriority(Notification.PRIORITY_MAX)
		notificationManager.notify(1, notificationBuilder?.build())
	}

	private fun sendNotification() {

		val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
		val intent = Intent(applicationContext, SplashActivity::class.java)
		intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
		val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
		val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
		val NOTIFICATION_CHANNEL_ID = "101"
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			@SuppressLint("WrongConstant") val notificationChannel = NotificationChannel(
				NOTIFICATION_CHANNEL_ID,
				"Notification",
				NotificationManager.IMPORTANCE_MAX
			)

			//Configure Notification Channel
			notificationChannel.description = "Game Notifications"
			notificationChannel.enableLights(true)
			notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
			notificationChannel.enableVibration(true)
			notificationManager.createNotificationChannel(notificationChannel)
		}
		val notificationBuilder: NotificationCompat.Builder? = NotificationCompat.Builder(
			this,
			NOTIFICATION_CHANNEL_ID
		)
			.setSmallIcon(R.mipmap.ic_launcher)
			.setContentTitle(title)
			.setAutoCancel(true)
			.setSound(defaultSound)
			.setContentText(content)
			.setContentIntent(pendingIntent)
			.setWhen(System.currentTimeMillis())
			.setPriority(Notification.PRIORITY_MAX)
		notificationManager.notify(1, notificationBuilder?.build())
	}

}
