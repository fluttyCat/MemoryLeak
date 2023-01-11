package com.baloot.app.ui.splashPage.splashFragment

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.baloot.app.R
import com.baloot.app.databinding.FragmentSplashBinding
import com.baloot.app.di.DaggerAppComponent
import com.baloot.app.ui.homePage.main.MainActivity
import com.baloot.app.ui.splashPage.splashFragment.viewModel.SplashViewModel
import com.baloot.app.ui.splashPage.splashFragment.viewModel.SplashViewModelImpl
import com.core.base.ParentFragment
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class SplashFragment : ParentFragment<SplashViewModel, FragmentSplashBinding>() {

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    lateinit var homeRepository: HomeRepository

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navigateToHomeActivity()
    }

    private fun navigateToHomeActivity() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(1000)
            val intentToHome =
                Intent(requireActivity(), MainActivity::class.java).apply {
                    startActivity(this)
                }

        }
    }

    override fun getViewModelClass(): Class<SplashViewModel> = SplashViewModel::class.java

    override fun getFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SplashViewModelImpl(
                    application = requireActivity().application,
                    localRepository = localRepository,
                    homeRepository = homeRepository
                ) as T
            }
        }
    }

    override fun getResourceLayoutId(): Int = R.layout.fragment_splash

    override fun inject() {
        DaggerAppComponent.builder()
            .app(requireActivity().application)
            .build()
            .inject(this)
    }


}