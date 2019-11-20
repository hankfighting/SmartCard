package com.hank.oma.entity;

import android.content.Intent;
import android.text.TextUtils;

import com.hank.oma.exception.CardException;
import com.hank.oma.utils.LogUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardResult {
    private String sw;
    private String rapdu;
    private int status;
    private String message;

    public String getRapdu() {
        return rapdu;
    }

    public void setRapdu(String rapdu) {
        this.rapdu = rapdu;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSw() {
        return sw;
    }

    public void setSw(String sw) {
        this.sw = sw;
    }

    public CardResult(int status, String message, String rapdu) {
        this.sw = rapdu.substring(rapdu.length() - 4);
        this.rapdu = rapdu.substring(0, rapdu.length() - 4);
        this.status = status;
        this.message = message;
    }

    public CardResult(int status, String message) {
        super();
        this.status = status;
        this.message = message;
    }


    public void check(String expSw) throws CardException {
        LogUtil.e("expSw:" + expSw);
        LogUtil.e("sw:" + this.sw);
        if (!isMatchSw(expSw, this.sw)) {
            throw new CardException(Integer.parseInt(this.sw, 16));
        }
    }

    private boolean isMatchSw(String expSw, String sw) {
        Pattern p = Pattern.compile(expSw);
        Matcher m = p.matcher(sw);
        return m.find();
    }

}
