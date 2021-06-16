package br.unifor.todolist.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import at.favre.lib.crypto.bcrypt.BCrypt
import br.unifor.todolist.Database.UserDAO
import br.unifor.todolist.R
import br.unifor.todolist.util.DatabaseUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mLoginEmail:EditText
    private lateinit var mLoginPassword:EditText
    private lateinit var mLoginSingIn: Button
    private lateinit var mRegister:TextView

    private lateinit var  userDao: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userDao = DatabaseUtil.getInstance(applicationContext).getUserDao()

        mLoginEmail = findViewById(R.id.Login_edittext_email)
        mLoginPassword = findViewById(R.id.Login_edittext_password)

        mLoginSingIn = findViewById(R.id.Login_button_signin)
        mLoginSingIn.setOnClickListener(this)

        mRegister = findViewById(R.id.Login_textview_register)
        mRegister.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.Login_textview_register ->{
                val it = Intent(applicationContext, RegisterActivity::class.java)
                startActivity(it)
            }

            R.id.Login_button_signin ->{

                //Verificando se os campos estão todos preenchidos
                
                val email = mLoginEmail.text.toString()
                val password = mLoginPassword.text.toString()

                var isLoginFormFilled = true

                if(email.isEmpty()){
                    mLoginEmail.error = "Este campo não pode ser nulo"
                    isLoginFormFilled = false
                }

                if(password.isEmpty()){
                    mLoginPassword.error = "Este campo não pode ser nulo"
                    isLoginFormFilled = false
                }

                if(isLoginFormFilled){

                    GlobalScope.launch {

                        //Buscando usuário por email no banco de dados 
                        
                        val user = userDao.findByEmail(email)

                        if(user != null) {

                            if (BCrypt.verifyer().verify(password.toCharArray(), user.password).verified) {
                                val it = Intent(applicationContext, MainActivity::class.java)
                                it.putExtra("userId", user.id)
                                startActivity(it)
                                finish()
                            } else {
                                showToastMessenger()
                            }

                        } else {
                            showToastMessenger()
                        }

                    }
                }
            }

        }
    }

    private fun showToastMessenger(){
        val handler = Handler(Looper.getMainLooper())
        handler.post{
            Toast.makeText(applicationContext, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show()
        }
    }

}
