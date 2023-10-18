package com.jvr.common.contracts

interface IGetTag {
    fun getTag():String {
        return javaClass.name
    }
}