package com.baloot.app.ui.homePage.crypthography

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.baloot.app.R
import com.baloot.app.databinding.FragmentCryptographyBinding
import com.baloot.app.di.DaggerAppComponent
import com.baloot.app.ui.homePage.crypthography.viewModel.CryptographyViewModel
import com.baloot.app.ui.homePage.crypthography.viewModel.CryptographyViewModelImpl
import com.baloot.app.util.Constants.key
import com.baloot.app.util.Cryptography
import com.core.base.ParentFragment
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import com.core.utils.Preference
import com.core.utils.SecurityHelper
import kotlinx.android.synthetic.main.fragment_cryptography.*
import java.security.*
import javax.crypto.*
import javax.inject.Inject


class CryptoGraphFragment : ParentFragment<CryptographyViewModel, FragmentCryptographyBinding>(),
    View.OnClickListener {

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    lateinit var homeRepository: HomeRepository

    @Inject
    lateinit var preferences: Preference

    @Inject
    lateinit var securityHelper: SecurityHelper


    private lateinit var cryp: Cryptography

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        clickListeners()
        cryp = Cryptography(requireContext())
    }


    private fun encryptText() {

        if (!preferences.shredPrefHasKey(key)) {
            val encryptedText = cryp.encryptData(dataBinding.encEt.text.toString())
            dataBinding.encTextTv.text = encryptedText
            dataBinding.decBtn.isEnabled = true

            preferences.put(key, encryptedText)
            dataBinding.decTextTv.text = cryp.decryptData(encryptedText)
            dataBinding.encEt.text.clear()

        } else {

            val encText = preferences.getString(key)
            val decText = cryp.decryptData(encText)
            dataBinding.decTextTv.text = decText
            viewModel.decryptedText = decText

            preferences.put(key, viewModel.decryptedText + "# ${dataBinding.encEt.text}")
            dataBinding.encEt.text.clear()
            dataBinding.decTextTv.text = preferences.getString(key)
            //cryp.encryptData(preferences.getString(key))

        }


    }

    private fun decryptText() {
        dataBinding.decTextTv.text = cryp.decryptData(dataBinding.encTextTv.text.toString())
    }


    private fun putEncString() {

        if (!preferences.shredPrefHasKey(key)) {

            preferences.putEncrypt(key, dataBinding.encEt.text.toString())
            dataBinding.decTextTv.text = preferences.getDecryptedString(key)
            dataBinding.encEt.text.clear()

        } else {
            val decText = preferences.getDecryptedString(key)
            viewModel.decryptedText = decText
            dataBinding.decTextTv.text = decText
            preferences.putEncrypt(key, viewModel.decryptedText + "# ${dataBinding.encEt.text}")
            dataBinding.encEt.text.clear()
            dataBinding.decTextTv.text = preferences.getDecryptedString(key)

        }

    }

    private fun getDecString() {
        dataBinding.decTextTv.text = preferences.getDecryptedString(key = key)
    }


    override fun getViewModelClass(): Class<CryptographyViewModel> =
        CryptographyViewModel::class.java

    override fun getFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CryptographyViewModelImpl(
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
            // putEncString()
            dataBinding.decBtn -> decryptText()
            //getDecString()
        }
    }

    private fun clickListeners() {
        dataBinding.encBtn.setOnClickListener(this)
        dataBinding.decBtn.setOnClickListener(this)
    }

}