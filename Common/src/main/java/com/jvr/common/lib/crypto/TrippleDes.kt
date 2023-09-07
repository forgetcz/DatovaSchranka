package com.jvr.common.lib.crypto

import android.util.Base64
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESedeKeySpec

class TrippleDes {
    private val ks: KeySpec
    private val skf: SecretKeyFactory
    private val cipher: Cipher
    private var arrayBytes: ByteArray
    private val myEncryptionKey = "ThisIsSpartaThisIsSparta"
    private val myEncryptionScheme: String = DESEDE_ENCRYPTION_SCHEME
    var key: SecretKey
    private val charSet = charset(UNICODE_FORMAT)

    companion object {
        private const val UNICODE_FORMAT = "UTF8"
        const val DESEDE_ENCRYPTION_SCHEME = "DESede"
    }

    init {
        arrayBytes = myEncryptionKey.toByteArray(charSet)
        ks = DESedeKeySpec(arrayBytes)
        skf = SecretKeyFactory.getInstance(myEncryptionScheme)
        cipher = Cipher.getInstance(myEncryptionScheme)
        key = skf.generateSecret(ks)
    }

    fun encrypt(unencryptedString: String): String? {
        var encryptedString: String? = null
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val plainText = unencryptedString.toByteArray(charSet)
            val encryptedText = cipher.doFinal(plainText)
            encryptedString = String(Base64.encode(encryptedText,0))//Base64.encodeBase64(
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return encryptedString
    }

    fun decrypt(encryptedString: String?): String? {
        var decryptedText: String? = null
        try {
            cipher.init(Cipher.DECRYPT_MODE, key)
            val encryptedText = Base64.decode(encryptedString,0)
            val plainText = cipher.doFinal(encryptedText)
            decryptedText = String(plainText)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return decryptedText
    }


}