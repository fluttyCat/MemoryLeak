package com.parisa.app.di

import android.app.Application
import com.parisa.app.ui.homePage.articleDetails.ArticlesDetailFragment
import com.parisa.app.ui.homePage.articlesPage.ArticlesFragment
import com.parisa.app.ui.homePage.main.MainActivity
import com.parisa.app.ui.homePage.profile.ProfileFragment
import com.parisa.app.ui.homePage.profile.bottomSheet.BottomSheetDialogFragment
import com.parisa.app.ui.splashPage.splashActivity.SplashActivity
import com.parisa.app.ui.splashPage.splashFragment.SplashFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class]
)
interface AppComponent {

    fun inject(app: Application)

    //Activity
    fun inject(app: MainActivity)
    fun inject(app: SplashActivity)

    //Fragment
    fun inject(app: SplashFragment)
    fun inject(app: ArticlesFragment)
    fun inject(app: ArticlesDetailFragment)
    fun inject(app: ProfileFragment)

    //bottom sheet
    fun inject(app: BottomSheetDialogFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun app(app: Application): Builder

        fun build(): AppComponent
    }


}
