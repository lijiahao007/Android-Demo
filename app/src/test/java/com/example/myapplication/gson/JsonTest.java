package com.example.myapplication.gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class JsonTest {
    @Test
    public void test() throws JSONException {
        String json = "{\"ad_t\":0,\"n_alert_type\":7,\"n_alternate_content\":\"\",\"n_alternate_title\":\"\",\"n_badge_add_num\":1,\"n_big_pic_path\":\"https:\\/\\/imgcps.jd.com\\/ling4\\/10058126049559\\/5Lqs6YCJ5aW96LSn\\/5L2g5YC85b6X5oul5pyJ\\/p-5f3a47329785549f6bc7a6e8\\/843db61f\\/cr\\/s\\/q.jpg\",\"n_category\":\"\",\"n_content\":\"报警消息【69000027】\",\"n_extras\":{\"isOnline\":true,\"password\":\"asdHAO0624770\"},\"n_flag\":1,\"n_large_icon\":\"https:\\/\\/dn-richpush.qbox.me\\/20230203154646\\/ic_logo_round.png\",\"n_priority\":0,\"n_style\":3,\"n_title\":\"报警消息\",\"msg_id\":\"18100584523005141\",\"rom_type\":\"0\",\"notification_id\":512688993,\"_j_data_\":\"{\\\"data_msgtype\\\":1,\\\"push_type\\\":1, \\\"is_vip\\\":0}\"}";

        JSONObject jsonObject = new JSONObject(json);
        JSONObject n_extras = jsonObject.optJSONObject("n_extras");
        System.out.println(n_extras);

        String password = n_extras.optString("password");
        boolean isOnline = n_extras.optBoolean("isOnline");
        System.out.println("password = 【" + password + "】  isOnline=【" + isOnline + "】");
    }
}
