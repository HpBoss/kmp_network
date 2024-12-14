package com.jd.library.network

class AndroidEncrypt(
    override val encryptString: String = "",
    override val decryptString: String = "",
    override val getRandomKey: String = "",
    override val getEncryptArray: Array<String> = arrayOf()
) : Encrypt() {
    override fun rsaEncrypt(publicKey: String, data: String, segmentSize: Int): String {
        TODO("Not yet implemented")
    }

    override fun rsaDecrypt(publicKey: String, data: String, segmentSize: Int): String {
        TODO("Not yet implemented")
    }

    override fun rsaSign(privateKey: String, content: String): String {
        TODO("Not yet implemented")
    }

    override fun esEncrypt(key: String, data: String): String {
        TODO("Not yet implemented")
    }

    override fun getMd5(content: String): String {
        TODO("Not yet implemented")
    }

}

actual fun getEncrypt(): Encrypt = AndroidEncrypt()