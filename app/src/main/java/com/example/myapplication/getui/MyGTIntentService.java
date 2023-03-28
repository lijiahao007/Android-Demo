package com.example.myapplication.getui;

import android.content.Context;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

public class MyGTIntentService extends GTIntentService {
    private final String TAG = "MyGTIntentService";

    public MyGTIntentService() {
    }

    /**
     * 个推进程启动成功回调该函数。
     * @param context
     * @param i
     */
    @Override
    public void onReceiveServicePid(Context context, int i) {
        Log.i(TAG, "onReceiveServicePid: " + i);
    }


    /**
     * 此方法用于接收和处理透传消息。透传消息个推只传递数据，不做任何处理，客户端接收到透传消息后需要自己去做后续动作处理，如通知栏展示、弹框等。
     * 如果开发者在客户端将透传消息创建了通知栏展示，建议将展示和点击回执上报给个推。
     */
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        byte[] payload = msg.getPayload();
        String data = new String(payload);
        Log.d(TAG, "receiver payload = " + data);//透传消息文本内容

        //taskid和messageid字段，是用于回执上报的必要参数。详情见下方文档“6.2 上报透传消息的展示和点击数据”
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
    }

    /**
     * 上报个推透传消息的展示回执。如果透传消息本地创建通知栏消息“展示”了，则调用此方法上报。
     */
    public boolean pushGtShow(Context context, String taskid ,String messageid) {
        int gtactionid = 60001;//gtactionid传入60001表示个推渠道消息展示了
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, gtactionid);
        return result;
    }

    /**
     * 上报个推透传消息的点击回执。如果透传消息本地创建通知栏消息被“点击”了，则调用此方法上报。
     */
    public boolean pushGtClick(Context context, String taskid ,String messageid) {
        int gtactionid = 60002;//gtactionid传入60002表示个推渠道消息点击了
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, gtactionid);
        return result;
    }


    // 接收 cid
    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    // cid 离线上线通知
    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Log.i(TAG, "onReceiveOnlineState: online:" + online);
    }

    // 各种事件处理回执
    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.i(TAG, "onReceiveCommandResult: cmdMessage:" + cmdMessage.getAction());
    }

    // 通知到达，只有个推通道下发的通知会回调此方法
    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) {
        Log.i(TAG, "onNotificationMessageArrived: msg:" + getGTNotificationMessageString(msg));
    }

    // 通知点击，只有个推通道下发的通知会回调此方法
    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage msg) {
        Log.i(TAG, "onNotificationMessageClicked: msg" + getGTNotificationMessageString(msg));
    }

    @Override
    public void onReceiveDeviceToken(Context context, String s) {
        Log.i(TAG, "onReceiveDeviceToken: " + s);
    }

    private String getGTNotificationMessageString(GTNotificationMessage msg) {
        return String.format("{taskId:%s, messageId:%s, title:%s, content:%s, payload:%s, url:%s, intentUri:%s}",
                msg.getTaskId(),
                msg.getMessageId(),
                msg.getTitle(),
                msg.getContent(),
                msg.getPayload(),
                msg.getUrl(),
                msg.getIntentUri()
        );
    }
}