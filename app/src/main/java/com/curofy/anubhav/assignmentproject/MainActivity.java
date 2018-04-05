package com.curofy.anubhav.assignmentproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.curofy.anubhav.assignmentproject.adapters.SignInPagerAdapter;
import com.curofy.anubhav.assignmentproject.models.CustomResponseModel;
import com.curofy.anubhav.assignmentproject.presenters.MainSignInPresenter;
import com.curofy.anubhav.assignmentproject.views.MainSignInView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainSignInView {


    private static final int PERMISSION_PHONE_STATE = 1090;
    public static final String RESPONSE_MODEL_KEY = "RESPONSE_MODEL_KEY";
    public static final String USER_MODEL_KEY = "USER_MODEL_KEY";
    @BindView(R.id.main_coordinator)
    CoordinatorLayout mainCoordinatorLayout;
    @BindView(R.id.rl_main_layout)
    RelativeLayout mainView;
    @BindView(R.id.main_pager)
    RelativeLayout llMainPagerView;
    @BindView(R.id.login_view_pager)
    ViewPager viewPager;
    @BindView(R.id.pager_tabs)
    TabLayout pagerTabs;
    @BindView(R.id.tv_country_selection)
    TextView country;
    @BindView(R.id.tv_country_code_selection)
    TextView countryCode;
    @BindView(R.id.et_phone_no)
    EditText contactNo;
    @BindView(R.id.btn_signIn)
    Button signIn;
    @BindView(R.id.signIn_progressBar)
    ProgressBar signInProgress;
    @BindView(R.id.bottom_view)
    LinearLayout llSignInBottomView;

    private MainSignInPresenter mainSignInPresenter;
    private SignInPagerAdapter signInPagerAdapter;
    private boolean isServiceFinishedProcessing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isServiceFinishedProcessing){
            toggleSignInBtnFromProgress();
        }
    }

    @OnClick(R.id.tv_country_selection)
    void countryChooser(View view){
        if(!TextUtils.isEmpty(contactNo.getText().toString().trim())){
            resetContactField();
        }
        mainSignInPresenter.processCountrySpinner();
    }

    @OnClick(R.id.btn_signIn)
    void signInUser(View view){
        mainSignInPresenter.processSignInRequest(contactNo.getText().toString().trim());
    }

    private void initializeViews() {
        ButterKnife.bind(this);
        mainSignInPresenter = new MainSignInPresenter(this,this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        }
        signInPagerAdapter = new SignInPagerAdapter(this, new int[]{R.drawable.tour_image,R.drawable.tour_image,
                R.drawable.tour_image,R.drawable.tour_image});
        viewPager.setAdapter(signInPagerAdapter);
        pagerTabs.setupWithViewPager(viewPager,true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
               requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_PHONE_NUMBERS},PERMISSION_PHONE_STATE);
        }else{
         mainSignInPresenter.processDeviceContactNo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_PHONE_STATE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    mainSignInPresenter.processDeviceContactNo();
                }
                break;
        }
    }

    @Override
    public void navigateToOtp(CustomResponseModel customResponseModel) {
          isServiceFinishedProcessing = true;
          Intent intent = new Intent(this,OtpActivity.class);
          intent.putExtra(RESPONSE_MODEL_KEY,customResponseModel);
          intent.putExtra(USER_MODEL_KEY,mainSignInPresenter.getSignInModel());
          startActivity(intent);
    }

    @Override
    public void showErrorMessage(String errStr) {
        Snackbar.make(mainCoordinatorLayout,errStr,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void resetContactField() {
        contactNo.setText("");
        contactNo.setHint(getResources().getString(R.string.enter_phone_no));
    }

    @Override
    public void countrySelected(int pos, String name) {
        /**Here we have assumed that the order of codes/countries are same, i.e
        * they are in order in strings.xml, or Else
        * We would have maintained a Hashmap/POJO Model
        */
        country.setText(name);
        mainSignInPresenter.getSignInModel().setCountry(name);
        countryCode.setText(mainSignInPresenter.getCountryCodes()[pos]);
        mainSignInPresenter.getSignInModel().setCountryCode(mainSignInPresenter.getCountryCodes()[pos]);

    }

    @Override
    public void toggleSignInBtnToProgress() {
        signIn.setVisibility(View.GONE);
        signInProgress.setVisibility(View.VISIBLE);
        contactNo.setClickable(false);
        contactNo.setEnabled(false);
        country.setClickable(false);
        country.setEnabled(false);
        llSignInBottomView.setClickable(false);
        llSignInBottomView.setFocusable(false);
        llSignInBottomView.setEnabled(false);
    }

    @Override
    public void toggleSignInBtnFromProgress() {
        signIn.setVisibility(View.VISIBLE);
        signInProgress.setVisibility(View.GONE);
        contactNo.setClickable(true);
        contactNo.setEnabled(true);
        country.setClickable(true);
        country.setEnabled(true);
        llSignInBottomView.setClickable(true);
        llSignInBottomView.setFocusable(true);
        llSignInBottomView.setEnabled(true);
    }

    @Override
    public void prefillContactNoIfAvail(String contact) {
        if(!TextUtils.isEmpty(contact.trim())) {
            contactNo.setText(contact.trim());
        }
    }


}
