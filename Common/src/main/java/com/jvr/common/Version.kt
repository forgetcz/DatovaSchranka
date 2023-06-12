package com.jvr.common

import java.util.*

class Version {
    private val tokenString: List<String> = BuildConfig.VERSION_NAME.split("\\.")//TODO:VERSION_NAME !!!
    val major = tokenString[0].toInt()
    val minor = tokenString[1].toInt()
    val build = tokenString[2].toInt()
    val revision = tokenString[3].toInt()

    /**
     * @return String
     */
    override fun toString(): String {
        return (major.toString() + "." + minor.toString() + "."
                + build.toString() + "." + revision.toString())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that: Version = other as Version
        return major == that.major && minor == that.minor && build == that.build && revision == that.revision
    }

    /**
     * Use : System.out.println(Objects.equals(temp1, temp2));
     * Use : System.out.println(Objects.equals(temp2, temp3));
     * @return
     * https://www.educative.io/answers/what-is-objectsequals-in-java
     */
    override fun hashCode(): Int {
        return Objects.hash(major, minor, build, revision)
    }
}