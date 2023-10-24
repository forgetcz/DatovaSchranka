package com.jvr.datovaschranka.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.jvr.common.lib.async.RunCommandAsyncKotlin
import com.jvr.datovaschranka.activities.MainActivity
import com.jvr.datovaschranka.api.DsApi
import com.jvr.datovaschranka.api.model.getListOfReceivedMessages.GetListOfReceivedMessages
import com.jvr.datovaschranka.api.model.getListOfSentMessages.GetListOfSentMessages
import com.jvr.datovaschranka.dbhelper.DbHelper
import com.jvr.datovaschranka.dbhelper.tableModel.v1.NamePasswordTable
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

// https://medium.com/@huseyinozkoc/android-services-tutorial-with-example-fa329e6a5b4b
class NotificationService: Service() {
    private var binder: IBinder? = null        // interface for clients that bind
    private var executor: ScheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(1)
    private var schedule: ScheduledFuture<*>? = null
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    private lateinit var dbHelper: DbHelper
    //private val handler = Handler(Looper.getMainLooper())

    // https://github.com/ezatpanah/Notifications-Youtube/blob/master/app/src/main/java/com/ezatpanah/notificationsyoutube/MainActivity.kt
    private val channelId = "channelID"
    private val channelName = "channelName"
    private var notificationId = 0

    private val runnableCode: Runnable = Runnable {
        try {
            //val time = Calendar.getInstance().time
            //val current = formatter.format(time)

            RunCommandAsyncKotlin<AppCompatActivity, Any, Int>(applicationContext, null
                ,{
                    val users = dbHelper.getUserTable.selectAll()
                    users?.forEach {  userItem ->
                        if (userItem.active) {
                            val namePassTableItem = dbHelper.getNamePasswordTable
                                .select(NamePasswordTable.COLUMN_FK_USER_ID+ "=" + userItem._id)
                                ?.first()
                            if (namePassTableItem != null) {
                                val user = namePassTableItem.userName
                                val pass = namePassTableItem.userPassword
                                GetListOfReceivedMessages().getListOfReceivedMessages(
                                    userItem._id!!, user, pass, userItem.testItem
                                    , DsApi.addDay(Date(), -160)!!, Date())
                                GetListOfSentMessages().getListOfSentMessages(userItem._id!!, user, pass
                                    , userItem.testItem, DsApi.addDay(Date(), -160)!!, Date())}
                        }
                    }
                    /*Log.d("Handlers", "$time ($current): Called on main thread - start")
                    Thread.sleep(20000);
                    Log.d("Handlers", "$time ($current): Called on main thread - end")*/
                    return@RunCommandAsyncKotlin
                }, null).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            //sendNotification(notificationId)
            /*handler.post {
                /*Toast.makeText(
                    applicationContext,
                    "$current I poop on you",
                    Toast.LENGTH_SHORT
                ).show()*/
                Log.d("Handlers", "$time ($current): Called on main thread - start")
                Thread.sleep(20000);
                Log.d("Handlers", "$time ($current): Called on main thread - end")
                //sendNotification(notificationId)
            }*/

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

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notif = NotificationCompat.Builder(this,channelId)
            .setContentTitle("Sample Title - setContentTitle")
            .setContentText("This is sample body notification - setContentText")
            .setTicker("New Message Alert! - setTicker")
            .setSmallIcon(androidx.loader.R.drawable.notification_bg)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setNumber(notificationId++)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
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
            .setContentText("This is a test notification")
            .setTicker("New Message Alert!")
            .setNumber(notificationId++)
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

    override fun onCreate() {
        super.onCreate()
        dbHelper = DbHelper(this, null)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val extras: Bundle? = intent?.extras
        if (extras != null) {
            if (extras.containsKey("Par1")) {
                val val1 = extras.getString("Par1")
                if (val1 != null){
                    Log.d("",val1.toString())
                }
            }
        }

        // Scheduling the first task which will execute after 0 seconds and then repeats periodically with
        // a period of 3600 seconds https://www.geeksforgeeks.org/scheduledthreadpoolexecutor-class-in-java/
        schedule = executor.scheduleAtFixedRate(runnableCode, 0, 3600,TimeUnit.SECONDS);
        return START_STICKY

        //runnableCode.run()
        //sendNotification(notificationId)
        //Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        //run this task after 5 seconds, non block for task3
        //exec.schedule(runnableCode, 5, TimeUnit.SECONDS);

        // init Delay = 5, repeat the task every 1 second
        //schedule = executor.scheduleAtFixedRate(runnableCode, 10, 60, TimeUnit.SECONDS)
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


        //return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    override fun onDestroy() {
        schedule?.cancel(true)
        executor.shutdown()

        Toast.makeText(this, "Service done", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }
}