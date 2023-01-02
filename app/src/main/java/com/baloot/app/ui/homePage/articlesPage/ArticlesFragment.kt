package com.baloot.app.ui.homePage.articlesPage

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.baloot.app.R
import com.baloot.app.databinding.FragmentArticlesBinding
import com.baloot.app.di.DaggerAppComponent
import com.baloot.app.encryption.IEncryption
import com.baloot.app.encryption.builder.EncryptionBuilder
import com.baloot.app.encryption.core.enums.Alias
import com.baloot.app.encryption.core.enums.CipherAlgorithm
import com.baloot.app.encryption.core.enums.EncryptionMode
import com.baloot.app.encryption.core.enums.EncryptionPadding
import com.baloot.app.ui.homePage.adapter.ArticlesAdapter
import com.baloot.app.ui.homePage.articlesPage.viewModel.ArticleViewModel
import com.baloot.app.ui.homePage.articlesPage.viewModel.ArticleViewModelImpl
import com.baloot.app.util.FooterAdapterVertical
import com.baloot.app.util.RootUtil.isDevelopmentSettingsEnabled
import com.core.base.ParentFragment
import com.core.dto.article.Article
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordConfig
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordGenerator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ArticlesFragment : ParentFragment<ArticleViewModel, FragmentArticlesBinding>(),
    View.OnClickListener {

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    lateinit var homeRepository: HomeRepository

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private val secret: String = "test_secret"
    private var encryptedStr: String? = null

    private val articlesAdapter: ArticlesAdapter by lazy {
        ArticlesAdapter {
            navigateToDetailFragment(
                it.title,
                it.description,
                it.publishedAt,
                it.urlToImage,
                it.source?.name
            )
        }
    }

    private val iEncryptionRSA: IEncryption by lazy {
        val builder = EncryptionBuilder(alias = Alias.RSA.value, type = CipherAlgorithm.RSA)
        builder.context = requireContext() //Need Only RSA on below API Lv22.
        builder.config.blockMode = EncryptionMode.ECB
        builder.config.encryptionPadding = EncryptionPadding.RSA_PKCS1
        builder.build()
    }
    private val iEncryptionAES: IEncryption by lazy {
        val builder = EncryptionBuilder(alias = Alias.AES.value, type = CipherAlgorithm.AES)
        builder.config.cipherAlgorithm = CipherAlgorithm.AES
        builder.config.blockMode = EncryptionMode.CBC
        builder.config.encryptionPadding = EncryptionPadding.PKCS7
        builder.build()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initArticleRecycler()
        subscribeArticleItems()
        generateTOTP()
        if (isDevelopmentSettingsEnabled(requireContext())) {
            testDeveloperOption()
            //Settings.Global.putString(requireActivity().contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, "0")
        }

    }

    private fun initArticleRecycler() {
        dataBinding.articleRv.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter =
                articlesAdapter.withLoadStateFooter(FooterAdapterVertical { articlesAdapter.retry() })
        }
    }

    private fun subscribeArticleItems() {
        lifecycleScope.launch {
            viewModel.getArticleData().collectLatest { bestPagingData ->
                val articleData = bestPagingData.map {
                    Article(
                        source = it.source,
                        author = it.author,
                        title = it.title,
                        description = it.description,
                        urlToImage = it.urlToImage,
                        publishedAt = it.publishedAt,
                    )
                }

                articlesAdapter.submitData(articleData)
                articlesAdapter.notifyDataSetChanged()
            }
        }
        articlesAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    dataBinding.progressBar.visibility = View.INVISIBLE
                    dataBinding.articleRv.visibility = View.VISIBLE
                }
                is LoadState.Loading -> {

                    dataBinding.articleRv.visibility = View.INVISIBLE
                    dataBinding.progressBar.visibility = View.VISIBLE
                }
                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error

                    dataBinding.progressBar.visibility = View.INVISIBLE

                    Toast.makeText(
                        requireActivity(),
                        "Load Error: ${state.error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {

                }
            }
        }
    }

    private fun navigateToDetailFragment(
        title: String?,
        desc: String?,
        publishedAt: String?,
        imageUrl: String?,
        source: String?
    ) {
        val action =
            ArticlesFragmentDirections.actionArticlesFragmentToArticlesDetailFragment(
                title = title,
                description = desc,
                published = publishedAt,
                url = imageUrl,
                source = source
            )

        findNavController().navigate(action)
    }


    private fun generateTOTP() {
        val timeStmp: Long = System.currentTimeMillis() / 1000
        val config = TimeBasedOneTimePasswordConfig(
            codeDigits = 6,
            hmacAlgorithm = HmacAlgorithm.SHA1,
            timeStep = 30,
            timeStepUnit = TimeUnit.SECONDS
        )
        val timeBasedOneTimePasswordGenerator =
            TimeBasedOneTimePasswordGenerator(secret.toByteArray(), config)
        val generatedTotp = timeBasedOneTimePasswordGenerator.generate(timestamp = timeStmp)
        Toast.makeText(requireContext(), generatedTotp, Toast.LENGTH_LONG).show()

    }


    private fun encryptRSA(plainStr: String): String {
        try {
            val plainByte = plainStr.toByteArray()
            val result = iEncryptionRSA.doEncrypt(plainByte = plainByte)
            return Base64.encodeToString(result.bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show()
        }
        return ""
    }

    private fun decryptRSA(encryptedStr: String): String {
        try {
            val encryptedByte = Base64.decode(encryptedStr, Base64.DEFAULT)
            val result = iEncryptionRSA.doDecrypt(encryptedByte = encryptedByte)
            return String(result.bytes)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show()
        }
        return ""
    }

    private fun encryptAES(plainStr: String): String {
        try {
            val plainByte = plainStr.toByteArray()
            val result = iEncryptionAES.doEncrypt(plainByte = plainByte)
            cipherIV = result.cipherIV
            return Base64.encodeToString(result.bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show()
        }
        return ""
    }

    private fun decryptAES(encryptedStr: String): String {
        try {
            val encryptedByte = Base64.decode(encryptedStr, Base64.DEFAULT)
            val result =
                iEncryptionAES.doDecrypt(encryptedByte = encryptedByte, cipherIV = cipherIV)
            return String(result.bytes)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show()
        }
        return ""
    }

    private var cipherIV: ByteArray?
        get() {
            sharedPreferences.getString("cipher_iv", null)?.let {
                return Base64.decode(it, Base64.DEFAULT)
            }
            return null
        }
        set(value) {
            val editor = sharedPreferences.edit()
            editor.putString("cipher_iv", Base64.encodeToString(value, Base64.DEFAULT))
            editor.apply()
        }

    private fun testEncryptAndDecrypt() {
        encryptAES(plainStr = secret).let {
            encryptedStr = it
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        val test = encryptedStr?.let { decryptAES(it) }
        Log.d("TAG", "onActivityCreated: $test")
    }

    private fun testDeveloperOption() {
        val view = layoutInflater.inflate(R.layout.dialog_developer_option_layout, null)
        val developerOptionAD = AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .create()
        developerOptionAD.setView(view)
        val window = developerOptionAD.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        developerOptionAD.show()
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

    override fun getResourceLayoutId(): Int = R.layout.fragment_articles

    override fun inject() {
        DaggerAppComponent.builder()
            .app(requireActivity().application)
            .build()
            .inject(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            dataBinding.aesBtn -> testEncryptAndDecrypt()
        }
    }

}