package com.luther.github.util

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * This class offer a simple SHA-1 hash algorithm use to showcase hashing sensitive data.
 * Use this if the data is not required to be reverse engineered.
 * For example, user password.
 */
object SHA1Hashing {
    private fun convertToHex(data: ByteArray): String {
        val buf = StringBuilder()
        for (b in data) {
            var halfByte = b.toInt() ushr 4 and 0x0F
            var twoHalfs = 0
            do {
                buf.append(if (halfByte in 0..9) ('0'.code + halfByte).toChar() else ('a'.code + (halfByte - 10)).toChar())
                halfByte = b.toInt() and 0x0F
            } while (twoHalfs++ < 1)
        }
        return buf.toString()
    }

    @Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
    fun hashString(text: String): String {
        val md = MessageDigest.getInstance("SHA-1")
        val textBytes = text.toByteArray(charset("iso-8859-1"))
        md.update(textBytes, 0, textBytes.size)
        val sha1hash = md.digest()
        return convertToHex(sha1hash)
    }
}