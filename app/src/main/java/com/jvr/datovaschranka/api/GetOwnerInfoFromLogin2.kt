@file:Suppress("LiftReturnOrAssignment", "UnnecessaryVariable")

package com.jvr.datovaschranka.api

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class GetOwnerInfoFromLogin2 {
    fun getOwnerInfoFromLogin2(userName: String, password : String, testItem : Boolean)
            : GetOwnerInfoFromLogin2Response? {
        val soapXmlString = StringBuilder().run {
            appendLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
            appendLine("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">")
            appendLine("    <soap:Body>")
            appendLine("        <GetOwnerInfoFromLogin2 xmlns=\"http://isds.czechpoint.cz/v20\">")
            appendLine("            <dbDummy />")
            appendLine("         </GetOwnerInfoFromLogin2>")
            appendLine("    </soap:Body>")
            appendLine("</soap:Envelope>")
            toString()
        }

        val url = "https://${DsApi.getUrl(testItem)}/DS/DsManage"
        val response = DsApi.getResponse(
            soapXmlString, "", url, userName, password
        )
        if (response.responseStatus) {
            val cls = GetOwnerInfoFromLogin2Response.fromXmlString(response.responseText)
            return cls
        } else {
            return null
        }
    }

    class GetOwnerInfoFromLogin2Response {
        companion object {
            fun fromXmlString(inputXml : String) : GetOwnerInfoFromLogin2Response {
                val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                val builder: DocumentBuilder = factory.newDocumentBuilder()
                val ins = InputSource(StringReader(inputXml))
                val doc: Document = builder.parse(ins)

                val xPath: XPath = XPathFactory.newInstance().newXPath()
                val dbIDPath = "/Envelope/Body/GetOwnerInfoFromLogin2Response/dbOwnerInfo/dbID"
                val dbIDCompile = xPath.compile(dbIDPath)

                val eval = dbIDCompile.evaluate(doc, XPathConstants.NODESET) as NodeList
                val n2 = eval.item(0)
                val element2: Element = n2 as Element

                val c = GetOwnerInfoFromLogin2Response()
                c.DbID = element2.textContent
                return c
            }
        }

        private var DbID = ""
        private var PnGivenNames = ""
        private var PnLastName = ""

        var dbID: String
            get() = dbID
            set(value) {
                dbID = value
            }
    }
}