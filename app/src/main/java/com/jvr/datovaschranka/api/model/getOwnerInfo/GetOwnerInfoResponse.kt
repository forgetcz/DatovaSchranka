package com.jvr.datovaschranka.api.model.getOwnerInfo

import com.jvr.datovaschranka.api.model.ApiCommon
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/*
<?xml version='1.0' encoding='utf-8'?>
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
    <SOAP-ENV:Body>
        <p:GetOwnerInfoFromLogin2Response xmlns:p="http://isds.czechpoint.cz/v20" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
            <p:dbOwnerInfo>
                <p:dbID>jptjjj9</p:dbID>
                <p:aifoIsds>false</p:aifoIsds>
                <p:dbType>FO</p:dbType>
                <p:ic xsi:nil="true"></p:ic>
                <p:pnGivenNames>Jiri</p:pnGivenNames>
                <p:pnLastName>Vrabec</p:pnLastName>
                <p:firmName xsi:nil="true"></p:firmName>
                <p:biDate>1972-11-27</p:biDate>
                <p:biCity>Praha 1</p:biCity>
                <p:biCounty>Hlavní město Praha</p:biCounty>
                <p:biState>CZ</p:biState>
                <p:adCode xsi:nil="true"></p:adCode>
                <p:adCity>Brno</p:adCity>
                <p:adDistrict xsi:nil="true"></p:adDistrict>
                <p:adStreet>Kvapilova</p:adStreet>
                <p:adNumberInStreet xsi:nil="true"></p:adNumberInStreet>
                <p:adNumberInMunicipality>6</p:adNumberInMunicipality>
                <p:adZipCode>61600</p:adZipCode>
                <p:adState xsi:nil="true"></p:adState>
                <p:nationality>CZ</p:nationality>
                <p:dbIdOVM xsi:nil="true"></p:dbIdOVM>
                <p:dbState>1</p:dbState>
                <p:dbOpenAddressing>true</p:dbOpenAddressing>
                <p:dbUpperID xsi:nil="true"></p:dbUpperID>
            </p:dbOwnerInfo>
            <p:dbStatus>
                <p:dbStatusCode>0000</p:dbStatusCode>
                <p:dbStatusMessage>Provedeno úspěšně.</p:dbStatusMessage>
            </p:dbStatus>
        </p:GetOwnerInfoFromLogin2Response>
    </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
* */
@Root(name = "Envelope", strict = false)
class GetOwnerInfoResponse : ApiCommon() {

    @field:Element(name = "Body", required = false)
    lateinit var getOwnerInfoBody: GetOwnerInfoResponseBody

    override fun toString(): String {
        return "Body = $getOwnerInfoBody"
    }
}

@Root(name = "Body", strict = false)
class GetOwnerInfoResponseBody {
    @field:Element(name = "GetOwnerInfoFromLogin2Response", required = false)
    lateinit var getOwnerInfoFromLogin2Response: GetOwnerInfoResponseStatusOwner

    override fun toString(): String {
        return "GetOwnerInfoFromLogin2Response = $getOwnerInfoFromLogin2Response"
    }
}

@Root(name = "GetUserInfoFromLogin2Response", strict = false)
class GetOwnerInfoResponseStatusOwner {
    @field:Element(name = "dbStatus", required = false)
    lateinit var getOwnerInfoDbStatus: GetOwnerInfoResponseDbStatusMessage

    @field:Element(name = "dbOwnerInfo", required = false)
    lateinit var getOwnerInfoDbOwnerInfo: GetOwnerInfoResponseDbId

    override fun toString(): String {
        return "dbStatus = $getOwnerInfoDbStatus, dbOwnerInfo = $getOwnerInfoDbOwnerInfo"
    }
}

@Root(name = "dbStatus", strict = false)
class GetOwnerInfoResponseDbStatusMessage {
    @field:Element(name = "dbStatusCode", required = false)
    lateinit var dbStatusCode: String

    @field:Element(name = "dbStatusMessage", required = false)
    lateinit var dbStatusMessage: String

    override fun toString(): String {
        return "dbStatusCode = $dbStatusCode, dbStatusMessage = $dbStatusMessage"
    }

}

@Root(name = "dbOwnerInfo", strict = false)
class GetOwnerInfoResponseDbId {
    @field:Element(name = "dbID", required = false)
    lateinit var dbID: String

    @field:Element(name = "dbType", required = false)
    lateinit var dbType: String

    override fun toString(): String {
        return "dbID = $dbID, dbType = $dbType"
    }
}
