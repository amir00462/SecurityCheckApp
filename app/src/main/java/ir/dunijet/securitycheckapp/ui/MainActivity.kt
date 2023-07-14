package ir.dunijet.securitycheckapp.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.KoinNavHost
import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.model.data.Log
import ir.dunijet.securitycheckapp.service.local.LocalRepository
import ir.dunijet.securitycheckapp.ui.theme.SecureHomeSystemTheme
import ir.dunijet.securitycheckapp.util.*
import ir.dunijet.securitycheckapp.di.myModules
import ir.dunijet.securitycheckapp.ui.features.*
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext

class MainActivity : BaseActivity() {

    val logMain = arrayListOf<Log>()
    lateinit var databaseService: LocalRepository

    companion object {

        // in outputNameScreen ->
        // 0 non , 1 add , 2 edit
        var outputName_dialogPending = 0
        var outputName_dialogPendingOutputWorking = FAKE_OUTPUT
        var outputName_newOutputName = FAKE_OUTPUT_NAME

        //  -   -   -   -   -   -   -    -   -   -   -   -   -   -   -   -   -

        // in output screen ->
        var dialogOutputTitle = "چراغ\u200Cهای حیاط"
        var dialogOutputIcon = R.drawable.ic_lamp

        var recomposition = 0
        var mainTypeModir = true
        var appColors = listOf<Color>()

        fun checkPermissions(context: Context) {

            Dexter.withContext(context)
                .withPermissions(
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.RECEIVE_SMS
                )
                .withListener(object : MultiplePermissionsListener {

                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            //context.showToast("دسترسی های لازم داده شد با تشکر")
                        } else {
                            context.showToast("برنامه برای کار نیاز به همه دسترسی ها دارد")
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                    }

                }).check()
        }
    }

    fun addLogsToDb() {

        lifecycleScope.launch {
            databaseService.writeLogs(logMain)
            logMain.clear()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContent {
            Koin(appDeclaration = {
                androidContext(this@MainActivity)
                modules(myModules)
            }) {
                databaseService = get()

                // check theme from sharedPref
                val themeInShared = databaseService.readFromLocal(key_APP_THEME)
                appColors = if (themeInShared == "null") {
                    if (isSystemInDarkTheme()) darkColors else lightColors
                } else {
                    if (themeInShared == "dark") darkColors else lightColors
                }



                SecureHomeSystemTheme {
                        val variantColor = appColors[1]
                        val uiController = rememberSystemUiController()
                        SideEffect { uiController.setStatusBarColor(variantColor) }
                        SecureHomeSystem()
                }


            }
        }
    }

    private fun isFirstRun(): Boolean {

        val readData = databaseService.readFromLocal(KEY_FIRST_RUN)
        return if (readData == "null") {
            databaseService.writeToLocal(KEY_FIRST_RUN, "-1")
            true
        } else {
            false
        }

    }

}

//@Composable
//fun CheckTheme(insideColors: List<Color>, toggleTheme: () -> Unit) {
//
//    SecureHomeSystemTheme(insideColors) {
//
//        val variantColor = MaterialTheme.colors.primaryVariant
//        val uiController = rememberSystemUiController()
//        SideEffect { uiController.setStatusBarColor(variantColor) }
//
//        SecureHomeSystem()
//
//    }
//
//}

@Composable
fun SecureHomeSystem() {

    val databaseServiceMain = get<LocalRepository>()
    val navController = rememberNavController()
    KoinNavHost(navController = navController, startDestination = MyScreens.SignUpScreen.route) {

        composable(MyScreens.SignUpScreen.route) {

            when (databaseServiceMain.readFromLocal(AuthenticatedOrNot)) {
                "null" -> {
                    SignUpScreen()
                }
                "changePassword" -> {
                    ChangePasswordScreen()
                }
                "home" -> {
                    HomeScreen()
                }
            }

        }

        composable(MyScreens.ChangePasswordScreen.route) {
            ChangePasswordScreen()
        }

        composable(MyScreens.HomeScreen.route) {
            HomeScreen()
        }

        composable(MyScreens.MembersScreen.route) {
            MembersScreen()
        }

        composable(MyScreens.SelectOutputName.route) {
            SelectOutputName()
        }

        composable(
            route = MyScreens.ChangePasswordInApp.route + "/" + "{$KEY_IS_ADMIN}",
            arguments = listOf(navArgument(KEY_IS_ADMIN) {
                type = NavType.BoolType
            })
        ) {
            ChangePasswordInApp(it.arguments!!.getBoolean(KEY_IS_ADMIN, true))
        }

        composable(MyScreens.RemoteScreen.route) {
            RemoteScreen()
        }

        composable(MyScreens.WiredZoneScreen.route) {
            WiredZoneScreen()
        }

        composable(MyScreens.WirelessZoneScreen.route) {
            WirelessZoneScreen()
        }

        composable(MyScreens.AlarmScreen.route) {
            AlarmScreen()
        }

        composable(MyScreens.OutputScreen.route) {
            OutputScreen()
        }

    }
}