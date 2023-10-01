@file:Suppress("UnnecessaryVariable", "LiftReturnOrAssignment")

package com.jvr.datovaschranka.api

import org.apache.commons.codec.binary.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*
import javax.net.ssl.HttpsURLConnection


class DsApi {
    companion object {
        //http://www.java2s.com/Code/Android/Date-Type/AdddayMonthandyeartoaDate.htm
        fun addDay(date: Date?, i: Int): Date? {
            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.DAY_OF_YEAR, i)
            return cal.time
        }

        fun addMonth(date: Date?, i: Int): Date? {
            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.MONTH, i)
            return cal.time
        }

        fun addYear(date: Date?, i: Int): Date? {
            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.YEAR, i)
            return cal.time
        }
        fun getUrl(testItem : Boolean) : String {
            var url = "ws1.mojedatovaschranka.cz"

            if (testItem) {
                url = "ws1.czebox.cz"
            }

            return url
        }

        @Throws(IOException::class)
        private fun convertInputStreamToString(ins: InputStream): String {
            val result = ByteArrayOutputStream()
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var length: Int
            while (ins.read(buffer).also { length = it } != -1) {
                result.write(buffer, 0, length)
            }
            return result.toString("UTF-8")
        }

        //@Throws(IOException::class)
        private fun convertInputStreamToString1(inStream: InputStream): String {
            val result = ByteArrayOutputStream()
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var length = 0
            while (length != -1) {
                length = inStream.read(buffer)
                result.write(buffer, 0, length)
            }

            // Java 1.1
            //return result.toString(StandardCharsets.UTF_8.name());
            return result.toString("UTF-8")

            // Java 10
            //return result.toString(StandardCharsets.UTF_8);
        }

        class Res(responseStatus : Boolean,responseText: String) {
            var responseStatus = false
            var responseText = ""

            init {
                this.responseText = responseText
                this.responseStatus = responseStatus
            }

            override fun toString(): String {
                return "code= $responseStatus"
            }
        }

        //@Throws(Exception::class)
        @Suppress("JoinDeclarationAndAssignment", "LiftReturnOrAssignment")
        fun getResponse(
            soapXml: String,
            soapAction: String?,
            requestedUrl: String,
            username: String?,
            password: String?
        ): Res {
            val url = URL(requestedUrl)
            val conn: HttpURLConnection
            if (requestedUrl.lowercase(Locale.getDefault()).startsWith("http://")) {
                conn =url.openConnection() as HttpURLConnection
            } else {
                val connection = url.openConnection() as HttpsURLConnection
                //connection.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
                //connection.setHostnameVerifier(new AllowAllHostnameVerifier());
                conn = connection
            }

            var authorization: String
            if (username != null && password != null) {
                authorization = "$username:$password"
                val encodedBytes = Base64.encodeBase64(authorization.toByteArray())
                authorization = "Basic " + String(encodedBytes, StandardCharsets.UTF_8)
                conn.setRequestProperty("Authorization",authorization) //"Basic aDYzYzZoOjVDUE9NRnRzclg4eWZlak1uS2xPOUE="
            }

            conn.setRequestProperty("connection", "close") //??
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8")
            conn.setRequestProperty(
                "SOAPAction",
                soapAction
            ) //"https://www.w3schools.com/xml/FahrenheitToCelsius"
            conn.doOutput = true
            val os = conn.outputStream
            os.write(soapXml.toByteArray())
            os.flush()
            os.close()
            val responseCode = conn.responseCode
            return if (responseCode == HttpURLConnection.HTTP_OK) {
                // read the response
                val res = conn.inputStream
                val text = convertInputStreamToString(res)
                conn.disconnect()
                //Log.d("", text)
                Res(true, text)
            } else {
                Res(false, "")
            }
        }
    }
}