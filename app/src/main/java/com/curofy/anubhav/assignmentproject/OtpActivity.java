package com.curofy.anubhav.assignmentproject;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.curofy.anubhav.assignmentproject.models.CustomResponseModel;
import com.curofy.anubhav.assignmentproject.models.SignInModel;
import com.curofy.anubhav.assignmentproject.presenters.OtpPresenter;
import com.curofy.anubhav.assignmentproject.receivers.IncomingSms;
import com.curofy.anubhav.assignmentproject.views.OtpView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by anubhav on 5/4/18.
 */

public class OtpActivity extends AppCompatActivity implements OtpView{

    private static final int PERMISSION_SMS_STATE = 1079;
    @BindView(R.id.et_otp_input_1)
    EditText etOtpValue1;
    @BindView(R.id.et_otp_input_2)
    EditText etOtpValue2;
    @BindView(R.id.et_otp_input_3)
    EditText etOtpValue3;
    @BindView(R.id.et_otp_input_4)
    EditText etOtpValue4;
    @BindView(R.id.tv_resend_otp)
    TextView tvResendOtp;
    @BindView(R.id.btn_call_otp)
    Button btnOtp;
    @BindView(R.id.otp_progressBar)
    ProgressBar otpProgressBar;
    @BindView(R.id.tv_otp_header)
    TextView tvOtpHeader;
    @BindView(R.id.otp_coordinator_view)
    CoordinatorLayout mainCoordinatorLayout;

    private IncomingSms incomingSms;
    private OtpPresenter otpPresenter;
    private CustomResponseModel customResponseModel;
    private SignInModel userModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        initializeViews();
    }

    private void initializeViews() {
        ButterKnife.bind(this);
        if(getIntent()!=null && getIntent().getExtras()!=null){
            customResponseModel = (CustomResponseModel) getIntent().getSerializableExtra(MainActivity.RESPONSE_MODEL_KEY);
            userModel = (SignInModel) getIntent().getSerializableExtra(MainActivity.USER_MODEL_KEY);
            tvOtpHeader.setText(String.format(getResources().getString(R.string.we_have_sent_a_verification_code_on_your_number),userModel.getContactNo()));
        }
        otpPresenter = new OtpPresenter(this,this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        }
        Toast.makeText(this, "Resend will be enabled after 10 secs.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvResendOtp.setEnabled(true);
                tvResendOtp.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.resend_enable),null,null,null);
                tvResendOtp.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        },10000);

    }

    private void registerSmsReceiver() {
        incomingSms = new IncomingSms(otpPresenter);
        registerReceiver(incomingSms,new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(incomingSms!=null) {
            unregisterReceiver(incomingSms);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        String[] permissions = {Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS};
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions,PERMISSION_SMS_STATE);
        }else{
            registerSmsReceiver();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_SMS_STATE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    registerSmsReceiver();
                }
                break;
        }
    }


    @Override
    public boolean checkAllOtpFields() {
        return etOtpValue1.getText().toString().trim().length() > 0 &&
                etOtpValue2.getText().toString().trim().length() > 0 &&
                etOtpValue3.getText().toString().trim().length() > 0 &&
                etOtpValue4.getText().toString().trim().length() > 0;

    }

    @Override
    public void fillOtpValueFromSms(int[] otp) {
      etOtpValue1.setText(String.valueOf(otp[0]));
      etOtpValue2.setText(String.valueOf(otp[1]));
      etOtpValue3.setText(String.valueOf(otp[2]));
      etOtpValue4.setText(String.valueOf(otp[3]));
    }

    @Override
    public void navigateToVerification() {
       Intent intent = new Intent(this,VerifiedActivity.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
       startActivity(intent);
    }

    @Override
    public void toggleCallMeButtonToLetsGo() {
        userModel.setOtp(etOtpValue1.getText().toString().trim()+etOtpValue2.getText().toString().trim()+
                etOtpValue3.getText().toString().trim()+etOtpValue4.getText().toString().trim());
        btnOtp.setText(getResources().getString(R.string.verify));

    }

    @Override
    public void toggleLetsGoButtonToCallMe() {
        btnOtp.setText(getResources().getString(R.string.call_me));
    }

    @Override
    public void toggleButtonToProgress() {
        btnOtp.setVisibility(View.GONE);
        otpProgressBar.setVisibility(View.VISIBLE);
        etOtpValue1.setClickable(false);
        etOtpValue1.setEnabled(false);
        etOtpValue2.setClickable(false);
        etOtpValue2.setEnabled(false);
        etOtpValue3.setClickable(false);
        etOtpValue3.setEnabled(false);
        etOtpValue4.setClickable(false);
        etOtpValue4.setEnabled(false);
    }

    @Override
    public void toggleProgressToButton() {
        btnOtp.setVisibility(View.VISIBLE);
        otpProgressBar.setVisibility(View.GONE);
        etOtpValue1.setClickable(true);
        etOtpValue1.setEnabled(true);
        etOtpValue2.setClickable(true);
        etOtpValue2.setEnabled(true);
        etOtpValue3.setClickable(true);
        etOtpValue3.setEnabled(true);
        etOtpValue4.setClickable(true);
        etOtpValue4.setEnabled(true);
    }

    @Override
    public void showErrorMessage(String str) {
        Snackbar.make(mainCoordinatorLayout,str,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void updateTokenModel(CustomResponseModel customResponseModel) {
      if(customResponseModel!=null){
          this.customResponseModel = customResponseModel;
      }
    }

    @OnTextChanged(R.id.et_otp_input_1)
    public void evaluateOtpField_1Changes(CharSequence text){
        if(!TextUtils.isEmpty(text)){
            if(checkAllOtpFields()){
                toggleCallMeButtonToLetsGo();
            }
        }else{
            toggleLetsGoButtonToCallMe();
        }
    }

    @OnTextChanged(R.id.et_otp_input_2)
    public void evaluateOtpField_2Changes(CharSequence text){
        if(!TextUtils.isEmpty(text)){
            if(checkAllOtpFields()){
                toggleCallMeButtonToLetsGo();
            }
        }else{
            toggleLetsGoButtonToCallMe();
        }
    }
    @OnTextChanged(R.id.et_otp_input_3)
    public void evaluateOtpField_3Changes(CharSequence text){
        if(!TextUtils.isEmpty(text)){
            if(checkAllOtpFields()){
                toggleCallMeButtonToLetsGo();
            }
        }else{
            toggleLetsGoButtonToCallMe();
        }
    }
    @OnTextChanged(R.id.et_otp_input_4)
    public void evaluateOtpField_4Changes(CharSequence text){
        if(!TextUtils.isEmpty(text)){
            if(checkAllOtpFields()){
                toggleCallMeButtonToLetsGo();
            }
        }else{
            toggleLetsGoButtonToCallMe();
        }
    }

    @OnClick(R.id.btn_call_otp)
    public void processVerification(View view){
        if(btnOtp.getText().toString().equalsIgnoreCase(getResources().getString(R.string.verify))){
            otpPresenter.processVerification(userModel,customResponseModel);
        }else{
            Toast.makeText(this, "Feature not available", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.tv_resend_otp)
    public void resendOtp(View view){
        if(tvResendOtp.isEnabled()){
            Toast.makeText(this, "Otp Re-sent", Toast.LENGTH_SHORT).show();
            toggleLetsGoButtonToCallMe();
            otpPresenter.resendOtp(userModel);
        }
    }
}
