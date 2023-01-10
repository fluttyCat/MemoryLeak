package com.parisa.app.ui.homePage.profile.bottomSheet

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.parisa.app.R
import com.parisa.app.databinding.BottomShitLayoutBinding
import com.core.base.ParentBottomSheetDialogFragment
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import com.parisa.app.di.DaggerAppComponent
import com.parisa.app.ui.homePage.profile.viewModel.ProfileViewModel
import com.parisa.app.ui.homePage.profile.viewModel.ProfileViewModelImpl
import javax.inject.Inject

class BottomSheetDialogFragment(
    val onSuccess: () -> Unit,
) :
    ParentBottomSheetDialogFragment<ProfileViewModel, BottomShitLayoutBinding>() {

    @Inject
    lateinit var homeRepository: HomeRepository

    @Inject
    lateinit var localRepository: LocalRepository

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun getResourceLayoutId(): Int = R.layout.bottom_shit_layout


    override fun getViewModelClass(): Class<ProfileViewModel> =
        ProfileViewModel::class.java

    override fun getFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModelImpl(
                    application = requireActivity().application,
                    localRepository = localRepository,
                    homeRepository = homeRepository
                ) as T
            }
        }
    }

    override fun inject() {
        DaggerAppComponent.builder()
            .app(requireActivity().application)
            .build()
            .inject(this)
    }

    override fun showProgress(tag: String) {
        super.showProgress(tag)
    }

    override fun hideProgress(tag: String) {
        super.hideProgress(tag)
    }

    override fun showError(tag: String, error: String) {
        super.showError(tag, error)
    }

    enum class BottomSheetType { EDIT, ALL }
}