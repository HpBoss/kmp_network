package com.jd.library.network

/**
 * @Author: 何飘
 * @Date: 2024/12/14 19:39
 * @Description:
 */
abstract class Encrypt {
    abstract val encryptString: String
    abstract val decryptString: String
    abstract val getRandomKey: String
    abstract val getEncryptArray: Array<String>
    abstract fun rsaEncrypt(publicKey: String, data: String, segmentSize: Int): String
    abstract fun rsaDecrypt(publicKey: String, data: String, segmentSize: Int): String
    abstract fun rsaSign(privateKey: String, content: String): String
    abstract fun esEncrypt(key: String, data: String): String
    abstract fun getMd5(content: String): String
}

expect fun getEncrypt(): Encrypt