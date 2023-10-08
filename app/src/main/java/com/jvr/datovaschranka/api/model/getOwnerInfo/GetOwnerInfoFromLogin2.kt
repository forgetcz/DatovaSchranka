@file:Suppress("LiftReturnOrAssignment", "UnnecessaryVariable")

package com.jvr.datovaschranka.api.model.getOwnerInfo

import com.jvr.datovaschranka.api.DsApi
import org.simpleframework.xml.core.Persister

class GetOwnerInfoFromLogin2 {
    fun getOwnerInfoFromLogin2(userName: String, password : String, testItem : Boolean)
            : GetOwnerInfoResponse? {

        val soapXmlRequestString = GetOwnerInfoRequest().getSoapXML()

        val url = "https://${DsApi.getUrl(testItem)}/DS/DsManage"

        val response = DsApi.getResponse(
            soapXmlRequestString, "", url, userName, password
        )

        if (response.responseStatus) {
            val deserializer = Persister()
            try {
                val container = GetOwnerInfoResponse().deserialize<GetOwnerInfoResponse>(response.responseText)
                //val container = deserializer.read(GetOwnerInfoResponse ::class.java, response.responseText)
                return container
            } catch (ex : Exception) {
                throw ex
            }

            //val cls = GetOwnerInfoFromLogin2Response.fromXmlString(response.responseText)
            //return cls
        } else {
            return null
        }
    }

    /*class GetOwnerInfoFromLogin2Response {
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

        var dbID: String = ""
            set(value) {
                field = value
            }
    }*/
}