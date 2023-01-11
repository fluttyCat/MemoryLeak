package com.baloot.app.ui.homePage.crypthography

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.baloot.app.R
import com.baloot.app.databinding.FragmentCryptographyBinding
import com.baloot.app.di.DaggerAppComponent
import com.baloot.app.ui.homePage.crypthography.viewModel.CryptographyViewModel
import com.baloot.app.ui.homePage.crypthography.viewModel.CryptographyViewModelImpl
import com.baloot.app.util.Cryptography
import com.core.base.ParentFragment
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import kotlinx.android.synthetic.main.fragment_cryptography.*
import java.security.*
import javax.crypto.*
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.M)
class CryptoGraphFragment : ParentFragment<CryptographyViewModel, FragmentCryptographyBinding>(),
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
        dataBinding.encTextTv.text = cryp.encryptData(dataBinding.encEt.text.toString())
        dataBinding.decBtn.isEnabled = true
    }

    private fun decryptText() {
        dataBinding.decTextTv.text = cryp.decryptData(dataBinding.encTextTv.text.toString())
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
            dataBinding.decBtn -> decryptText()
        }
    }

}