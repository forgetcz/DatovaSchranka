package com.jvr.datovaschranka.api.model.getListOfSentMessages

import com.jvr.datovaschranka.api.model.ApiCommon
import org.simpleframework.xml.*

@Root(name = "SOAP-ENV:Envelope", strict = false)
@NamespaceList(
    Namespace(prefix = "xmlns:SOAP-ENV", reference = "http://schemas.xmlsoap.org/soap/envelope/"),
    Namespace(prefix = "xmlns:xsd", reference = "http://www.w3.org/2001/XMLSchema"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema")
)
class GetListOfSentMessagesResponseRoot: ApiCommon()  {
    companion object{
        const val example = """<?xml version="1.0" encoding="utf-8"?>
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
  <SOAP-ENV:Body>
    <q:GetListOfSentMessagesResponse xmlns:q="http://isds.czechpoint.cz/v20" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
      <q:dmRecords>
        <q:dmRecord dmType="E">
          <q:dmOrdinal>1</q:dmOrdinal>
          <q:dmID>9456796</q:dmID>
          <q:dbIDSender>jptjjj9</q:dbIDSender>
          <q:dmSender>Jiri Vrabec</q:dmSender>
          <q:dmSenderAddress>Kvapilova 6, 61600 Brno</q:dmSenderAddress>
          <q:dmSenderType>40</q:dmSenderType>
          <q:dmRecipient>Pravnicka osoba</q:dmRecipient>
          <q:dmRecipientAddress>Palackého 1043, 666 02 Předklášteří</q:dmRecipientAddress>
          <q:dmSenderOrgUnit xsi:nil="true">
          </q:dmSenderOrgUnit>
          <q:dmSenderOrgUnitNum xsi:nil="true">
          </q:dmSenderOrgUnitNum>
          <q:dbIDRecipient>uuyjmzh</q:dbIDRecipient>
          <q:dmRecipientOrgUnit xsi:nil="true">
          </q:dmRecipientOrgUnit>
          <q:dmRecipientOrgUnitNum xsi:nil="true">
          </q:dmRecipientOrgUnitNum>
          <q:dmToHands xsi:nil="true">
          </q:dmToHands>
          <q:dmAnnotation>Pokus 001 27.10.2025</q:dmAnnotation>
          <q:dmRecipientRefNumber xsi:nil="true">
          </q:dmRecipientRefNumber>
          <q:dmSenderRefNumber xsi:nil="true">
          </q:dmSenderRefNumber>
          <q:dmRecipientIdent xsi:nil="true">
          </q:dmRecipientIdent>
          <q:dmSenderIdent xsi:nil="true">
          </q:dmSenderIdent>
          <q:dmLegalTitleLaw xsi:nil="true">
          </q:dmLegalTitleLaw>
          <q:dmLegalTitleYear xsi:nil="true">
          </q:dmLegalTitleYear>
          <q:dmLegalTitleSect xsi:nil="true">
          </q:dmLegalTitleSect>
          <q:dmLegalTitlePar xsi:nil="true">
          </q:dmLegalTitlePar>
          <q:dmLegalTitlePoint xsi:nil="true">
          </q:dmLegalTitlePoint>
          <q:dmPersonalDelivery>true</q:dmPersonalDelivery>
          <q:dmAllowSubstDelivery>true</q:dmAllowSubstDelivery>
          <q:dmMessageStatus>6</q:dmMessageStatus>
          <q:dmAttachmentSize>252</q:dmAttachmentSize>
          <q:dmDeliveryTime>2023-09-27T07:31:06.515+02:00</q:dmDeliveryTime>
          <q:dmAcceptanceTime>2023-09-27T07:31:23.186+02:00</q:dmAcceptanceTime>
        </q:dmRecord>
        <q:dmRecord dmType="E">
          <q:dmOrdinal>2</q:dmOrdinal>
          <q:dmID>9409954</q:dmID>
          <q:dbIDSender>jptjjj9</q:dbIDSender>
          <q:dmSender>Jiri Vrabec</q:dmSender>
          <q:dmSenderAddress>Kvapilova 6, 61600 Brno</q:dmSenderAddress>
          <q:dmSenderType>40</q:dmSenderType>
          <q:dmRecipient>Pravnicka osoba</q:dmRecipient>
          <q:dmRecipientAddress>Palackého 1043, 666 02 Předklášteří</q:dmRecipientAddress>
          <q:dmSenderOrgUnit xsi:nil="true">
          </q:dmSenderOrgUnit>
          <q:dmSenderOrgUnitNum xsi:nil="true">
          </q:dmSenderOrgUnitNum>
          <q:dbIDRecipient>uuyjmzh</q:dbIDRecipient>
          <q:dmRecipientOrgUnit xsi:nil="true">
          </q:dmRecipientOrgUnit>
          <q:dmRecipientOrgUnitNum xsi:nil="true">
          </q:dmRecipientOrgUnitNum>
          <q:dmToHands xsi:nil="true">
          </q:dmToHands>
          <q:dmAnnotation>Pokus 1</q:dmAnnotation>
          <q:dmRecipientRefNumber xsi:nil="true">
          </q:dmRecipientRefNumber>
          <q:dmSenderRefNumber xsi:nil="true">
          </q:dmSenderRefNumber>
          <q:dmRecipientIdent xsi:nil="true">
          </q:dmRecipientIdent>
          <q:dmSenderIdent xsi:nil="true">
          </q:dmSenderIdent>
          <q:dmLegalTitleLaw xsi:nil="true">
          </q:dmLegalTitleLaw>
          <q:dmLegalTitleYear xsi:nil="true">
          </q:dmLegalTitleYear>
          <q:dmLegalTitleSect xsi:nil="true">
          </q:dmLegalTitleSect>
          <q:dmLegalTitlePar xsi:nil="true">
          </q:dmLegalTitlePar>
          <q:dmLegalTitlePoint xsi:nil="true">
          </q:dmLegalTitlePoint>
          <q:dmPersonalDelivery>false</q:dmPersonalDelivery>
          <q:dmAllowSubstDelivery>true</q:dmAllowSubstDelivery>
          <q:dmMessageStatus>6</q:dmMessageStatus>
          <q:dmAttachmentSize>563</q:dmAttachmentSize>
          <q:dmDeliveryTime>2023-09-13T20:01:25.198+02:00</q:dmDeliveryTime>
          <q:dmAcceptanceTime>2023-09-13T20:01:30.879+02:00</q:dmAcceptanceTime>
        </q:dmRecord>
      </q:dmRecords>
      <q:dmStatus>
        <q:dmStatusCode>0000</q:dmStatusCode>
        <q:dmStatusMessage>Provedeno úspěšně.</q:dmStatusMessage>
      </q:dmStatus>
    </q:GetListOfSentMessagesResponse>
  </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
"""
    }

    @field:Element(name = "Body", required = false)
    lateinit var body: GetListOfReceivedMessagesResponseBody
}

@Root(name = "Body", strict = false)
class GetListOfReceivedMessagesResponseBody {
    @field:Element(name = "GetListOfSentMessagesResponse", required = false)
    @NamespaceList(
        Namespace(prefix = "xmlns:q", reference = "http://isds.czechpoint.cz/v20"),
        Namespace(prefix = "xmlns:xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    )
    lateinit var getListOfSentMessagesResponse: GetListOfSentMessagesResponse
}

@Root(name = "GetListOfSentMessagesResponse", strict = false)
class GetListOfSentMessagesResponse {
    @field:ElementList(name = "dmRecords", required = false)
    lateinit var dmRecords : List<GetListOfSentMessagesResponseDmRecords>

    @field:Element(name = "dmStatus", required = false)
    lateinit var dmStatus : DmStatus
}

@Root(name = "dmStatus", strict = false)
class DmStatus {
    @field:Element(name = "dmStatusCode", required = false)
    var dmStatusCode : Int? = null
}

@Root(name = "dmRecords", strict = false)
class GetListOfSentMessagesResponseDmRecords {
    @field:Attribute(name = "dmType", required = false)
    var dmType : String? = null

    @field:Element(name = "dmOrdinal", required = false)
    var dmOrdinal : Int? = null

    @field:Element(name = "dmSender", required = false)
    var dmSender : String? = null

    @field:Element(name = "dmAnnotation", required = false)
    var dmAnnotation : String? = null

}
