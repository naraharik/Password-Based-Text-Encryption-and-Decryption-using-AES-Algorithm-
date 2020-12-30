package com.example.aesproj
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_second.*
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


class SecondActivity : AppCompatActivity() {

    var inputPassword: EditText? = null
    var outputtText: TextView? = null
    var AES = "AES"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        inputPassword = findViewById<View>(R.id.inputPasswordd) as EditText
        outputtText = findViewById<View>(R.id.outputtText) as TextView


        val bundle: Bundle? = intent.extras
        var outputString = bundle!!.getString("user_message")
        outputText.text = "Encrypted Msg :    $outputString"

        deccBtn!!.setOnClickListener {
            try {
                outputString = decrypt(outputString, inputPassword!!.text.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            outputtText!!.text = "Decrypted Msg:    $outputString"
        }
    }
    @Throws(Exception::class)
    private fun decrypt(outputString: String?, password: String): String {
        val key = generateKey(password)
        val c = Cipher.getInstance(AES)
        c.init(Cipher.DECRYPT_MODE, key)
        val decodeValue = Base64.decode(outputString, Base64.DEFAULT)
        val decValue = c.doFinal(decodeValue)
        return String(decValue)
    }

    @Throws(Exception::class)
    private fun generateKey(password: String): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = password.toByteArray(charset("UTF-8"))
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        return SecretKeySpec(key, "AES")
    }
}