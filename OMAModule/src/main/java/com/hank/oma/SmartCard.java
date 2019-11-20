package com.hank.oma;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.core.util.Preconditions;

import com.hank.oma.core.BaseSmartCard;
import com.hank.oma.core.EnumReaderType;
import com.hank.oma.core.SmartCardByGoogle;
import com.hank.oma.core.SmartCardBySimalliance;
import com.hank.oma.entity.CardResult;
import com.hank.oma.exception.CardException;
import com.hank.oma.utils.Hex;

import java.util.Objects;


/**
 * @author hank
 * 2019-11-19
 * OpenModileAPI封装，已根据Android版本进行适配
 */
public final class SmartCard {
    @NonNull
    private BaseSmartCard smartCardInterface;

    private static class SingletonHolder {
        private final static SmartCard instance = new SmartCard();
    }

    public static SmartCard getInstance() {
        return SingletonHolder.instance;
    }

    private SmartCard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            smartCardInterface = new SmartCardByGoogle();
        } else {
            smartCardInterface = new SmartCardBySimalliance();
        }
    }

    /**
     * 执行APDU指令
     */
    @WorkerThread
    public CardResult execute(@NonNull String command) {
        Preconditions.checkNotNull(command, "command must not be null");
        Preconditions.checkArgument(command.length() > 6, "command format error");
        command = command.toUpperCase();
        return smartCardInterface.execute(command);
    }

    /**
     * 执行APDU指令
     */
    @WorkerThread
    public CardResult execute(@NonNull byte[] command) {
        return execute(Objects.requireNonNull(Hex.bytesToHexString(command)));
    }

    /**
     * 执行APDU指令
     */
    @WorkerThread
    public CardResult execute(@NonNull String command, @NonNull String expSw) {
        Preconditions.checkNotNull(expSw, "expSw must not be null");
        CardResult cardResult = execute(command);
        if (cardResult.getStatus() == 0) {
            try {
                cardResult.check(expSw);
            } catch (CardException e) {
                cardResult.setStatus(-2);
                cardResult.setMessage(e.getMessage());
            }
        }
        return cardResult;
    }

    /**
     * 关闭SE Channel
     */
    public void closeChannel() {
        smartCardInterface.closeChannel();
    }

    /**
     * 关闭SE服务
     */
    public void closeService() {
        smartCardInterface.closeService();
    }

    /**
     * 获取Reader类型
     */
    public EnumReaderType getmReaderType() {
        return smartCardInterface.getmReaderType();
    }

    /**
     * 设置Reader类型
     */
    public void setmReaderType(@NonNull EnumReaderType mReaderType) {
        smartCardInterface.setmReaderType(mReaderType);
    }
}
