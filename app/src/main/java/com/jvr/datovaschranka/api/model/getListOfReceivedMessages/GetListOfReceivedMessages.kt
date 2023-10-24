@file:Suppress("UnnecessaryVariable", "LiftReturnOrAssignment")

package com.jvr.datovaschranka.api.model.getListOfReceivedMessages

import android.text.format.DateFormat
import com.jvr.datovaschranka.api.DsApi
import java.util.*
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class GetListOfReceivedMessages {

    companion object {
        private val reentrantLock: Lock = ReentrantLock()
        val lastMessages: MutableMap<Int, Pair<Date,GetListOfReceivedMessagesResponseRoot>> = HashMap()
    }

    fun getListOfReceivedMessages(userId : Int, userName: String, password : String, testItem : Boolean
                                  , fromDate : Date, toDate: Date, offset : Int = 1
                                  , limit : Int = 100)
            : Pair<Date,GetListOfReceivedMessagesResponseRoot>? {

        reentrantLock.lock()

        val soapXmlString = StringBuilder().run {
            appendLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
            appendLine("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">")
            appendLine("    <soap:Body>")
            appendLine("        <GetListOfReceivedMessages xmlns=\"http://isds.czechpoint.cz/v20\">")
            appendLine("            <dmFromTime>${DateFormat.format("yyyy-MM-dd", fromDate)}T00:00:00</dmFromTime>")
            appendLine("            <dmToTime>${DateFormat.format("yyyy-MM-dd", toDate)}T23:59:59</dmToTime>")
            appendLine("            <dmRecipientOrgUnitNum xsi:nil=\"true\" />")
            appendLine("            <dmStatusFilter>-1</dmStatusFilter>")
            appendLine("            <dmOffset>${offset}</dmOffset>")
            appendLine("            <dmLimit>${limit}</dmLimit>")
            appendLine("         </GetListOfReceivedMessages>")
            appendLine("    </soap:Body>")
            appendLine("</soap:Envelope>")
            toString()
        }
        /*val container = GetListOfReceivedMessagesResponseRoot()
            .deserialize<GetListOfReceivedMessagesResponseRoot>(GetListOfReceivedMessagesResponseRoot.example)
        println(container)*/

        val url = "https://${DsApi.getUrl(testItem)}/DS/dx"
        val response = DsApi.getResponse(
            soapXmlString, "", url, userName, password
        )

        if (response.responseStatus) {
            val container = GetListOfReceivedMessagesResponseRoot().deserialize<GetListOfReceivedMessagesResponseRoot>(response.responseText)
            lastMessages[userId] = Pair(Date(),container)
            reentrantLock.unlock();
            return lastMessages[userId]
        } else {
            reentrantLock.unlock();
            return null
        }
    }
}