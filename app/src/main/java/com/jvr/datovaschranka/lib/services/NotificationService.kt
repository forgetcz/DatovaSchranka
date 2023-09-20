package com.jvr.datovaschranka.lib.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.jvr.datovaschranka.R
import com.jvr.datovaschranka.activities.MainActivity
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

// https://medium.com/@huseyinozkoc/android-services-tutorial-with-example-fa329e6a5b4b
class NotificationService: Service() {
    private var binder: IBinder? = null        // interface for clients that bind
    private var executor: ScheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(1)
    private lateinit var schedule: ScheduledFuture<*>
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    private val handler = Handler(Looper.getMainLooper())

    private val runnableCode: Runnable = Runnable {
        try {
            val time = Calendar.getInstance().time
            val current = formatter.format(time)
            Log.d("Handlers", "$current: Called on main thread")

            handler.post {
                /*Toast.makeText(
                    applicationContext,
                    "$current I poop on you",
                    Toast.LENGTH_SHORT
                ).show()*/
                sendNotification(notificationId)
            }

            /*Looper.prepare()
            Toast.makeText(applicationContext, "I poop on you", Toast.LENGTH_LONG).show()
            Looper.loop()*/
        } catch (e: Exception)
        {
            println(e.message)
        }
        // Repeat this the same runnable code block again another 2 seconds
        // 'this' is referencing the Runnable object
        //handler.postDelayed(this, 2000)
    }

    // https://github.com/ezatpanah/Notifications-Youtube/blob/master/app/src/main/java/com/ezatpanah/notificationsyoutube/MainActivity.kt
    private val channelId = "channelID"
    private val channelName = "channelName"
    private var notificationId = 0

    /*
        https://developer.android.com/develop/ui/views/notifications/build-notification
        ..on Android 8.0 and higher, you must register your app's notification channel with the system by passing an instance of...
    */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName
                , NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.BLUE
                enableLights(true)
                description = "descriptionText"
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(id : Int) {
        createNotificationChannel()

        val intent = Intent(this,MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notif = NotificationCompat.Builder(this,channelId)
            .setContentTitle("Sample Title")
            .setContentText("This is sample body notif")
            .setSmallIcon(R.drawable.ic_baseline_info_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()


        val notifManger = NotificationManagerCompat.from(this)
        notifManger.notify(id,notif)

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun addNotification() {
        createNotificationChannel()

        val builder = NotificationCompat.Builder(this,channelId)
            .setSmallIcon(android.R.mipmap.sym_def_app_icon)
            .setContentTitle("Notifications Example $notificationId")
            .setTicker("New Message Alert!")
            .setNumber(notificationId++)
            .setContentText("This is a test notification")
            .setAutoCancel(true)

        val inboxStyle = NotificationCompat.InboxStyle()

        val events = arrayOfNulls<String>(6)
        events[0] = "This is first line...."
        events[1] = "This is second line..."
        events[2] = "This is third line..."
        events[3] = "This is 4th line..."
        events[4] = "This is 5th line..."
        events[5] = "This is 6th line..."
        inboxStyle.setBigContentTitle("Big Title Details:")
        // Moves events into the big view
        events.forEach {
            inboxStyle.addLine(it)
        }

        builder.setStyle(inboxStyle)


        // attach action
        val notificationIntent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(contentIntent)

        // Add as notification - Finally, you pass the Notification object to the system by calling NotificationManager.notify() to send your notification.
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        /*val extras: Bundle? = intent!!.extras
        if (extras != null) {
            if (extras.containsKey("Par1")) {
                val val1 = extras.getString("Par1")
                println(val1)
            }
        }*/

        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        //val period: Long = 60 // the period between successive executions
        //schedule = exec.scheduleAtFixedRate(runnableCode, 0, period, TimeUnit.SECONDS)

        //run this task after 5 seconds, non block for task3
        //exec.schedule(runnableCode, 5, TimeUnit.SECONDS);

        // init Delay = 5, repeat the task every 1 second
        schedule = executor.scheduleAtFixedRate(runnableCode, 10, 60, TimeUnit.SECONDS)
        /////////////////////////////////////////////////



        /*player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        player.isLooping = true
        player.start()

        var ringtone: Ringtone? = null
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()
            ringtone = RingtoneManager.getRingtone(
                applicationContext, RingtoneManager.getActualDefaultRingtoneUri(
                    applicationContext, RingtoneManager.TYPE_ALARM
                )
            )
            ringtone.play()
            ///Thread.sleep(5000)
        } catch (ex: Exception) {
            //logger.e(context, ex)
        } finally {
            ringtone?.stop()
        }
        */

        return START_STICKY
        //return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    override fun onDestroy() {
        schedule.cancel(true)
        executor.shutdown()

        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }
}