package com.curofy.anubhav.assignmentproject.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.curofy.anubhav.assignmentproject.presenters.MainSignInPresenter;
import com.curofy.anubhav.assignmentproject.presenters.OtpPresenter;

/**
 * Created by anubhav on 5/4/18.
 */

public class IncomingSms extends BroadcastReceiver {

    final SmsManager sms;
    private OtpPresenter otpPresenter;

    public IncomingSms(OtpPresenter otpPresenter) {
        sms = SmsManager.getDefault();
        this.otpPresenter = otpPresenter;
    }



    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    otpPresenter.processSms(message);
                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}