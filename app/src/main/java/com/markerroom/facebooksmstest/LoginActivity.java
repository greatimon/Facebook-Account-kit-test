package com.markerroom.facebooksmstest;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.BaseUIManager;
import com.facebook.accountkit.ui.ButtonType;
import com.facebook.accountkit.ui.LoginFlowState;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.TextPosition;
import com.facebook.accountkit.ui.UIManager;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private static final int APP_REQUEST_CODE = 1000;
    public static final String TAG = "SG3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        android.support.v7.app.ActionBar AB= getSupportActionBar();
        if(AB != null) {
            AB.hide();
        }
        setContentView(R.layout.activity_main);

        // 엑세스 토큰이 있는지 체크
        AccessToken accessToken = AccountKit.getCurrentAccessToken();

        // 만약에 이전에 로그인되어 있었다면, 바로 'account Activity'로 넘어감
        if(accessToken != null) {
            Log.d(TAG, "accessToken: " + accessToken.toString());
            launchAccountActivity();
        }

        // 앱 퍼미션 요청
//        permissionCheck();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == APP_REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            // 로그인 에러
            if(loginResult.getError() != null) {
                String toastMessage = loginResult.getError().getErrorType().getMessage();
                Log.d(TAG, "toastMessage: " + toastMessage);
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
            }
            // 로그인이 성공 --> 'account Activity'로 넘어감
            else if(loginResult.getAccessToken() != null) {
                launchAccountActivity();
            }
        }
    }


    private void launchAccountActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }


    public void onPhoneLogin(View view) {
        onLogin(LoginType.PHONE);
    }


    public void onEmailLogin(View view) {
        onLogin(LoginType.EMAIL);
    }


    public void onLogin(final LoginType loginType) {
        final Intent intent = new Intent(this, AccountKitActivity.class);

        // 로그인타입 and 응답타입 설정
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        AccountKitActivity.ResponseType.TOKEN
                );

        /** 커스텀 UI 설정 */
//        UIManager uiManager = new SkinManager(
//                SkinManager.Skin.CONTEMPORARY, // CLASSIC or CONTEMPORARY, or TRANSLUCENT
//                getResources().getColor(R.color.colorPrimary),
//                getResources().getIdentifier("bg_wave", "drawable", getPackageName()),
//                SkinManager.Tint.WHITE, // WHITE or BLACK
//                0.85 // 0.55 ~ 0.85
//        );
//        configurationBuilder.setUIManager(uiManager);

        // 사용자 핸드폰 번호 인식해서, 핸드폰번호 기입란에 자동완성 해주기 위한 옵션설정
        // - 선행조건 : 'READ_PHONE_STATE' 퍼미션
//        configurationBuilder.setReadPhoneStateEnabled(true);

        // 문자로 받은 인증코드 자동파싱해서, 코드입력 기입란에 자동완성 해주기 위한 옵션설정
        // - 선행조건 : 'RECEIVE_SMS' 퍼미션
//        configurationBuilder.setReceiveSMS(true);

        // configure 빌드
        final AccountKitConfiguration configuration = configurationBuilder.build();

        // Account Kit activity 열기
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration);
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    private void permissionCheck() {
        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() { }
                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) { }
                })
//                .setPermissions(Manifest.permission.READ_PHONE_STATE,
//                        Manifest.permission.RECEIVE_SMS)
                .setPermissions(Manifest.permission.READ_PHONE_STATE)
                .check();
    }
}
