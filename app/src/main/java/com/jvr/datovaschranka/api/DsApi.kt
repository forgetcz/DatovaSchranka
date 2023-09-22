@file:Suppress("UnnecessaryVariable", "LiftReturnOrAssignment")

package com.jvr.datovaschranka.api

import android.text.format.DateFormat
import org.apache.commons.codec.binary.Base64
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*
import javax.net.ssl.HttpsURLConnection
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import kotlin.collections.ArrayList


class DsApi {
    companion object {
        private fun getUrl(testItem : Boolean) : String {
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

        private class Res(responseStatus : Boolean,responseText: String) {
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
        private fun getResponse(
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

            var authorization = ""
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

        /*fun getOwnerInfoFromLogin2Response(userName: String, password : String, testItem : Boolean): String {
            val soapXmlString = StringBuilder().run {
                appendLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
                appendLine("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">")
                appendLine("    <soap:Body>")
                appendLine("        <GetOwnerInfoFromLogin2Response xmlns=\"http://isds.czechpoint.cz/v20\">")
                appendLine("            <dbDummy />")
                appendLine("         </GetOwnerInfoFromLogin2Response>")
                appendLine("    </soap:Body>")
                appendLine("</soap:Envelope>")
                toString()
            }

            val response = getResponse(soapXmlString, ""
                ,"https://${getUrl(testItem)}/DS/DsManage", userName, password)
            Log.d("", response.toString())
            return response.toString()
        }

        fun  findDataBox2(userName: String, password : String, firmName : String, testItem : Boolean
                          ,  dbType: ApiDbType): String {
            val soapXmlString = StringBuilder().run {
                appendLine("""<?xml version="1.0" encoding="utf-8"?>
                    <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
                        <soap:Body>
                            <FindDataBox2 xmlns="http://isds.czechpoint.cz/v20">
                                <dbOwnerInfo>
                                    <dbID />
                                    <dbType>${dbType}</dbType>
                                    <ic />
                                    <pnGivenNames />
                                    <pnLastName />
                                    <firmName>${firmName}</firmName>
                                    <biDate xsi:nil="true" />
                                    <biCity />
                                    <biCounty />
                                    <biState />
                                    <adCode xsi:nil="true" />
                                    <adCity />
                                    <adDistrict xsi:nil="true" />
                                    <adStreet />
                                    <adNumberInStreet />
                                    <adNumberInMunicipality />
                                    <adZipCode />
                                    <adState />
                                    <nationality />
                                    <dbIdOVM xsi:nil="true" />
                                    <dbState xsi:nil="true" />
                                    <dbOpenAddressing xsi:nil="true" />
                                    <dbUpperID xsi:nil="true" />
                                </dbOwnerInfo>
                            </FindDataBox2>
                        </soap:Body>
                    </soap:Envelope>
                """
                )

                toString()
            }

            val response = getResponse(soapXmlString, ""
                ,"https://${getUrl(testItem)}/DS/DsManage", userName, password)
            Log.d("", response.toString())
            return response.toString()
        }*/
    }

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

        val url = "https://${getUrl(testItem)}/DS/DsManage"
        //url = "https://ws1.czebox.cz/DS/DsManage"
        val response = getResponse(soapXmlString, ""
            ,url, userName, password)
        //Log.d("", response.toString())
        if (response.responseStatus) {
            val cls = GetOwnerInfoFromLogin2Response.fromString(response.responseText)
            return cls
        } else {
            return null
        }
    }

    fun getListOfReceivedMessages(userName: String, password : String, testItem : Boolean
                                  , fromDate : Date, toDate: Date, offset : Int = 1, limit : Int = 100)
            : List<GetListOfReceivedMessagesResponse>? {
        val df = DateFormat()


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

        val url = "https://${getUrl(testItem)}/DS/dx"
        val response = getResponse(soapXmlString, ""
            ,url, userName, password)

        if (response.responseStatus) {
            val cls = GetListOfReceivedMessagesResponse.fromString(response.responseText)
            return cls
        } else {
            return null
        }
    }


    class GetListOfReceivedMessagesResponse {
        companion object {
            fun fromString(inputXml : String) : List<GetListOfReceivedMessagesResponse> {
                val finalResultList : ArrayList<GetListOfReceivedMessagesResponse> = ArrayList()
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
                    val oneItem = GetListOfReceivedMessagesResponse()
                    val oneRow = lines.item(counterRow)
                    val elements = oneRow.childNodes
                    var counterElements = 0
                    var allFound = false
                    while (!allFound && counterElements < elements.length ) {
                        val item = elements.item(counterElements)

                        if (item.nodeName == "q:dbIDSender") {
                            oneItem.dbIDSender = item.firstChild.nodeValue
                        } else if (item.nodeName == "q:dmAnnotation") {
                            oneItem.dmAnnotation = item.firstChild.nodeValue
                        }
                        allFound = (oneItem.dbIDSender.isNotEmpty()
                                && oneItem.dmAnnotation.isNotEmpty())

                        counterElements++
                    }
                    finalResultList.add(oneItem)
                    counterRow++
                }
                return finalResultList
            }
        }

        private var dbIDSender = ""
        private var dmAnnotation = ""
    }

    class GetOwnerInfoFromLogin2Response {
        companion object {
            fun fromString(inputXml : String) : GetOwnerInfoFromLogin2Response {
                val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                val builder: DocumentBuilder = factory.newDocumentBuilder()
                val ins = InputSource(StringReader(inputXml))
                val doc: Document = builder.parse(ins)
                //doc.documentElement.normalize()
                /*val n1 = nList.item(0)
                val element: Element = n1 as Element
                val firstname = element.getElementsByTagName("name").item(0).textContent
                System.out.println(firstname);
                */
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