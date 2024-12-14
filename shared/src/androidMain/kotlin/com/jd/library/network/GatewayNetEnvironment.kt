package com.jd.library.network

/**
 * @Author: 何飘
 * @Date: 2024/12/14 16:34
 * @Description:
 */
data class GatewayNetEnvironment(
    val baseUrl: String,
    val appId: String,
    val appKey: String,
    val isPre: Boolean
) {
    companion object {
        private lateinit var sCurrent: GatewayNetEnvironment

        fun getCurrentEnv(): GatewayNetEnvironment {
            return sCurrent
        }

        fun setCurrentEnv(newEnv: GatewayNetEnvironment) {
            if (newEnv.baseUrl.isEmpty() || newEnv.appKey.isEmpty() || newEnv.appId.isEmpty()) return
            sCurrent = newEnv
        }
    }
}