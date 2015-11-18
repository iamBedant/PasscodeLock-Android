package org.wordpress.passcodelock;

import android.content.Intent;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class PasscodeUnlockActivity extends AbstractPasscodeKeyboardActivity {
    
    @Override
    public void onBackPressed() {
        AppLockManager.getInstance().getCurrentAppLock().forcePasswordLock();
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(i);
        finish();
    }


    @Override
    protected void onPinLockInserted() {
        String passLock = mPinCodeField.getText().toString();
        if(AppLockManager.getInstance().getCurrentAppLock().verifyPassword(passLock)) {
            authenticationSucceeded();
        } else {
            authenticationFailed();
        }
    }

    @Override
    protected FingerprintManagerCompat.AuthenticationCallback getFingerprintCallback() {
        return new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                super.onAuthenticationHelp(helpMsgId, helpString);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                authenticationSucceeded();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                authenticationFailed();
            }
        };
    }

    private void authenticationSucceeded() {
        setResult(RESULT_OK);
        finish();
    }

    private void authenticationFailed() {
        Thread shake = new Thread() {
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(PasscodeUnlockActivity.this, R.anim.shake);
                findViewById(R.id.AppUnlockLinearLayout1).startAnimation(shake);
                showPasswordError();
                mPinCodeField.setText("");
            }
        };
        runOnUiThread(shake);
    }
}