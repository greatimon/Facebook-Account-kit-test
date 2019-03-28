package com.markerroom.facebooksmstest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;

/**
 * Created by JYN on 2019-03-28.
 */
public class AccountActivity extends AppCompatActivity {

    TextView id;
    TextView infoLabel;
    TextView info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        id = findViewById(R.id.id);
        infoLabel = findViewById(R.id.info_label);
        info = findViewById(R.id.info);

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                // Account Kit ID 받기
                String accountKitId = account.getId();
                id.setText(accountKitId);

                PhoneNumber phoneNumber = account.getPhoneNumber();

                // 핸드폰번호를 받아왔다면, 보여주기
                if(account.getPhoneNumber() != null) {
                    Log.d(LoginActivity.TAG, "phoneNumber: " + phoneNumber);
                    String formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());
                    Log.d(LoginActivity.TAG, "formattedPhoneNumber: " + formattedPhoneNumber);

                    info.setText(formattedPhoneNumber);
                    infoLabel.setText(R.string.phone_label);
                }
                // 이메일 주소를 받아왔다면, 보여주기
                else {
                    String emailString = account.getEmail();

                    info.setText(emailString);
                    infoLabel.setText(R.string.email_label);
                }
            }

            @Override
            public void onError(AccountKitError accountKitError) {
                // 에러메세지 보여주기
                String toastMessage = accountKitError.getErrorType().getMessage();
                Toast.makeText(AccountActivity.this, toastMessage, Toast.LENGTH_LONG).show();
            }
        });
    }


    // 핸드폰번호를 보여주기 위한 형식으로 변환하기 위한 헬퍼 메소드
    private String formatPhoneNumber(String phoneNumber) {
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber pn = phoneNumberUtil.parse(phoneNumber, Locale.getDefault().getCountry());
            phoneNumber = phoneNumberUtil.format(pn, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        }
        catch (NumberParseException e) {
            e.printStackTrace();
        }

        return  phoneNumber;
    }

    // Account kit 로그아웃하기
    public void onLogout(View view) {

        AccountKit.logOut();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
