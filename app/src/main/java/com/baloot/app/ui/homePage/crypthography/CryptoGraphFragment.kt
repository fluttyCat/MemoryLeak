package com.baloot.app.ui.homePage.crypthography

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.baloot.app.util.Encrypt
import com.core.base.ParentFragment
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import kotlinx.android.synthetic.main.fragment_cryptography.*
import java.io.IOException
import java.security.*
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
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

    private lateinit var encrypt: Encrypt


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dataBinding.encBtn.setOnClickListener(this)
    }


    private fun encryptText() {

        val text = dataBinding.encEt.text.toString()
        Log.d("text", "Encrypted Text:$text")

        try {
            encrypt = Encrypt()
            val encryptedText: ByteArray? =
                encrypt.encryptText(alias = ALIAS, textToEncrypt = text)
            Log.d("s@urac", "Encrypted Text:$encryptedText")

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
        }
    }

}