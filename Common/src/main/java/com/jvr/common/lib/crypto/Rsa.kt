@file:Suppress("UnnecessaryVariable", "LiftReturnOrAssignment")

package com.jvr.common.lib.crypto

import android.content.Context
import android.security.keystore.KeyProperties
import android.util.Base64
import com.jvr.common.lib.AppSettings
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.EncodedKeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

// https://gustavopeiretti.com/rsa-encrypt-decrypt-java/
// https://gustavopeiretti.com/rsa-keys-java/

class Rsa(private val applicationContext: Context) {

    companion object {
        const val appSettingPrivateKey = "private"
        const val appSettingPublicKey = "public"
    }

    private fun generateKeys() : KeyPair {
        /////////////// Initialize //////////////////////////////////////
        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(4096)
        val kp: KeyPair = kpg.generateKeyPair()
        //val aPrivate: PrivateKey = kp.private
        //val aPublic: PublicKey = kp.public
        //println("Private key: " + aPrivate.format) //PKCS#8
        //println("Public key: " + aPublic.format) // X.509

        //val app = AppSettings.getInstance(applicationContext)
        // app.setKey(appSettingPrivateKey, String(Base64.encode(aPrivate.encoded,0)))
        //app.setKey(appSettingPublicKey, String(Base64.encode(aPublic.encoded,0)))
        return  kp
    }

    private fun saveKeys(keyPair : KeyPair) {
        val app = AppSettings.getInstance(applicationContext)
        app.setKey(appSettingPrivateKey, String(Base64.encode(keyPair.private.encoded,0)))
        app.setKey(appSettingPublicKey, String(Base64.encode(keyPair.public.encoded,0)))
    }

    private fun loadKeys() {
        try {
            val app = AppSettings.getInstance(applicationContext)
            val appSettPublic = app.getKey(appSettingPublicKey)
            val appSettPrivate = app.getKey(appSettingPrivateKey)

            val privateByteArr = Base64.decode(appSettPrivate, 0)
            val privateKeyFactory: KeyFactory =
                KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
            val privateKeySpec: EncodedKeySpec =
                PKCS8EncodedKeySpec(privateByteArr)//aPrivate.encoded
            val privateKey: PrivateKey = privateKeyFactory.generatePrivate(privateKeySpec)

            val publicByteArr = Base64.decode(appSettPublic, 0)
            val publicKeyFactory = KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
            val publicKeySpec: EncodedKeySpec = X509EncodedKeySpec(publicByteArr)//aPublic.encoded
            val publicKey: PublicKey = publicKeyFactory.generatePublic(publicKeySpec)

            KeyPair(publicKey, privateKey)
        } catch (_: Exception) {
            val keys = generateKeys()
            saveKeys(keys)
        }
    }

    fun getPrivateKey() : PrivateKey {
        val app = AppSettings.getInstance(applicationContext)
        val appSettPrivate = app.getKey(appSettingPrivateKey)
        if (appSettPrivate == null) {
            generateKeys()
        }
        val appSettPrivateByteArr = Base64.decode(appSettPrivate,0)
        val privateKeyFactory: KeyFactory = KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
        val privateKeySpec: EncodedKeySpec = PKCS8EncodedKeySpec(appSettPrivateByteArr)//aPrivate.encoded
        val privateKey : PrivateKey = privateKeyFactory.generatePrivate(privateKeySpec)
        return privateKey
    }
    fun crypt(what: String) : String {
        val app = AppSettings.getInstance(applicationContext)
        val appSettPublic = app.getKey(appSettingPublicKey)
        val publicByteArr = Base64.decode(appSettPublic,0)
        val publicKeyFactory = KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
        val publicKeySpec: EncodedKeySpec = X509EncodedKeySpec(publicByteArr)//aPublic.encoded
        val publicKey : PublicKey = publicKeyFactory.generatePublic(publicKeySpec)

        val cipherEncode: Cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
        cipherEncode.init(Cipher.ENCRYPT_MODE, publicKey)
        val bytesEncoded = cipherEncode.doFinal(what.toByteArray(StandardCharsets.UTF_8))
        val result =String(Base64.encode(bytesEncoded,0))

        return result
    }

    fun decCrypt(what: String) : String {
        val app = AppSettings.getInstance(applicationContext)
        val appSettPrivate = app.getKey(appSettingPrivateKey)
        val appSettPrivateByteArr = Base64.decode(appSettPrivate,0)
        val privateKeyFactory: KeyFactory = KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
        val privateKeySpec: EncodedKeySpec = PKCS8EncodedKeySpec(appSettPrivateByteArr)//aPrivate.encoded
        val privateKey : PrivateKey = privateKeyFactory.generatePrivate(privateKeySpec)

        val cipherDecode = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
        cipherDecode.init(Cipher.DECRYPT_MODE, privateKey)
        val bytes = cipherDecode.doFinal( Base64.decode(what,0))
        val stringRes = String(bytes)
        return stringRes
    }

    fun cryptEncrypt(what: String) : String {
        val app = AppSettings.getInstance(applicationContext)
        val appSettPrivate = app.getKey(appSettingPrivateKey)
        val appSettPrivateByteArr = Base64.decode(appSettPrivate,0)
        val privateKeyFactory: KeyFactory = KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
        val privateKeySpec: EncodedKeySpec = PKCS8EncodedKeySpec(appSettPrivateByteArr)//aPrivate.encoded
        val privateKey : PrivateKey = privateKeyFactory.generatePrivate(privateKeySpec)

        val appSettPublic = app.getKey(appSettingPublicKey)
        val appSettPublicByteArr = Base64.decode(appSettPublic,0)
        val publicKeyFactory = KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
        val publicKeySpec: EncodedKeySpec = X509EncodedKeySpec(appSettPublicByteArr)//aPublic.encoded
        val publicKey : PublicKey = publicKeyFactory.generatePublic(publicKeySpec)

        /////////// decrypt ///////////////////////
        val cipherEncode: Cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
        cipherEncode.init(Cipher.ENCRYPT_MODE, publicKey)
        val bytesEncoded = cipherEncode.doFinal(what.toByteArray(StandardCharsets.UTF_8))

        val cipherDecode = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
        cipherDecode.init(Cipher.DECRYPT_MODE, privateKey)
        val bytes = cipherDecode.doFinal(bytesEncoded)
        val stringRes = String(bytes)
        return stringRes
    }

    init {
        loadKeys()
    }
}