package com.chinet.meethere;


import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;

import java.io.IOException;

public class OnTokenAcquired implements AccountManagerCallback<Bundle> {

    private Bundle bundle;


    @Override
    public void run(AccountManagerFuture<Bundle> result) {

        try {
            bundle = result.getResult();
        } catch (OperationCanceledException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            e.printStackTrace();
        }


    }
}
