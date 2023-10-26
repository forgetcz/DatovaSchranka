package com.jvr.common.lib.logger

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.jvr.common.BuildConfig
import com.jvr.common.Version
import com.jvr.common.contracts.BaseActivityClass
import com.jvr.common.contracts.ILogger
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection


class RestLogger(Url:String): ILogger {
    private var url: String = Url

    private val logger: ComplexLogger = ComplexLogger(
        this.javaClass.name, mutableListOf(
            BasicLogger(), HistoryLogger() //er()
            //, new RestApiLogger()
        )
    )

    @Throws(JSONException::class, PackageManager.NameNotFoundException::class)
    private fun getBasicInfo(context: Context?): JSONObject {
        val messageObject = JSONObject()
        messageObject.put("appName", BuildConfig.LIBRARY_PACKAGE_NAME)
        if (context != null) {
            val pInfo = context.packageManager.getPackageInfo(
                context.packageName, 0
            )

            messageObject.put("VERSION_NAME", pInfo.versionName)

            val sysInfo = JSONObject()
            messageObject.put(
                "android_id", Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            )
            messageObject.put("sysInfo", sysInfo)

            val packageName = context.packageName
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            val appInfo = context.applicationInfo
            messageObject.put("versionName", pi.versionName)
            messageObject.put("versionCode", pi.versionCode)
            messageObject.put("getPackageName", packageName)
            messageObject.put("appInfo", appInfo.toString())
        }

        val version = JSONObject()
        val programVersion = Version()
        version.put("Major", " : " + programVersion.major)
        version.put("Minor", " : " + programVersion.minor)
        version.put("Build", " : " + programVersion.build)
        version.put("Revision", " : " + programVersion.revision)
        messageObject.put("Version", version)

        val buildInfo = JSONObject()
        buildInfo.put("MODEL", Build.MODEL)
        buildInfo.put("MANUFACTURER", Build.MANUFACTURER)
        buildInfo.put("DEVICE", Build.DEVICE)
        buildInfo.put("CODENAME", Build.VERSION.CODENAME)
        messageObject.put("BuildInfo", buildInfo)

        return messageObject
    }

    private fun restApiLog(tag : String, ex: Exception?, context: Context?){
        val httpEndpoint = URL(url)
        val myConnection = httpEndpoint.openConnection() as HttpsURLConnection
        myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1")
        myConnection.setRequestProperty("Content-Type", "application/json")
        myConnection.requestMethod = "POST"
        myConnection.doOutput = true

        val messageObject: JSONObject = getBasicInfo(context)
        messageObject.put("tag", tag)
        messageObject.put("message", ex?.message)
        messageObject.put("stack", Arrays.toString(ex?.stackTrace))
        //messageObject.put("logType",lt);
        //messageObject.put("logType",lt);
        val jsonMessage = messageObject.toString()
        myConnection.outputStream.write(jsonMessage.toByteArray())
        val response = myConnection.responseCode
        if (response == 200) {
            Log.d("${getTag()}:${::restApiLog.name}", "response == 200")
        } else {
            val s = Integer.toString(myConnection.responseCode)
            Log.e("${getTag()}:${::restApiLog.name}", "response != 200 -> ${response}:${s}")
        }
    }

    override fun d(message: String) {
        Log.d(getTag(), ILogger.errMessageNotTargeted)
    }
    override fun d(tag: String, message: String) {
        Log.d(tag, ILogger.errMessageNotTargeted)
    }
    override fun d(context: BaseActivityClass, message: String) {
        restApiLog(context.getTag(), Exception(message), context)
    }

    override fun i(message: String) {
        Log.d(getTag(), ILogger.errMessageNotTargeted)
    }
    override fun i(tag: String, message: String) {
        Log.d(getTag(), ILogger.errMessageNotTargeted)
    }
    override fun i(context: BaseActivityClass, message: String) {
        Log.d(getTag(), ILogger.errMessageNotTargeted)
    }

    override fun w(message: String) {
        Log.d(getTag(), ILogger.errMessageNotTargeted)
    }
    override fun w(tag: String, message: String) {
        Log.d(tag, ILogger.errMessageNotTargeted)
    }
    override fun w(context: BaseActivityClass, message: String) {
        Log.d(getTag(), ILogger.errMessageNotTargeted)
    }

    override fun e(message: String) {
        Log.d(getTag(), ILogger.errMessageNoContext)
    }
    override fun e(context: BaseActivityClass, message: String) {
        restApiLog(context.getTag(), Exception(message), context)
    }
    override fun e(context: BaseActivityClass, message: Exception) {
        restApiLog(context.getTag(), Exception(message), context)
    }
    override fun e(tag: String, message: Exception) {
        Log.d(tag, ILogger.errMessageNoContext)
    }
    override fun e(message: Exception) {
        Log.d(getTag(), ILogger.errMessageNoContext)
    }
}