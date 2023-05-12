package ir.dunijet.securitycheckapp.ui

import android.R
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ir.dunijet.securitycheckapp.ui.theme.SecureHomeSystemTheme
import ir.dunijet.securitycheckapp.util.BaseActivity


class RecomposeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: intent.getBundleExtra("saved_state"))

        setContent {
            SecureHomeSystemTheme {

                Box(modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.Center) {
                    Text(modifier = Modifier.clickable {

//                        window.allowEnterTransitionOverlap = false
//                        window.allowReturnTransitionOverlap = false
//
//                        this@RecomposeActivity.recreate()

//                        val intent = Intent(this@RecomposeActivity, this@RecomposeActivity::class.java)
//                        val options = ActivityOptionsCompat.makeCustomAnimation(
//                            this@RecomposeActivity,
//                            R.anim.fade_in,
//                            R.anim.fade_out
//                        )
//                        startActivity(intent, options.toBundle())

                    }, text = "hello world")
                }

            }
        }

    }

    fun transitionRecreate() {
        val bundle = Bundle()
        onSaveInstanceState(bundle)
        val intent = Intent(this, javaClass)
        intent.putExtra("saved_state", bundle)
        finish()
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    fun recreateSmoothly(activity :RecomposeActivity) {
        val mCurrentActivity: Activity = activity
        val intent: Intent = activity.intent
        val tempBundle = Bundle()
        intent.putExtra("bundle", tempBundle)

        mCurrentActivity.finish()
        mCurrentActivity.overridePendingTransition(R.anim.fade_out, R.anim.fade_in)
        mCurrentActivity.startActivity(intent)
    }

}

