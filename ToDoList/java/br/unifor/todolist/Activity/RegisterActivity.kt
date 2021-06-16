package br.unifor.todolist.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import at.favre.lib.crypto.bcrypt.BCrypt
import br.unifor.todolist.Database.UserDAO
import br.unifor.todolist.Model.User
import br.unifor.todolist.R
import br.unifor.todolist.util.DatabaseUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var userDAO:UserDAO

    private lateinit var mFirstName:EditText
    private lateinit var mLastName:EditText
    private lateinit var mEmail:EditText
    private lateinit var mPassword:EditText
    private lateinit var mConfirmationPassword:EditText
    private lateinit var mSingUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_acitvity)

        userDAO = DatabaseUtil.getInstance(applicationContext).getUserDao()

        mFirstName = findViewById(R.id.Register_edittext_firstname)
        mLastName = findViewById(R.id.Register_edittext_lastname)
        mEmail = findViewById(R.id.Register_edittext_email)
        mPassword = findViewById(R.id.Register_edittext_password)
        mConfirmationPassword = findViewById(R.id.Register_edittext_confirmationpassword)

        mSingUp = findViewById(R.id.Register_button_registrar)
        mSingUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when(v?.id){

            //Registro de um novo usuário
            R.id.Register_button_registrar ->{
                
                val firstName = mFirstName.text.toString()
                val lastName = mLastName.text.toString()
                val email = mEmail.text.toString()
                val password = mPassword.text.toString()
                val confirmationPassword = mConfirmationPassword.text.toString()

                var isFormFilled = true

                if(firstName.isEmpty()){
                    mFirstName.error = "Este campo não pode ser vazio"
                    isFormFilled = false
                }

                if(lastName.isEmpty()){
                    mLastName.error = "Este campo não pode ser vazio"
                    isFormFilled = false
                }

                if(email.isEmpty()){
                    mEmail.error = "Este campo não pode ser vazio"
                    isFormFilled = false
                }

                if(password.isEmpty()){
                    mFirstName.error = "Este campo não pode ser vazio"
                    isFormFilled = false
                }

                if(confirmationPassword.isEmpty()){
                    mConfirmationPassword.error = "Este campo não pode ser vazio"
                    isFormFilled = false
                }


                if(isFormFilled){

                    if(password != confirmationPassword){
                        mConfirmationPassword.error = "As senhas não coincidem"
                        return
                    }

                    //Inserção de um novo usuário caso não existe, caso existe, exibe uma mensagem de erro
                    GlobalScope.launch {
                        
                        val handler = Handler(Looper.getMainLooper())

                        val user = userDAO.findByEmail(email)

                        if(user == null){

                            val newUser = User(
                                    firstName = firstName,
                                    lastName = lastName,
                                    email = email,
                                    password = BCrypt.withDefaults().hashToString(12, password.toCharArray())
                            )

                            userDAO.insert(newUser)

                            handler.post {
                                Toast.makeText(applicationContext, "Usuário adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                                finish()
                            }

                        } else {

                            handler.post {
                               mEmail.error = "Já existe um usuário cadastrado com esse endereço de email"
                            }
                        }
                    }
                }

            }
        }

    }
}

