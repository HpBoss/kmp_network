package com.jd.library.network

/**
 * @Author: 何飘
 * @Date: 2024/12/14 18:27
 * @Description:
 */
interface UserInfo {
    val teamId: String
    val uerName: String
    val appId: String
    val encryptedUseName: String
    val tenantCode: String
    val encryptedTenantCode: String
    val randomKey: String
    val lat: String
    val lng: String
    val channel: String
}

expect fun getUserInfo(): UserInfo