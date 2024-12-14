package com.jd.library.network

class IOSUserInfo(
    override val teamId: String = "",
    override val uerName: String = "",
    override val appId: String = "",
    override val encryptedUseName: String = "",
    override val tenantCode: String = "",
    override val encryptedTenantCode: String = "",
    override val randomKey: String = "",
    override val lat: String = "",
    override val lng: String = "",
    override val channel: String = ""
) : UserInfo

actual fun getUserInfo(): UserInfo = IOSUserInfo()