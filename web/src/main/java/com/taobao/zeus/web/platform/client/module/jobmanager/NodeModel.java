package com.taobao.zeus.web.platform.client.module.jobmanager;

/**
 * Created by tengdj on 2017/3/24.
 */
public enum NodeModel {
    NONE_NODE("请选择", null),
    MASTER("master-01",  "10.105.216.57"),   // s3-hadoop-rts-gw-prod-02  主
    SLAVE_01("slave-01", "10.105.84.181");   // s3-hadoop-rts-gw-prod-01
    // SLAVE_03("slave-03", "hadoop-bd4"),
    // SLAVE_04("slave-04", "hadoop-bd5");
    // SLAVE_05("slave-05", "10.31.52.74"),
    // SLAVE_06("slave-06", "10.31.52.75"),
    // SLAVE_07("slave-07", "10.31.52.102"),
    // SLAVE_08("slave-08", "10.31.52.44"),
    // SLAVE_09("slave-09", "10.31.52.42");

    private String name;
    private String ip;

    private NodeModel(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public static String getIp(String name) {
        for (NodeModel n : NodeModel.values()) {
            if (n.getName().equals(name)) {
                return n.getIp();
            }
        }
        return null;
    }

    public static String getName(String ip) {
        for (NodeModel n : NodeModel.values()) {
            if (n.getIp().equals(ip)) {
                return n.getName();
            }
        }
        return null;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }
}
