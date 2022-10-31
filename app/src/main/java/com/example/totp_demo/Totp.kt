package com.example.totp_demo

import androidx.compose.ui.text.toUpperCase
import org.apache.commons.codec.binary.Base32
import java.math.BigInteger
import java.nio.ByteBuffer
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and
import kotlin.experimental.or

object Totp {
    private fun prepareKey(key: String): ByteArray {
        val tmp = key.replace(" ", "").uppercase()
        println("preparedKey str: " + tmp)
        return Base32().decode(tmp)
    }

    private fun hmacSha1(key: ByteArray, value: ByteArray): ByteArray =
        with(Mac.getInstance("HmacSHA1")) {
            val macKey = SecretKeySpec(key, algorithm)
            init(macKey)
            doFinal(value)
        }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun oneTimePassword(rawKey: String, rawValue: Long): String {
        val preparedKey = prepareKey(rawKey)
        val preparedValue = rawValue.toByteArray()
        println("key: " + rawKey)
        println("value: " + rawValue)
        println("preparedKey: " + preparedKey.asUByteArray().toList())
        println("preparedValue: " + preparedValue.asUByteArray().toList())
        val hash = hmacSha1(preparedKey, preparedValue)
        val number= with(hash) {
            println(this.toUByteArray().toList())
            println("this[lastIndex -1]: ${this[lastIndex].toUInt()}")
            println("this[lastIndex -1] and 0x0F: ${this[lastIndex] and 0x0F}")
            val offset = last() and 0x0F
            println("offset: $offset")
            val hashParts: ByteArray = hash.slice(offset until offset + 4).toByteArray()
            println("hashParts before: ${hashParts.asUByteArray().toList()}")
            hashParts[0] = hashParts[0] and 0x7F
            println("hashParts: ${hashParts.asUByteArray().toList()}")
            hashParts.asUByteArray().toUInt()
        }

        return number.mod(1000000u).toString()
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
private fun UByteArray.toUInt(): UInt = run {
    val a = this[0].toUInt() shl 24
    val b = this[1].toUInt() shl 16
    val c = this[2].toUInt() shl 8
    val d = this[3].toUInt()
    a + b + c + d
}

private fun Long.toByteArray(): ByteArray = ByteBuffer.allocate(8).putLong(this).array()
