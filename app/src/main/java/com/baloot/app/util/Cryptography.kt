package com.baloot.app.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import com.baloot.app.util.Constants.AES_MODE_M_OR_GREATER
import com.baloot.app.util.Constants.ANDROID_KEY_STORE_NAME
import com.baloot.app.util.Constants.CHARSET_NAME
import com.baloot.app.util.Constants.FIXED_IV
import com.baloot.app.util.Constants.KEY_ALIAS
import com.baloot.app.util.Constants.s_keyInitLock
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import java.util.*
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec

class Cryptography(val context: Context) {

    @RequiresApi(api = VERSION_CODES.M)
    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchProviderException::class,
        InvalidAlgorithmParameterException::class
    )
    fun generateKeysForAPIMOrGreater() {
        val keyGenerator: KeyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEY_STORE_NAME
        )
        if (Build.VERSION.SDK_INT < VERSION_CODES.P) {
            //23- 27
            generateKeyForMToP(keyGenerator)
        }
        else {
            // >=28
            if (hasStrongBox()) {
                generateKeyForPToUp(keyGenerator)
            } else {
                generateKeyForMToP(keyGenerator)
            }
        }
        keyGenerator.generateKey()

    }


    @RequiresApi(api = VERSION_CODES.P)
    @Throws(InvalidAlgorithmParameterException::class)
    private fun generateKeyForPToUp(keyGenerator: KeyGenerator) {
        Locale.setDefault(Locale.US)
        val start = Calendar.getInstance()
        start.add(Calendar.HOUR_OF_DAY, -26)
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 99)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setKeyValidityStart(start.time)
                .setKeyValidityEnd(end.time)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setIsStrongBoxBacked(true)
                .setRandomizedEncryptionRequired(false)
                .build()
        )
    }

    @RequiresApi(api = VERSION_CODES.M)
    @Throws(InvalidAlgorithmParameterException::class)
    private fun generateKeyForMToP(keyGenerator: KeyGenerator) {
        Locale.setDefault(Locale.US)
        val start = Calendar.getInstance()
        start.add(Calendar.HOUR_OF_DAY, -26)
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 99)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setKeyValidityStart(start.time)
                .setKeyValidityEnd(end.time)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE) // NOTE no Random IV. According to above this is less secure but acceptably so.
                .setRandomizedEncryptionRequired(false)
                .build()
        )
    }

     @RequiresApi(api = VERSION_CODES.P)
     private fun hasStrongBox(): Boolean {
         return context.packageManager
             .hasSystemFeature(PackageManager.FEATURE_STRONGBOX_KEYSTORE)
     }

    @Throws(
        CertificateException::class,
        NoSuchAlgorithmException::class,
        IOException::class,
        KeyStoreException::class,
        UnrecoverableKeyException::class
    )
    private fun getSecretKeyAPIMorGreater(): Key? {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_NAME)
        keyStore.load(null)
        return keyStore.getKey(KEY_ALIAS, null)
    }

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        UnrecoverableEntryException::class,
        CertificateException::class,
        KeyStoreException::class,
        IOException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        NoSuchProviderException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
    fun encryptData(stringDataToEncrypt: String?) : String{
        initKeys()
        requireNotNull(stringDataToEncrypt) { "Data to be decrypted must be non null" }
        var cipher: Cipher? = null
        if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
            cipher = Cipher.getInstance(AES_MODE_M_OR_GREATER)
            cipher.init(
                Cipher.ENCRYPT_MODE, getSecretKeyAPIMorGreater(),
                GCMParameterSpec(128, FIXED_IV)
            )
        }
        val encodedBytes =
            cipher?.doFinal(stringDataToEncrypt.toByteArray(charset(CHARSET_NAME)))
        return Base64.encodeToString(encodedBytes, Base64.DEFAULT)

    }

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        UnrecoverableEntryException::class,
        CertificateException::class,
        KeyStoreException::class,
        IOException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        NoSuchProviderException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
    fun decryptData(encryptedData: String?): String {
        initKeys()
        requireNotNull(encryptedData) { "Data to be decrypted must be non null" }
        val encryptedDecodedData = Base64.decode(encryptedData, Base64.DEFAULT)
        val c: Cipher
        try {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
                c = Cipher.getInstance(AES_MODE_M_OR_GREATER)
                c.init(
                    Cipher.DECRYPT_MODE,
                    getSecretKeyAPIMorGreater(),
                    GCMParameterSpec(128, FIXED_IV)
                )
                val decodedBytes = c.doFinal(encryptedDecodedData)
                return String(decodedBytes, charset(CHARSET_NAME))
            }
        } catch (e: InvalidKeyException) {
            // Since the keys can become bad (perhaps because of luck screen change)
            // drop keys in this case.
            removeKeys()
        }
        return ""
    }


    @Throws(
        KeyStoreException::class,
        CertificateException::class,
        NoSuchAlgorithmException::class,
        IOException::class,
        NoSuchProviderException::class,
        InvalidAlgorithmParameterException::class,
        UnrecoverableEntryException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class
    )
    private fun initKeys() {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_NAME)
        keyStore.load(null)

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            initValidKeys()
        } else {
            var keyValid = false
            try {
                val keyEntry = keyStore.getEntry(KEY_ALIAS, null)
                if (keyEntry is KeyStore.SecretKeyEntry &&
                    Build.VERSION.SDK_INT >= VERSION_CODES.M
                ) {
                    keyValid = true
                }
            } catch (e: NullPointerException) {

            } catch (e: UnrecoverableKeyException) {

            }
            if (!keyValid) {
                synchronized(s_keyInitLock) {

                    // System upgrade or something made key invalid
                    removeKeys(keyStore)
                    initValidKeys()
                }
            }
        }
    }

    @Throws(
        KeyStoreException::class,
        CertificateException::class,
        NoSuchAlgorithmException::class,
        IOException::class
    )
    fun removeKeys() {
        synchronized(s_keyInitLock) {
            val keyStore =
                KeyStore.getInstance(ANDROID_KEY_STORE_NAME)
            keyStore.load(null)
            removeKeys(keyStore)
        }
    }

    @Throws(KeyStoreException::class)
    fun removeKeys(keyStore: KeyStore) {
        keyStore.deleteEntry(KEY_ALIAS)
    }

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchProviderException::class,
        InvalidAlgorithmParameterException::class,
        CertificateException::class,
        UnrecoverableEntryException::class,
        NoSuchPaddingException::class,
        KeyStoreException::class,
        InvalidKeyException::class,
        IOException::class
    )
    private fun initValidKeys() {
        synchronized(s_keyInitLock) {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
                generateKeysForAPIMOrGreater()
            }
        }
    }


}