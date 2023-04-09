package ir.dunijet.securitycheckapp.service.sms

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.*
import android.telephony.SmsManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import ir.dunijet.securitycheckapp.util.SMS_SENT
import ir.dunijet.securitycheckapp.util.getFlags

class SmsRepository(private val context: Context) {

    @SuppressLint("MissingPermission")
    fun sendSms(text: String, sendTo: String, simChosen: Int = 1 ) {

        val sentPI = PendingIntent.getBroadcast(context, 0, Intent(SMS_SENT), getFlags())

        val localSubscriptionManager = SubscriptionManager.from(context)
        if (localSubscriptionManager.activeSubscriptionInfoCount > 1) {

            val localList: List<*> = localSubscriptionManager.activeSubscriptionInfoList
            val simInfo1 = localList[0] as SubscriptionInfo
            val simInfo2 = localList[1] as SubscriptionInfo

            SmsManager
                .getSmsManagerForSubscriptionId(
                if (simChosen == 1) simInfo1.subscriptionId else simInfo2.subscriptionId)
                .sendTextMessage(sendTo, null, text, sentPI, null)

        } else {

            SmsManager
                .getDefault()
                .sendTextMessage(sendTo, null, text, sentPI, null)

        }

    }

}