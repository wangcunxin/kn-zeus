package com.taobao.zeus.util;

/**
 * Created by tengdj on 2017/5/11.
 */
public enum  DingDingGroup {
    DEFAULT("default", "https://oapi.dingtalk.com/robot/send?access_token=b4522ff6f45501837b60f07301bb31af7bd5de633172fc5a6350fb9b45554650"),
    SSIS("ssis", "https://oapi.dingtalk.com/robot/send?access_token=d06ac0028e2f0c412c8415808668e6b5a7d9c135802c31a371e10e4f3a145b97"),
    CUISHOU("cuishou", "https://oapi.dingtalk.com/robot/send?access_token=dad492b8610e013f2b5f9fe2197e97449cec82dad4b7d76e26dabff4c993d073");

    private String groupName;
    private String webhookToken;

    private DingDingGroup(String groupName, String webhookToken) {
        this.groupName = groupName;
        this.webhookToken = webhookToken;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getWebhookToken() {
        return webhookToken;
    }

    public static String getWebhookToken(String groupName) {
        for (DingDingGroup group : DingDingGroup.values()) {
            if (group.getGroupName().equals(groupName)) {
                return group.getWebhookToken();
            }
        }
        return null;
    }
}
