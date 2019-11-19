package com.hank.oma;

import android.os.Build;
import com.hank.oma.core.BaseSmartCard;
import com.hank.oma.core.EnumReaderType;
import com.hank.oma.core.SmartCardByGoogle;
import com.hank.oma.core.SmartCardBySimalliance;
import com.hank.oma.entity.CardResult;

/**
 * @author hank
 * 2019-11-19
 * OpenModileAPI封装，已根据Android版本进行适配
 */
public final class SmartCard {
    private BaseSmartCard smartCardInterface;

    private static class SingletonHolder {
        private final static SmartCard instance = new SmartCard();
    }

    public static SmartCard getInstance() {
        return SingletonHolder.instance;
    }
private
     SmartCard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            smartCardInterface = new SmartCardByGoogle();
        } else {
            smartCardInterface = new SmartCardBySimalliance();
        }
    }

    /**
     * 执行APDU指令
     */
    public CardResult execute(String command) {
        return smartCardInterface.execute(command);
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
    public void setmReaderType(EnumReaderType mReaderType) {
        smartCardInterface.setmReaderType(mReaderType);
    }
}
