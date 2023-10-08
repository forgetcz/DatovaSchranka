@file:Suppress("UnnecessaryVariable", "LiftReturnOrAssignment")

package com.jvr.datovaschranka.api.model.getListOfReceivedMessages

import android.text.format.DateFormat
import com.jvr.datovaschranka.api.DsApi
import com.jvr.datovaschranka.api.model.ApiEnums
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.StringReader
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import kotlin.collections.ArrayList

class GetListOfReceivedMessages {

    companion object {
        val lastMessages: MutableMap<Int, GetListOfReceivedMessagesResponseRoot?> = HashMap()
        var lastReadDate : Date = Date(0)
    }

    private var dbIDSender = ""
    private var dmAnnotation = ""
    private var messageStatus : ApiEnums.MessageStatus? = null

    private fun fromXmlString(inputXml : String) : List<GetListOfReceivedMessages> {
        val finalResultList : ArrayList<GetListOfReceivedMessages> = ArrayList()
        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val builder: DocumentBuilder = factory.newDocumentBuilder()
        val ins = InputSource(StringReader(inputXml))
        val doc: Document = builder.parse(ins)

        val xPath: XPath = XPathFactory.newInstance().newXPath()
        val dbIDCompile = xPath.compile("/Envelope/Body/GetListOfReceivedMessagesResponse/dmRecords")

        val dmRecords = dbIDCompile.evaluate(doc, XPathConstants.NODESET) as NodeList
        val n2 = dmRecords.item(0)
        val element2: Element = n2 as Element

        val lines  = element2.childNodes
        var counterRow = 0

        while (counterRow < lines.length ){
            val oneMessageItem = GetListOfReceivedMessages()
            val oneRow = lines.item(counterRow)
            val elements = oneRow.childNodes
            var counterElements = 0
            var allRequestedItemsFound = false

            while (!allRequestedItemsFound && counterElements < elements.length ) {
                val soapXmlElement = elements.item(counterElements)

                when (soapXmlElement.nodeName) {
                    "q:dbIDSender" -> {
                        oneMessageItem.dbIDSender = soapXmlElement.firstChild.nodeValue
                    }
                    "q:dmAnnotation" -> {
                        oneMessageItem.dmAnnotation = soapXmlElement.firstChild.nodeValue
                    }
                    "q:dmMessageStatus" -> {
                        oneMessageItem.messageStatus = ApiEnums.MessageStatus.fromIntString(
                            soapXmlElement.firstChild.nodeValue)
                    }
                }

                allRequestedItemsFound = oneMessageItem.dbIDSender.isNotEmpty()
                        && oneMessageItem.dmAnnotation.isNotEmpty()
                        && oneMessageItem.messageStatus != null

                counterElements++
            }
            finalResultList.add(oneMessageItem)
            counterRow++
        }
        return finalResultList
    }

    fun getListOfReceivedMessages(userId : Int, userName: String, password : String, testItem : Boolean
                                  , fromDate : Date, toDate: Date, offset : Int = 1
                                  , limit : Int = 100)
            : GetListOfReceivedMessagesResponseRoot? {

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
        val container = GetListOfReceivedMessagesResponseRoot()
            .deserialize<GetListOfReceivedMessagesResponseRoot>(GetListOfReceivedMessagesResponseRoot.example)
        println(container)

        val url = "https://${DsApi.getUrl(testItem)}/DS/dx"
        val response = DsApi.getResponse(
            soapXmlString, "", url, userName, password
        )

        if (response.responseStatus) {
            lastReadDate = Date()
            val container = GetListOfReceivedMessagesResponseRoot().deserialize<GetListOfReceivedMessagesResponseRoot>(response.responseText)
            lastMessages[userId] = container
            return lastMessages[userId]
        } else {
            return null
        }
    }

    override fun toString(): String {
        return "$dbIDSender:dbIDSender, $dmAnnotation : dmAnnotation"
    }
}