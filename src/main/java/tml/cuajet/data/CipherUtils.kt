@file:Suppress("unused")

package tml.cuajet.data

import android.annotation.SuppressLint
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

class CipherUtils {
    companion object {
        @SuppressLint("GetInstance")
        fun encryptDESBase64(password: String, text: String): String? {
            try {
                val keySpec = DESKeySpec(password.toByteArray(charset("UTF8")))
                val keyFactory: SecretKeyFactory = SecretKeyFactory.getInstance("DES")
                val key: SecretKey = keyFactory.generateSecret(keySpec)
                val cleartext = text.toByteArray(charset("UTF8"))
                val cipher: Cipher = Cipher.getInstance("DES") // cipher is not thread safe
                cipher.init(Cipher.ENCRYPT_MODE, key)
                return Base64.encodeToString(cipher.doFinal(cleartext), Base64.DEFAULT)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return text
        }

        @SuppressLint("GetInstance")
        fun decryptDESBase64(password: String, text: String?): String? {
            try {
                val keySpec = DESKeySpec(password.toByteArray(charset("UTF8")))
                val keyFactory: SecretKeyFactory = SecretKeyFactory.getInstance("DES")
                val key: SecretKey = keyFactory.generateSecret(keySpec)
                val encryptedBytes: ByteArray = Base64.decode(text, Base64.DEFAULT)
                val cipher: Cipher = Cipher.getInstance("DES")
                cipher.init(Cipher.DECRYPT_MODE, key)
                val decryptedData: ByteArray = cipher.doFinal(encryptedBytes)
                return String(decryptedData)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return text
        }
    }
}