package com.jvr.datovaschranka.api

enum class ApiDbType {
    OVM {
        override fun asInt(): Int = 10
        override fun asDescriptionString() = "DS orgánu veřejné moci (OVM)"
    }, OVM_FO {
        override fun asInt(): Int = 14
        override fun asDescriptionString(): String  = "DS fyzické osoby v roli Orgánu veřejné moci"
    }, OVM_PFO {
        override fun asInt(): Int = 15
        override fun asDescriptionString(): String = "DS podnikající fyzické osoby v roli Orgánu veřejné moci (např.notář nebo exekutor)"
    }, OVM_PO {
        override fun asInt(): Int = 16
        override fun asDescriptionString(): String = "DS orgánu veřejné moci, vzniklá změnou typu poté, co byl\n" +
                "subjekt odpovídající existující schránce typu PO nebo PO_REQ\n" +
                "zapsán do Rejstříku OVM."
    }, OVM_REQ {
        override fun asInt(): Int = 16
        override fun asDescriptionString(): String = "podřízená DS typu OVM vzniklá na žádost, pro potřeby\n" +
                "mateřského OVM, přebírá IČO mateřské schránky typu OVM"
    }, PO {
        override fun asInt(): Int = 20
        override fun asDescriptionString(): String = "DS právnické osoby (z ROS)"
    }, PO_REQ {
        override fun asInt(): Int = 22
        override fun asDescriptionString(): String = "DS právnické osoby, vzniklá na žádost (takový typ subjektu,\n" +
                "kterému nebyla automaticky zřízena DS typu PO)"
    }, PFO {
        override fun asInt(): Int = 30
        override fun asDescriptionString(): String = "DS podnikající fyzické osoby, včetně cizinců"
    }, PFO_ADVOK {
        override fun asInt(): Int = 31
        override fun asDescriptionString(): String = "DS advokáta"
    }, PFO_DANPOR {
        override fun asInt(): Int = 32
        override fun asDescriptionString(): String = "DS daňového poradce"
    }, PFO_INSSPR {
        override fun asInt(): Int = 33
        override fun asDescriptionString(): String = "DS insolvenčního správce"
    }, PFO_AUDITOR {
        override fun asInt(): Int = 34
        override fun asDescriptionString(): String = "DS statutárního auditora (OSVČ nebo zaměstnance – pak nemá IČO)"
    }, FO {
        override fun asInt(): Int = 40
        override fun asDescriptionString(): String = "DS fyzické osoby"

    };

    abstract fun asInt(): Int
    abstract fun asDescriptionString(): String
}