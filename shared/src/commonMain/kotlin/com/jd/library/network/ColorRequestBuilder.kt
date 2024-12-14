package com.jd.library.network

import com.jd.library.network.utils.Constants

class ColorRequestBuilder {

    private var headers: MutableMap<String, String> = mutableMapOf()
    private var params: MutableMap<String, Any> = mutableMapOf()

    fun headers(headers: MutableMap<String, String>): ColorRequestBuilder {
        this.headers += headers
        return this
    }

    fun params(params: MutableMap<String, Any>): ColorRequestBuilder {
        this.params += params
        return this
    }

    fun build() {
        headers.apply {
            set(Constants.HEADER_KEY_GATEWAY_VERSION, Constants.HEADER_GATEWAY_COLOR)
            set(Constants.HEADER_KEY_METHOD_GET, "false")
        }
    }
}