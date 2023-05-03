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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
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

class MainActivity : ComponentActivity() {

    val logMain = arrayListOf<Log>()
    lateinit var databaseServiceMain: LocalRepository

    companion object {

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
            databaseServiceMain.writeLogs(logMain)
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
                SecureHomeSystemTheme {
                    appColors = if (isSystemInDarkTheme()) darkColors else lightColors

                    val variantColor = MaterialTheme.colors.primaryVariant
                    val uiController = rememberSystemUiController()
                    SideEffect { uiController.setStatusBarColor(variantColor) }

                    databaseServiceMain = get()
                    SecureHomeSystem()
                }
            }
        }


    }
}

@Composable
fun SecureHomeSystem() {

    val databaseServiceMain = get<LocalRepository>()
    val navController = rememberNavController()
    KoinNavHost(navController = navController, startDestination = MyScreens.SignUpScreen.route) {

        composable(MyScreens.SignUpScreen.route) {

            HomeScreen()

//            if(databaseServiceMain.readFromLocal(RouteToGo) == "null") {
//                SignUpScreen()
//            } else {
//                WiredZoneScreen()
//            }

        }

        composable(MyScreens.ChangePasswordScreen.route) {
            ChangePasswordScreen()
        }

        composable(MyScreens.MembersScreen.route) {
            // MembersScreen()
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
            // RemoteScreen()
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