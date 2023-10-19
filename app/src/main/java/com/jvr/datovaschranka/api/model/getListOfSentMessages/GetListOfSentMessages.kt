package com.jvr.datovaschranka.api.model.getListOfSentMessages

import android.text.format.DateFormat
import com.jvr.datovaschranka.api.DsApi
import com.jvr.datovaschranka.api.model.getListOfReceivedMessages.GetListOfReceivedMessages
import com.jvr.datovaschranka.api.model.getListOfReceivedMessages.GetListOfReceivedMessagesResponseRoot
import java.util.*

class GetListOfSentMessages {

    companion object {
        val lastMessages: MutableMap<Int, Pair<Date, GetListOfSentMessagesResponseRoot?>> = HashMap()
    }

    fun getListOfSentMessages(userId : Int, userName: String, password : String, testItem : Boolean
                                  , fromDate : Date, toDate: Date, offset : Int = 1
                                  , limit : Int = 100)
            : Pair<Date, GetListOfSentMessagesResponseRoot?>? {

        val soapXmlString = StringBuilder().run {
            appendLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
            appendLine("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">")
            appendLine("    <soap:Body>")
            appendLine("        <GetListOfSentMessages xmlns=\"http://isds.czechpoint.cz/v20\">")
            appendLine("            <dmFromTime>${DateFormat.format("yyyy-MM-dd", fromDate)}T00:00:00</dmFromTime>")
            appendLine("            <dmToTime>${DateFormat.format("yyyy-MM-dd", toDate)}T23:59:59</dmToTime>")
            appendLine("            <dmSenderOrgUnitNum xsi:nil=\"true\" />")
            appendLine("            <dmOffset>${offset}</dmOffset>")
            appendLine("            <dmLimit>${limit}</dmLimit>")
            appendLine("        </GetListOfSentMessages>")
            appendLine("    </soap:Body>")
            appendLine("</soap:Envelope>")
            toString()
        }

        val url = "https://${DsApi.getUrl(testItem)}/DS/dx"
        val response = DsApi.getResponse(
            soapXmlString, "", url, userName, password
        )

        if (response.responseStatus) {
            val container = GetListOfSentMessagesResponseRoot().deserialize<GetListOfSentMessagesResponseRoot>(response.responseText)
            lastMessages[userId] = Pair(Date(),container)
            return lastMessages[userId]
        } else {
            return null
        }
    }
}