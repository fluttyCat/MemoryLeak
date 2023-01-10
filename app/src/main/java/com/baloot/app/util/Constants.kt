package com.baloot.app.util

object Constants {
    const val TRANSFORMATION = "AES/CBC/PKCS7Padding"
    const val ANDROID_KEY_STORE = "AndroidKeyStore"
    const val ALIAS = "parisaAlias"

    const val ANDROID_KEY_STORE_NAME = "AndroidKeyStore"
    const val AES_MODE_M_OR_GREATER = "AES/GCM/NoPadding"
    const val AES_MODE_LESS_THAN_M = "AES/CBC/PKCS5PADDING"
    const val KEY_ALIAS = "hamrahBankSinaKeyNAME"
    const val CHARSET_NAME = "UTF-8"
    const val KEY_ENCRYPTION_ALGORITHM = "RSA"
    const val RSA_MODE = "RSA/ECB/PKCS1Padding"
    const val CIPHER_PROVIDER_NAME_ENCRYPTION_DECRYPTION_RSA = "AndroidOpenSSL"
    const val CIPHER_PROVIDER_NAME_ENCRYPTION_DECRYPTION_AES = "BC"
    const val KEY_CIPHER_MARSHMALLOW_PROVIDER = "AndroidKeyStoreBCWorkaround"
    const val SHARED_PREFERENCE_NAME = "YOUR-EncryptedKeysSharedPreferences"
    const val ENCRYPTED_KEY_NAME = "ENCRYPTED_KEY_NAME"
    val LOG_TAG = Cryptography::class.java.name
    val FIXED_IV = byteArrayOf(
        55, 54, 53, 52, 51, 50,
        49, 48, 47,
        46, 45, 44
    )
    val s_keyInitLock = Any()
}