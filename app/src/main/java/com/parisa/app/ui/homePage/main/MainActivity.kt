package com.parisa.app.ui.homePage.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.core.base.ParentActivity
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import com.parisa.app.R
import com.parisa.app.databinding.ActivityMainBinding
import com.parisa.app.di.DaggerAppComponent
import com.parisa.app.ui.homePage.SampleSingletonClass
import com.parisa.app.ui.homePage.main.viewModel.MainViewModel
import com.parisa.app.ui.homePage.main.viewModel.MainViewModelImpl
import javax.inject.Inject


class MainActivity : ParentActivity<MainViewModel, ActivityMainBinding>() {

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    lateinit var homeRepository: HomeRepository

    private lateinit var navController: NavController
    private var broadcastReceiver: BroadcastReceiver? = null

    private var singletonSampleClass: SampleSingletonClass? = null


    /*
     * Always remember to NEVER use static variables for views or activities or contexts.
     */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHost) as NavHostFragment? ?: return

        navController = host.navController

        dataBinding.bottomNavigation.setupWithNavController(navController)

        /*
    * Option 1: Do not pass activity context to the Singleton class.
    * Instead pass application Context
    *
    * Option 2: If you really have to use activity context,
    * then when the activity is destroyed,
    * ensure that the context you passed to the singleton class is set to null.
    */
        singletonSampleClass = SampleSingletonClass().getInstance(context = this)

    }


    override fun getFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewModelImpl(
                    application = application,
                    localRepository = localRepository,
                    homeRepository = homeRepository
                ) as T
            }
        }
    }

    override fun inject() {
        DaggerAppComponent.builder()
            .app(application)
            .build()
            .inject(this)
    }

    override fun getResourceLayoutId(): Int = R.layout.activity_main

    override fun getViewModelClass(): Class<MainViewModel> = MainViewModel::class.java

    override fun showProgress(tag: String) {
        super.showProgress(tag)

    }

    override fun hideProgress(tag: String) {
        super.hideProgress(tag)
    }

    override fun showError(tag: String, error: String) {
        super.showError(tag, error)
        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
    }

    private fun registerBroadCastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                //your receiver code goes here!
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter("SmsMessage.intent.MAIN"))
    }

    override fun onStart() {
        super.onStart()

        /*
        * pointed out, if the broadcast Receiver is registered in onCreate(),
        * then when the app goes into the background and resumed again,
        * the receiver will not be registered again.
        * So register the broadcastReceiver in onStart() or onResume()
        *  of the activity and unregister in onStop().
        * */
        registerBroadCastReceiver()
    }

    @Override
    override fun onStop() {
        super.onStop()

        /*
         * Uncomment this line in order to avoid memory leak.
         * You need to unregister the broadcast receiver since the broadcast receiver keeps a reference of the activity.
         * Now when its time for your Activity to die, the Android framework will call onDestroy() on it
         * but the garbage collector will not be able to remove the instance from memory because the broadcastReceiver
         * is still holding a strong reference to it.
         * */

        /*if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver)
        }*/

    }


    override fun onDestroy() {
        super.onDestroy()

        /*
     * Option 2: Unregister the singleton class here i.e. if you pass activity context to the Singleton class,
     * then ensure that when the activity is destroyed, the context in the singleton class is set to null.
     */
        singletonSampleClass?.onDestroy()
    }
}
