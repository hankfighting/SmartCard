package com.hank.oma.core;

import android.content.Context;
import android.text.TextUtils;

import androidx.core.util.Preconditions;

import com.hank.oma.SmartCard;
import com.hank.oma.entity.CardResult;
import com.hank.oma.utils.Hex;
import com.hank.oma.utils.LogUtil;
import com.hank.oma.utils.Utils;

import org.simalliance.openmobileapi.Channel;
import org.simalliance.openmobileapi.Reader;
import org.simalliance.openmobileapi.SEService;
import org.simalliance.openmobileapi.SEService.CallBack;
import org.simalliance.openmobileapi.Session;


/**
 * @author hank
 * 2019-10-29
 * Android P之前需要调用SIMalliance的Jar实现
 * SIMalliance资源地址：https://simalliance.org/se/se-educational-resources/
 */
public final class SmartCardBySimalliance extends BaseSmartCard implements CallBack {
    private SEService mSEService;
    private boolean mServiceIsConnection = false;
    private Object mLock = new Object();
    private Channel mChannel;
    private Session mSession;
    private final String TAG = SmartCardBySimalliance.class.getSimpleName();

    /**
     * 绑定OMA服务
     *
     * @throws InterruptedException
     */
    private void bindService() throws InterruptedException {
        // 判断SEService是否已经连接
        if (mSEService == null) {
            new SEService(Utils.getApp().getApplicationContext(), this);
            LogUtil.d(TAG, "start bind SEService");
            if (!mServiceIsConnection) {
                synchronized (mLock) {
                    if (!mServiceIsConnection) {
                        LogUtil.d("thread is waiting");
                        mLock.wait();
                    }
                }
            }
        }
    }

    /**
     * 执行APDU指令
     *
     * @param command APDU指令
     */
    @Override
    public CardResult execute(String command) {
        Preconditions.checkNotNull(command, "command must not be null");
        try {
            bindService();
            return executeApduCmd(command);
        } catch (InterruptedException e) {
            LogUtil.e(TAG, e.getMessage());
            return operFail("thread error:" + e.getMessage());
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            return operFail("error:" + e.getMessage());
        }
    }


    @Override
    public void serviceConnected(SEService service) {
        LogUtil.d(TAG, "service connected");
        synchronized (mLock) {
            if (service.isConnected()) {
                LogUtil.d("bind SEService success");
                mSEService = service;
            }
            LogUtil.d(TAG, "Thread notifyAll");
            mServiceIsConnection = true;
            mLock.notifyAll();
        }
    }


    /**
     * 关闭Channel
     */
    @Override
    public void closeChannel() {
        closeChannelAndSession();
    }

    /**
     * 关闭SEService
     */
    @Override
    public void closeService() {
        closeChannel();
        try {
            if (mSEService != null && mSEService.isConnected()) {
                mSEService.shutdown();
                mSEService = null;
                mServiceIsConnection = false;
                LogUtil.i(TAG, "SEService shutdown success");
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "SEService shutdown error:" + e.getMessage());
        }
    }

    /**
     * 执行APDU指令
     *
     * @param mReuqestCommand
     * @return
     * @throws Exception
     */
    protected synchronized CardResult executeApduCmd(String mReuqestCommand) throws Exception {
        if (TextUtils.isEmpty(mReuqestCommand) || mReuqestCommand.length() < 6) {
            return new CardResult(STATUS_CODE_FAIL, "Command is null or length is not enough");
        }

        LogUtil.e(TAG, "Command APDU:" + mReuqestCommand);

        /**
         * 判断以00A404开头需开通道
         */
        if ("00A404".equalsIgnoreCase(mReuqestCommand.substring(0, 6))) {
            closeChannelAndSession();
            // 获取AID
            int totallength = Integer.parseInt(mReuqestCommand.substring(8, 10), 16);
            String aid = mReuqestCommand.substring(10, 10 + totallength * 2);
            // 打开通道
            Object[] openResult = openCurrentAvailableChannel(aid);
            int resultCode = (int) openResult[0];
            String resultDesc = (String) openResult[1];
            if (resultCode == STATUS_CODE_SUCCESS) {
                String rapdu = Hex.bytesToHexString(mChannel.getSelectResponse());
                LogUtil.e(TAG, "Response APDU：" + rapdu);
                return new CardResult(rapdu, STATUS_CODE_SUCCESS, resultDesc);
            }

            LogUtil.e(TAG, "OpenChannel Error Desc:" + resultDesc);
            return new CardResult(resultCode, resultDesc);
        }


        byte[] byteCommand = Hex.hexStringToBytes(mReuqestCommand);

        if (mChannel != null) {
            byte[] byteRapdu = mChannel.transmit(byteCommand);
            String rapdu = Hex.bytesToHexString(byteRapdu);
            LogUtil.i(TAG, "Response APDU：" + rapdu);
            return new CardResult(rapdu, STATUS_CODE_SUCCESS, null);
        } else {
            return new CardResult(STATUS_CODE_FAIL, "Channal is not open");
        }
    }

    /**
     * 打开当前选择的通道
     */
    private Object[] openCurrentAvailableChannel(String aid) throws Exception {
        Reader reader = getCurrentAvailableReader();
        // 判断通道是否存在
        if (reader == null) {
            return new Object[]{STATUS_CODE_FAIL, "selected reader not exist"};
        }
        // 判断选择的通道是否可用
        if (!reader.isSecureElementPresent()) {
            return new Object[]{STATUS_CODE_FAIL, "selected reader can not use"};
        }

        mSession = reader.openSession();
        byte[] byteAid = Hex.hexStringToBytes(aid);
        LogUtil.i(TAG, "open channel applet：" + aid);
        if (mSession != null) {
            mChannel = mSession.openLogicalChannel(byteAid);
        }

        if (mChannel == null) {
            return new Object[]{STATUS_CODE_FAIL, "channel is null"};
        }

        return new Object[]{0, "open channel success"};
    }

    /**
     * 获取当前选择的通道的Reader对象
     *
     * @return
     */
    private Reader getCurrentAvailableReader() {
        LogUtil.e(TAG, "select reader name:" + getmReaderType().getValue());
        Reader[] readers = mSEService.getReaders();
        // 判断是否有可用的通道
        if (readers == null || readers.length < 1) {
            LogUtil.e(TAG, "There is no avaliable reader");
            return null;
        }

        // 获取当前需要操作通道的Reader对象
        for (Reader reader : readers) {
            LogUtil.e(TAG, "avaliable reader name:" + reader.getName());
            if (reader.getName().startsWith(getmReaderType().getValue())) {
                return reader;
            }
        }
        return null;
    }

    /**
     * 关闭当前打开的Channel和Session
     */
    public void closeChannelAndSession() {
        try {
            if (mChannel != null && !mChannel.isClosed()) {
                mChannel.close();
                mChannel = null;
                LogUtil.i(TAG, "channel close success");
            }
        } catch (Exception e) {
            LogUtil.i(TAG, "channel close error:" + e.getMessage());
        }

        try {
            if (mSession != null && !mSession.isClosed()) {
                mSession.close();
                mSession = null;
                LogUtil.d(TAG, "session close success");
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "session close error:" + e.getMessage());
        }
    }


}
