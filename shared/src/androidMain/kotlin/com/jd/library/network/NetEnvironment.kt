package com.jd.library.network

/**
 * @Author: 何飘
 * @Date: 2024/12/14 17:32
 * @Description:
 */
data class NetEnvironment(val baseUrl: String, val intranetBaseUrl: String) {
    companion object {
        private lateinit var sCurrent: NetEnvironment

        fun setCurrentEnv(newEnv: NetEnvironment) {
            if (newEnv.baseUrl.isEmpty() ||newEnv.intranetBaseUrl.isEmpty()) return
            sCurrent = newEnv
        }

        fun getCurrentEnv(): NetEnvironment {
            return sCurrent
        }
    }
}