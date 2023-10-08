package com.jvr.datovaschranka.api.model

import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.stream.Format
import java.io.ByteArrayOutputStream

@Suppress("UnnecessaryVariable")
open class ApiCommon {
    fun getSoapXML() : String {
        val stream = ByteArrayOutputStream()
        val serializer = Persister(Format("<?xml version=\"1.0\" encoding= \"utf-8\" ?>"))
        serializer.write(this,stream)
        val soapXmlString = String(stream.toByteArray())
        return soapXmlString
    }

    /*fun <T: Comparable<T>> sort(list: List<T>): List<T> {
        return list.sorted()
    }*/


    inline fun <reified T: Any> deserialize(inputText: String) : T {
        val deserializer = Persister()
        val container = deserializer.read(T::class.java, inputText)
        return container
    }

    /*fun <T: Any> deserialize2(inputText: String) : T {
        val deserializer = Persister()
        val container = deserializer.read(T::class.java, inputText)
        return container
    }*/
}