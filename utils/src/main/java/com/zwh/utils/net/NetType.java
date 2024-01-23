package com.zwh.utils.net;

/**
 * 网络类型的监听
 * 这里使用枚举进行网络类型的确认
 */
public enum NetType {
    // 有网络,包括wifi和gprs
    AUTO,

    // WIFI
    WIFI,

    // 4G
    NET_4G,

    // 3G
    NET_3G,

    // 2G
    NET_2G,

    // 没有网络
    NONE,
}
