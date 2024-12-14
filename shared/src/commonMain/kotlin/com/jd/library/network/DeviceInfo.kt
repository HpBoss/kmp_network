package com.jd.library.network

/**
 * @Author: 何飘
 * @Date: 2024/12/14 18:49
 * @Description:
 */
interface DeviceInfo {
    val deviceWidth: Int // 设备屏幕宽度
    val deviceHeight: Int // 设备屏幕高度
    val deviceUniqueId: String // uniqueId
    val pushDeviceToken: String // token
    val appVersionName: String // 应用版本
    val locale: String // 语言
    val timeZone: String // 时区
    val deviceType: String // 设备类型
    val osVersion: String // 系统版本
    val internetType: String // 网络类型
    val brand: String // 品牌
    val model: String // 型号
}

expect fun getDeviceInfo(): DeviceInfo