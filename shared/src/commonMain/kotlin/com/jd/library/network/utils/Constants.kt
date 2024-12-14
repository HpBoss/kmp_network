package com.jd.library.network.utils

object Constants {
    const val TAG = "GatewayHttpManager"
    const val JAPI_MOCK_URL_PREFIX = "http://mocker.jd.com/mocker/zh/"
    const val HEADER_KEY_GATEWAY_VERSION: String = "x-client-gateway-version"
    const val HEADER_GATEWAY_NONE = "none"
    const val HEADER_GATEWAY_V1 = "v1"
    const val HEADER_GATEWAY_V2 = "v2"
    const val HEADER_GATEWAY_COLOR = "color"

    const val HEADER_KEY_ACTION = "x-client-action"
    //内网访问接口标示
    const val HEADER_KEY_INTRANET = "x-client-intranet"
    const val HEADER_KEY_CONNECT_TIMEOUT = "x-client-connect-timeout"
    const val HEADER_KEY_WRITE_TIMEOUT = "x-client-write-timeout"
    const val HEADER_KEY_READ_TIMEOUT = "x-client-read-timeout"

    //跳过业务拦截器
    const val HEADER_KEY_SKIP_INTERCEPTORS = "x-client-skip-interceptors"

    //J-API MOCK
    const val HEADER_KEY_JAPI_MOCK_ID = "x-client-japi-mock-id"
    const val HEADER_KEY_JAPI_MOCK_URL = "x-client-japi-mock-url"

    //Http get 参数放在url query中
    const val HEADER_KEY_METHOD_GET = "true"
    const val HEADER_KEY_DO_NOT_REPORT_ERROR = "x-client-do-not-report-error"

    //并发请求数
    const val MAX_REQUESTS = 128
    const val MAX_REQUESTS_PER_HOST = 24

    const val REQUEST_ENCRYPT_NONE: Int = 1 shl 1
    const val REQUEST_ENCRYPT_DES: Int = 1 shl 2
    const val REQUEST_ENCRYPT_RSA: Int = 1 shl 3

    //响应解密
    const val RESPONSE_DECRYPT_RSA: Int = 1 shl 4
    // 请求超时时间（毫秒）
    const val TIMEOUT: Long = 20 * 1000
}