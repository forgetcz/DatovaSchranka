package com.jvr.datovaschranka.api.model.getListOfReceivedMessages

import com.jvr.datovaschranka.api.model.ApiCommon
import com.jvr.datovaschranka.api.model.ApiEnums
import org.simpleframework.xml.*

@Root(name = "SOAP-ENV:Envelope", strict = false)
@NamespaceList(
    Namespace(prefix = "xmlns:SOAP-ENV", reference = "http://schemas.xmlsoap.org/soap/envelope/"),
    Namespace(prefix = "xmlns:xsd", reference = "http://www.w3.org/2001/XMLSchema"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema")
)
class GetListOfReceivedMessagesResponseRoot: ApiCommon()  {
    companion object{
        const val example = """<?xml version='1.0' encoding='utf-8'?>
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
    <SOAP-ENV:Body>
        <q:GetListOfReceivedMessagesResponse xmlns:q="http://isds.czechpoint.cz/v20" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
            <q:dmRecords>
                <q:dmRecord dmType="E">
                    <q:dmOrdinal>1</q:dmOrdinal>
                    <q:dmID>9452659</q:dmID>
                    <q:dbIDSender>uuyjmzh</q:dbIDSender>
                    <q:dmSender>Pravnicka osoba</q:dmSender>
                    <q:dmSenderAddress>Palackého 1043, 666 02 Předklášteří</q:dmSenderAddress>
                    <q:dmSenderType>20</q:dmSenderType>
                    <q:dmRecipient>Jiri Vrabec</q:dmRecipient>
                    <q:dmRecipientAddress>Kvapilova 6, 61600 Brno</q:dmRecipientAddress>
                    <q:dmSenderOrgUnit xsi:nil="true"></q:dmSenderOrgUnit>
                    <q:dmSenderOrgUnitNum xsi:nil="true"></q:dmSenderOrgUnitNum>
                    <q:dbIDRecipient>jptjjj9</q:dbIDRecipient>
                    <q:dmRecipientOrgUnit xsi:nil="true"></q:dmRecipientOrgUnit>
                    <q:dmRecipientOrgUnitNum xsi:nil="true"></q:dmRecipientOrgUnitNum>
                    <q:dmToHands xsi:nil="true"></q:dmToHands>
                    <q:dmAnnotation>Pokus 2</q:dmAnnotation>
                    <q:dmRecipientRefNumber xsi:nil="true"></q:dmRecipientRefNumber>
                    <q:dmSenderRefNumber xsi:nil="true"></q:dmSenderRefNumber>
                    <q:dmRecipientIdent xsi:nil="true"></q:dmRecipientIdent>
                    <q:dmSenderIdent xsi:nil="true"></q:dmSenderIdent>
                    <q:dmLegalTitleLaw xsi:nil="true"></q:dmLegalTitleLaw>
                    <q:dmLegalTitleYear xsi:nil="true"></q:dmLegalTitleYear>
                    <q:dmLegalTitleSect xsi:nil="true"></q:dmLegalTitleSect>
                    <q:dmLegalTitlePar xsi:nil="true"></q:dmLegalTitlePar>
                    <q:dmLegalTitlePoint xsi:nil="true"></q:dmLegalTitlePoint>
                    <q:dmPersonalDelivery>false</q:dmPersonalDelivery>
                    <q:dmAllowSubstDelivery>true</q:dmAllowSubstDelivery>
                    <q:dmMessageStatus>7</q:dmMessageStatus>
                    <q:dmAttachmentSize>409</q:dmAttachmentSize>
                    <q:dmDeliveryTime>2023-09-22T17:25:06.084+02:00</q:dmDeliveryTime>
                    <q:dmAcceptanceTime>2023-09-22T17:25:38.927+02:00</q:dmAcceptanceTime>
                </q:dmRecord>
                <q:dmRecord dmType="E">
                    <q:dmOrdinal>2</q:dmOrdinal>
                    <q:dmID>9452403</q:dmID>
                    <q:dbIDSender>uuyjmzh</q:dbIDSender>
                    <q:dmSender>Pravnicka osoba</q:dmSender>
                    <q:dmSenderAddress>Palackého 1043, 666 02 Předklášteří</q:dmSenderAddress>
                    <q:dmSenderType>20</q:dmSenderType>
                    <q:dmRecipient>Jiri Vrabec</q:dmRecipient>
                    <q:dmRecipientAddress>Kvapilova 6, 61600 Brno</q:dmRecipientAddress>
                    <q:dmSenderOrgUnit xsi:nil="true"></q:dmSenderOrgUnit>
                    <q:dmSenderOrgUnitNum xsi:nil="true"></q:dmSenderOrgUnitNum>
                    <q:dbIDRecipient>jptjjj9</q:dbIDRecipient>
                    <q:dmRecipientOrgUnit xsi:nil="true"></q:dmRecipientOrgUnit>
                    <q:dmRecipientOrgUnitNum xsi:nil="true"></q:dmRecipientOrgUnitNum>
                    <q:dmToHands xsi:nil="true"></q:dmToHands>
                    <q:dmAnnotation>Pokus 1</q:dmAnnotation>
                    <q:dmRecipientRefNumber xsi:nil="true"></q:dmRecipientRefNumber>
                    <q:dmSenderRefNumber xsi:nil="true"></q:dmSenderRefNumber>
                    <q:dmRecipientIdent xsi:nil="true"></q:dmRecipientIdent>
                    <q:dmSenderIdent xsi:nil="true"></q:dmSenderIdent>
                    <q:dmLegalTitleLaw xsi:nil="true"></q:dmLegalTitleLaw>
                    <q:dmLegalTitleYear xsi:nil="true"></q:dmLegalTitleYear>
                    <q:dmLegalTitleSect xsi:nil="true"></q:dmLegalTitleSect>
                    <q:dmLegalTitlePar xsi:nil="true"></q:dmLegalTitlePar>
                    <q:dmLegalTitlePoint xsi:nil="true"></q:dmLegalTitlePoint>
                    <q:dmPersonalDelivery>false</q:dmPersonalDelivery>
                    <q:dmAllowSubstDelivery>true</q:dmAllowSubstDelivery>
                    <q:dmMessageStatus>7</q:dmMessageStatus>
                    <q:dmAttachmentSize>409</q:dmAttachmentSize>
                    <q:dmDeliveryTime>2023-09-22T10:47:40.598+02:00</q:dmDeliveryTime>
                    <q:dmAcceptanceTime>2023-09-22T10:48:54.512+02:00</q:dmAcceptanceTime>
                </q:dmRecord>
            </q:dmRecords>
            <q:dmStatus>
                <q:dmStatusCode>0000</q:dmStatusCode>
                <q:dmStatusMessage>Provedeno úspěšně.</q:dmStatusMessage>
            </q:dmStatus>
        </q:GetListOfReceivedMessagesResponse>
    </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
"""
    }

    @field:Element(name = "Body", required = false)
    lateinit var body: GetListOfReceivedMessagesResponseBody
}

@Root(name = "Body", strict = false)
class GetListOfReceivedMessagesResponseBody {
    @field:Element(name = "GetListOfReceivedMessagesResponse", required = false)
    @NamespaceList(
        Namespace(prefix = "xmlns:q", reference = "http://isds.czechpoint.cz/v20"),
        Namespace(prefix = "xmlns:xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    )
    lateinit var getListOfReceivedMessagesResponse: GetListOfReceivedMessagesResponse
}

@Root(name = "GetListOfReceivedMessagesResponse", strict = false)
class GetListOfReceivedMessagesResponse {
    @field:ElementList(name = "dmRecords", required = false)
    lateinit var dmRecords : List<GetListOfReceivedMessagesResponseDmRecords>

    @field:Element(name = "dmStatus", required = false)
    lateinit var dmStatus : DmStatus
}

@Root(name = "dmStatus", strict = false)
class DmStatus {
    @field:Element(name = "dmStatusCode", required = false)
    var dmStatusCode : Int? = null
}

@Root(name = "dmRecords", strict = false)
class GetListOfReceivedMessagesResponseDmRecords {
    @field:Attribute(name = "dmType", required = false)
    var dmType : String? = null

    @field:Element(name = "dmOrdinal", required = false)
    var dmOrdinal : Int? = null

    @field:Element(name = "dmSender", required = false)
    var dmSender : String? = null

    @field:Element(name = "dmAnnotation", required = false)
    var dmAnnotation : String? = null

    @field:Element(name = "dmMessageStatus", required = false)
    var dmMessageStatus : Int? = null

    val translatedDmMessageStatus : ApiEnums.MessageStatus
        get() = ApiEnums.MessageStatus.fromInt(dmMessageStatus!!)

}
