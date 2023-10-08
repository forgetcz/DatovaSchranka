package com.jvr.datovaschranka.api.model.getOwnerInfo

import com.jvr.datovaschranka.api.model.ApiCommon
import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root

/**
<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema">
    <soap:Body>
        <GetOwnerInfoFromLogin2 xmlns="http://isds.czechpoint.cz/v20">
            <dbDummy />
        </GetOwnerInfoFromLogin2>
    </soap:Body>
</soap:Envelop>
}*/
@Root(name = "soap:Envelope", strict = false)
@NamespaceList(
    Namespace(prefix = "soap", reference = "http://schemas.xmlsoap.org/soap/envelope/"),
    Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema")
)
class GetOwnerInfoRequest : ApiCommon() {
    @field:Element(name = "soap:Body", required = false)
    var body: GetOwnerInfoRequestBody? = null
        get() {
            if (field == null) {
                field = GetOwnerInfoRequestBody()
            }
            return field
        }

    init {
        body!!.getOwnerInfoFromLogin2!!
    }
}

@Root(name = "soap:Body", strict = false)
class GetOwnerInfoRequestBody {
    @field:Element(name = "GetOwnerInfoFromLogin2", required = false)
    var getOwnerInfoFromLogin2: GetOwnerInfoRequestDummy? = null
        get() {
            if (field == null) {
                field = GetOwnerInfoRequestDummy()
            }
            return field
        }
}

@Root(name = "GetOwnerInfoFromLogin2", strict = false)
@Namespace(prefix = "", reference = "http://isds.czechpoint.cz/v20")
//@NamespaceList(Namespace(prefix = "xmlns", reference = "http://isds.czechpoint.cz/v20"))
class GetOwnerInfoRequestDummy {
    @field:Element(name = "dbDummy", required = false)
    var dbDummy: String = ""
}
