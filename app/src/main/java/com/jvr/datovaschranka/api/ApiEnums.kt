package com.jvr.datovaschranka.api

class ApiEnums {

    enum class OwnerType {
        OVM {
            override fun asInt(): Int = 10
            override fun asDescriptionString() = "DS orgánu veřejné moci (OVM)"
        },
        OVM_FO {
            override fun asInt(): Int = 14
            override fun asDescriptionString(): String =
                "DS fyzické osoby v roli Orgánu veřejné moci"
        },
        OVM_PFO {
            override fun asInt(): Int = 15
            override fun asDescriptionString(): String =
                "DS podnikající fyzické osoby v roli Orgánu veřejné moci (např.notář nebo exekutor)"
        },
        OVM_PO {
            override fun asInt(): Int = 16
            override fun asDescriptionString(): String =
                "DS orgánu veřejné moci, vzniklá změnou typu poté, co byl\n" +
                        "subjekt odpovídající existující schránce typu PO nebo PO_REQ\n" +
                        "zapsán do Rejstříku OVM."
        },
        OVM_REQ {
            override fun asInt(): Int = 16
            override fun asDescriptionString(): String =
                "podřízená DS typu OVM vzniklá na žádost, pro potřeby\n" +
                        "mateřského OVM, přebírá IČO mateřské schránky typu OVM"
        },
        PO {
            override fun asInt(): Int = 20
            override fun asDescriptionString(): String = "DS právnické osoby (z ROS)"
        },
        PO_REQ {
            override fun asInt(): Int = 22
            override fun asDescriptionString(): String =
                "DS právnické osoby, vzniklá na žádost (takový typ subjektu,\n" +
                        "kterému nebyla automaticky zřízena DS typu PO)"
        },
        PFO {
            override fun asInt(): Int = 30
            override fun asDescriptionString(): String =
                "DS podnikající fyzické osoby, včetně cizinců"
        },
        PFO_ADVOK {
            override fun asInt(): Int = 31
            override fun asDescriptionString(): String = "DS advokáta"
        },
        PFO_DANPOR {
            override fun asInt(): Int = 32
            override fun asDescriptionString(): String = "DS daňového poradce"
        },
        PFO_INSSPR {
            override fun asInt(): Int = 33
            override fun asDescriptionString(): String = "DS insolvenčního správce"
        },
        PFO_AUDITOR {
            override fun asInt(): Int = 34
            override fun asDescriptionString(): String =
                "DS statutárního auditora (OSVČ nebo zaměstnance – pak nemá IČO)"
        },
        FO {
            override fun asInt(): Int = 40
            override fun asDescriptionString(): String = "DS fyzické osoby"

        };

        abstract fun asInt(): Int
        abstract fun asDescriptionString(): String
    }

    enum class MessageStatus(val value: Int) {
        Podana(1) {
            override fun asInt() = value
            override fun asDescriptionString() = "Datová zpráva byla podána (vznikla v ISDS)."
        },
        Podepsana(2) {
            override fun asInt() = value
            override fun asDescriptionString() = "Datová zpráva včetně písemností podepsána podacím časovým razítkem."
        },
        NeproslaAV(3) {
            override fun asInt() = value
            override fun asDescriptionString() = "Datová zpráva neprošla AV kontrolou - zpráva není ani dodána; konečný stav zprávy\n" +
                    "před smazáním."
        }, Dodana(4) {
            override fun asInt()  = value
            override fun asDescriptionString() = "Datová zpráva dodána do schránky adresáta (zapsán čas dodání), je přístupná\n" +
                    "adresátovi."
        }, Uplynulo10dni(5) {
            override fun asInt() = value
            override fun asDescriptionString() = "Uplynulo 10 dní od dodání veřejné zprávy, která dosud nebyla doručena přihlášením\n" +
                    "(předpoklad doručení fikcí u neOVM schránky); u komerční nebo systémové zprávy\n" +
                    "nemůže tento stav nastat."
        }, DorucenaPrihlasenim(6) {
            override fun asInt() = value
            override fun asDescriptionString() = "Osoba (nebo aplikace přihlašující se systémovým certifikátem) oprávněná číst tuto\n" +
                    "zprávu se přihlásila - zpráva byla doručena přihlášením.\n"
        }, Prectena(7) {
            override fun asInt() = 7
            override fun asDescriptionString() = "Zpráva byla přečtena (na portále nebo akcí ESS).\n"
        }, Nedorucitelna(8) {
            override fun asInt() = value
            override fun asDescriptionString() = "Zpráva byla označena jako nedoručitelná, protože DS adresáta byla zpětně\n" +
                    "znepřístupněna; netýká se systémových zpráv"
        },Archiv(9) {
            override fun asInt() = value
            override fun asDescriptionString() = "Obsah zprávy byl smazán, obálka zprávy včetně hashů přesunuta do archivu\n" +
                    "(jen některé služby umí přistupovat k archivním obálkám zpráv)\n"
        }, Trezor(10) {
            override fun asInt() = value
            override fun asDescriptionString() = "Zpráva byla přesunuta do Datového trezoru odesílatele nebo adresáta (nebo obou);\n" +
                    "netýká se systémových zpráv.n"
        }
        ;

        companion object {
            fun fromInt(value: Int) = values().first { it.value == value }
            fun fromIntString(value: String) = values().first { it.value.toString() == value }
        }

        abstract fun asInt(): Int
        abstract fun asDescriptionString(): String
    }
}