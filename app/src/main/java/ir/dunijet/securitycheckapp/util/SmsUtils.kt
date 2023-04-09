package ir.dunijet.securitycheckapp.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsManager
import android.telephony.SmsMessage
import androidx.activity.ComponentActivity
import ir.dunijet.securitycheckapp.model.data.Log

fun smsSentListener(
    myContext: Context,
    route: String,
    isLoading: (Boolean) -> Unit,
    log: (Log) -> Unit
): BroadcastReceiver {
    return object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            when (resultCode) {

                ComponentActivity.RESULT_OK -> {
                    log.invoke(Log(text = route + "-" + "sms send" + "-" + "RESULT_OK"))
                }

                SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                    log.invoke(Log(text = route + "-" + "sms not send" + "-" + "GENERIC_FAILURE"))
                    myContext.showToast("مشکلی در ارسال پیامک وجود دارد")
                    isLoading.invoke(false)
                }

                SmsManager.RESULT_ERROR_NO_SERVICE -> {
                    log.invoke(Log(text = route + "-" + "sms not send" + "-" + "NO_SERVICE"))
                    myContext.showToast("سیگنال موبایل ضعیف است")
                    isLoading.invoke(false)
                }

                SmsManager.RESULT_ERROR_NULL_PDU -> {
                    log.invoke(Log(text = route + "-" + "sms not send" + "-" + "NULL_PDU"))
                    myContext.showToast("مشکلی در ارسال پیامک وجود دارد")
                    isLoading.invoke(false)
                }

                SmsManager.RESULT_ERROR_RADIO_OFF -> {
                    log.invoke(Log(text = route + "-" + "sms not send" + "-" + "RADIO_OFF"))
                    myContext.showToast("لطفا حالت هواپیما را خاموش کنید")
                    isLoading.invoke(false)
                }
            }
        }
    }
}


fun smsReceivedListener(
    engineNumber: String,
    serial: String,
    result: (String) -> Unit
): BroadcastReceiver {

    return object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {
                for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {

                    val msgBody = smsMessage.messageBody
                    val msgSender = smsMessage.originatingAddress.toString()

                    if (msgSender.correctNumber() == engineNumber) {

                        if (msgBody.contains(serial)) {
                            result.invoke(msgBody)
                        }

                    }

                }
            }
        }

    }
}

fun smsReceivedListenerLong(
    serial: String,
    result: (String) -> Unit
): BroadcastReceiver {

    return object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent) {
            var mySmsText: String = ""

            if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {

                val bundle = intent.extras
                if (bundle != null) {

                    val pdus = bundle["pdus"] as Array<*>?
                    val messages = arrayOfNulls<SmsMessage>(pdus!!.size)

                    for (i in pdus.indices)
                        messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)

                    if (messages.isNotEmpty()) {

                        val content = StringBuffer()
                        for (sms in messages)
                            content.append(sms!!.displayMessageBody)

                        mySmsText = content.toString()
                    }

                    // use mySmsText
                    if (mySmsText.contains(serial)) {
                        result.invoke(mySmsText)
                    }

                }
            }
        }
    }
}
