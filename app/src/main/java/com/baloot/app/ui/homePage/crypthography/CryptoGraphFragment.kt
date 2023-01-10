package com.baloot.app.ui.homePage.crypthography

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.baloot.app.R
import com.baloot.app.databinding.FragmentCryptographyBinding
import com.baloot.app.di.DaggerAppComponent
import com.baloot.app.ui.homePage.articlesPage.viewModel.ArticleViewModel
import com.baloot.app.ui.homePage.articlesPage.viewModel.ArticleViewModelImpl
import com.baloot.app.util.Constants.ALIAS
import com.baloot.app.util.Constants.ANDROID_KEY_STORE
import com.baloot.app.util.Constants.TRANSFORMATION
import com.baloot.app.util.Cryptography
import com.core.base.ParentFragment
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import kotlinx.android.synthetic.main.fragment_cryptography.*
import java.security.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.M)
class CryptoGraphFragment : ParentFragment<ArticleViewModel, FragmentCryptographyBinding>(),
    View.OnClickListener {

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    lateinit var homeRepository: HomeRepository

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var encryptedPairData: Pair<ByteArray, ByteArray>

    private lateinit var cryp: Cryptography

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //getKeyGenerator()

        cryp = Cryptography(requireContext())

        dataBinding.encBtn.setOnClickListener(this)
        dataBinding.decBtn.setOnClickListener(this)
    }

    private fun encryptText() {
        dataBinding.encTextTv.text = encryptWithKeyStore(dataBinding.encEt.text.toString())
    }

    private fun decryptText() {
        val iv = encryptedPairData.first
        val encryptedData = encryptedPairData.second
        dataBinding.decTextTv.text = decryptData(iv, encryptedData)
    }

    private fun encryptWithKeyStore(plainText: String): String {
        encryptedPairData = getEncryptedDataPair(plainText)
        return encryptedPairData.second.toString(Charsets.UTF_8)

    }

    private fun getKeyGenerator() {
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
        val keyGeneratorSpec = KeyGenParameterSpec.Builder(
            ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(false)
            .build()
        keyGenerator.init(keyGeneratorSpec)
        keyGenerator.generateKey()
    }

    private fun getKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore.load(null)
        val secreteKeyEntry: KeyStore.SecretKeyEntry =
            keyStore.getEntry(ALIAS, null) as KeyStore.SecretKeyEntry
        return secreteKeyEntry.secretKey
    }

    private fun getEncryptedDataPair(data: String): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getKey())

        val _iv: ByteArray = cipher.iv
        val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        return Pair(_iv, encryptedData)
    }

    private fun decryptData(iv: ByteArray, encData: ByteArray): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val keySpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), keySpec)
        return cipher.doFinal(encData).toString(Charsets.UTF_8)

    }


    override fun getViewModelClass(): Class<ArticleViewModel> = ArticleViewModel::class.java

    override fun getFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ArticleViewModelImpl(
                    application = requireActivity().application,
                    localRepository = localRepository,
                    homeRepository = homeRepository
                ) as T
            }
        }
    }

    override fun getResourceLayoutId(): Int = R.layout.fragment_cryptography

    override fun inject() {
        DaggerAppComponent.builder()
            .app(requireActivity().application)
            .build()
            .inject(this)
    }

    override fun onClick(v: View?) {
        when (v) {
               dataBinding.encBtn -> encryptText()
               dataBinding.decBtn -> decryptText()
//            dataBinding.encBtn -> {
//                dataBinding.encTextTv.text = cryp.encryptData(dataBinding.encEt.text.toString())
//            }
//            dataBinding.decBtn -> {
//                dataBinding.decTextTv.text = cryp.decryptData(dataBinding.encTextTv.text.toString())
//            }
        }
    }

}


/*    private fun encryptText() {

        val text = dataBinding.encEt.text.toString()
        Log.d("text", "Encrypted Text:$text")

        try {
            encrypt = Encrypt()
            val encryptedText: ByteArray? =
                encrypt.encryptText(alias = ALIAS, textToEncrypt = text)
            Log.d("s@urac", "Encrypted Text:$encryptedText")

            dataBinding.encTextTv.text = encryptedText.toString()


        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: SignatureException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: UnrecoverableEntryException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun decryptText() {

        try {

            decrypt = Decrypt()
            encrypt = Encrypt()


            val decryptedText = decrypt.decryptData(
                alias = ALIAS,
                encryptedData = encrypt.getEncryption(),
                encryptionIv = encrypt.getIv()
            )

            Log.d(
                "s@urac",
                "s@urac Decrypted data is:$decryptedText"
            )

            dataBinding.decTextTv.text = decryptedText

        } catch (e: UnrecoverableEntryException) {
        } catch (e: NoSuchAlgorithmException) {
        } catch (e: KeyStoreException) {
        } catch (e: NoSuchPaddingException) {
        } catch (e: NoSuchProviderException) {
        } catch (e: IOException) {
        } catch (e: InvalidKeyException) {
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        }


    }*/