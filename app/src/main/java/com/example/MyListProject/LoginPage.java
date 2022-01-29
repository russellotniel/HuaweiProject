package com.example.MyListProject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.huawei.hms.support.api.entity.common.CommonConstant;

public class LoginPage extends AppCompatActivity {

    private AccountAuthService mAuthService;
    private AccountAuthParams mAuthParam;
    private static final int REQUEST_CODE_SIGN_IN = 1000;
    private static final String TAG = "Account";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        findViewById(R.id.HuaweiIdAuthButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                silentSignInByHwId();
            }
        });

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void silentSignInByHwId() {
        mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setEmail()
                .setAuthorizationCode()
                .createParams();

        mAuthService = AccountAuthManager.getService(this, mAuthParam);

        Task<AuthAccount> task = mAuthService.silentSignIn();
        task.addOnSuccessListener(new OnSuccessListener<AuthAccount>() {
            @Override
            public void onSuccess(AuthAccount authAccount) {
                dealWithResultOfSignIn(authAccount);
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    Intent signInIntent = mAuthService.getSignInIntent();
                    signInIntent.putExtra(CommonConstant.RequestParams.IS_FULL_SCREEN, true);
                    startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
                }
            }
        });
    }

    private void dealWithResultOfSignIn(AuthAccount authAccount) {
        Log.i(TAG,"code getAuthorizationCode: -> "+authAccount.getAuthorizationCode());
        Log.i(TAG,"code getAvatarUri -> "+authAccount.getAvatarUri());
        Log.i(TAG,"code getDisplayName -> "+authAccount.getDisplayName());
        Log.i(TAG,"code getEmail -> "+authAccount.getEmail());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            Log.i(TAG, "onActivitResult of sigInInIntent, request code: " + REQUEST_CODE_SIGN_IN);
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                AuthAccount authAccount = authAccountTask.getResult();
                dealWithResultOfSignIn(authAccount);
                Log.i(TAG, "onActivitResult of sigInInIntent, request code: " + REQUEST_CODE_SIGN_IN);
            } else {
                Log.e(TAG, "sign in failed : " +((ApiException)authAccountTask.getException()).getStatusCode());
            }
        }
    }
}