package com.jd.library.network

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform