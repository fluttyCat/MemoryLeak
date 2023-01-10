package com.baloot.app.util

import com.baloot.app.util.Constants.ANDROID_KEY_STORE
import com.baloot.app.util.Constants.TRANSFORMATION
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

class Decrypt {

    private var keyStore: KeyStore? = null
    private val FIXED_IV = byteArrayOf(
        55, 54, 53, 52, 51, 50,
        49, 48, 47,
        46, 45, 44
    )

    init {
        initKeyStore()
    }

    @Throws(
        KeyStoreException::class,
        CertificateException::class,
        NoSuchAlgorithmException::class,
        IOException::class
    )
    private fun initKeyStore() {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
    }

    @Throws(
        UnrecoverableEntryException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        NoSuchProviderException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IOException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class,
        InvalidAlgorithmParameterException::class
    )
    fun decryptData(alias: String, encryptedData: ByteArray?, encryptionIv: ByteArray?): String? {

        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        val keySpec = IvParameterSpec(encryptionIv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias), keySpec)
        return cipher.doFinal(encryptedData).toString(Charsets.UTF_8)
    }

    @Throws(
        NoSuchAlgorithmException::class,
        UnrecoverableEntryException::class,
        KeyStoreException::class
    )
    private fun getSecretKey(alias: String): SecretKey? {
        return (keyStore!!.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
    }
}



/*
*
*
* /* val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(128, encryptionIv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias), spec)
        return String(cipher.doFinal(encryptedData), charset("UTF-8"))*/*/