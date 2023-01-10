package com.baloot.app.util

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.io.IOException
import java.security.*
import javax.crypto.*


@RequiresApi(Build.VERSION_CODES.M)

class Encrypt {
    private var encryption: ByteArray? = null
    private var iv: ByteArray? = null


    @Throws(
        UnrecoverableEntryException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        NoSuchProviderException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IOException::class,
        InvalidAlgorithmParameterException::class,
        SignatureException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
    fun encryptText(alias: String, textToEncrypt: String): ByteArray? {

        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias = alias))

        val _iv: ByteArray = cipher.iv
        val encryptedData = cipher.doFinal(textToEncrypt.toByteArray(Charsets.UTF_8))
        return encryptedData
    }


    private fun getSecretKey(alias: String): SecretKey? {
        val keyGenerator: KeyGenerator = KeyGenerator
            .getInstance(KeyProperties.KEY_ALGORITHM_AES, Constants.ANDROID_KEY_STORE)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
        )
        return keyGenerator.generateKey()
    }

    fun getEncryption(): ByteArray? {
        return encryption
    }

    fun getIv(): ByteArray? {
        return iv
    }

}

/*
*
*
*    /* val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias!!))
        iv = cipher.iv
        return cipher.doFinal(textToEncrypt.toByteArray(charset("UTF-8"))).also { encryption = it }*/*/