package com.hank.oma.core;

import android.content.Context;

import com.hank.oma.entity.CardResult;

public abstract class BaseSmartCard {
    private EnumReaderType mReaderType;
    final int STATUS_CODE_SUCCESS = 0;
    final int STATUS_CODE_FAIL = -1;

    public abstract CardResult execute(String command);

    public abstract void closeChannel();

    public abstract void closeService();

    CardResult operFail(String errorMsg) {
        closeChannel();
        return new CardResult(STATUS_CODE_FAIL, errorMsg);
    }

    public EnumReaderType getmReaderType() {
        if (mReaderType == null) {
            return EnumReaderType.READER_TYPE_ESE;
        }
        return mReaderType;
    }

    public void setmReaderType(EnumReaderType mReaderType) {
        this.mReaderType = mReaderType;
    }
}
