package com.baloot.app.ui.homePage.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.baloot.app.R
import com.baloot.app.databinding.FragmentProfileBinding
import com.baloot.app.di.DaggerAppComponent
import com.baloot.app.ui.homePage.profile.bottomSheet.BottomSheetDialogFragment
import com.baloot.app.ui.homePage.profile.viewModel.ProfileViewModel
import com.baloot.app.ui.homePage.profile.viewModel.ProfileViewModelImpl
import com.core.base.ParentFragment
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import javax.inject.Inject


class ProfileFragment : ParentFragment<ProfileViewModel, FragmentProfileBinding>(),
    View.OnClickListener {

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    lateinit var homeRepository: HomeRepository

    lateinit var bottomSheet: BottomSheetDialogFragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dataBinding.githubBtn.setOnClickListener(this)
        dataBinding.aboutMeBtn.setOnClickListener(this)

    }

    private fun intentToGithubPage() {
        Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/fluttyCat")).apply {
            startActivity(this)
        }
    }

    private fun openAboutMeBottomSheet() {
        bottomSheet =
            BottomSheetDialogFragment {

            }
        bottomSheet.isCancelable = true
        bottomSheet.show(
            requireActivity().supportFragmentManager,
            "bottomSheet"
        )
    }

    override fun getViewModelClass(): Class<ProfileViewModel> = ProfileViewModel::class.java

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

    override fun getResourceLayoutId(): Int = R.layout.fragment_profile

    override fun inject() {
        DaggerAppComponent.builder()
            .app(requireActivity().application)
            .build()
            .inject(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            dataBinding.githubBtn -> intentToGithubPage()
            dataBinding.aboutMeBtn -> openAboutMeBottomSheet()
        }
    }

}