package com.example.aesproj
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


class MainActivity : AppCompatActivity() {

    var inputText: EditText? = null
    var inputPassword: EditText? = null
    var outputText: TextView? = null
    var encBtn:Button? = null
    var decBtn: Button? = null
    var outputString: String? = null
    var AES = "AES"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputText = findViewById<View>(R.id.inputText) as EditText
        inputPassword = findViewById<View>(R.id.password) as EditText
        outputText = findViewById<View>(R.id.outputText) as TextView
        encBtn = findViewById<View>(R.id.encBtn) as Button
        decBtn = findViewById<View>(R.id.decBtn) as Button
        encBtn!!.setOnClickListener {
            try {
                outputString = encrypt(inputText!!.text.toString(), inputPassword!!.text.toString())
                outputText!!.text = outputString
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        decBtn!!.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("user_message", outputString)
            startActivity(intent)
        }
        //decBtn!!.setOnClickListener {
          //  try {
            // outputString = decrypt(outputString, inputPassword!!.text.toString())
           // } catch (e: Exception) {
            //    e.printStackTrace()
            //}
            //outputText!!.text = outputString
        //}
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
    private fun encrypt(Data: String, password: String): String {
        val key = generateKey(password)
        val c = Cipher.getInstance(AES)
        c.init(Cipher.ENCRYPT_MODE, key)
        val encVal = c.doFinal(Data.toByteArray())
        return Base64.encodeToString(encVal, Base64.DEFAULT)
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