package instaU.ayush.com.security

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private val ALGORITHM = System.getenv("hashAlgorithm")
private val HASH_KEY = System.getenv("hashSecret").toByteArray()

private val hMacKey = SecretKeySpec(HASH_KEY, ALGORITHM)

fun hashPassword(password: String): String{
    val hMac = Mac.getInstance(ALGORITHM)
    hMac.init(hMacKey)

    return hex(hMac.doFinal(password.toByteArray()))
}