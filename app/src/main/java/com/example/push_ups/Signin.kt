package com.example.push_ups

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signin.*
import org.jetbrains.anko.startActivity

class Signin : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var authListener: FirebaseAuth.AuthStateListener //리스너 선언
    lateinit var googleSigneInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)


        SignUp_Btn.setOnClickListener {
            startActivity<Signup>()
        }

        SignIn_Btn.setOnClickListener {
            loginUserId(Id_EditText.text.toString(), Pswd_EditText.text.toString())
        }

        // Configure Google Sign In
        //GoogleSignInOptions 옵션을 관리해주는 클래스로 API 키값과 요청할 값이 저장되어 있다.
        var gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

        googleSigneInClient= GoogleSignIn.getClient(this,gso)
        auth = FirebaseAuth.getInstance()

        //리스너 초기화
        authListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user != null) {
                //로그인 되었을때 발생하는 이벤트
            } else {
                //로그아웃 또는 로그인이 안 되었을 때 발생하는 이벤트
            }
        }

    }

    private fun signIn(){
        val signInIntent = googleSigneInClient.signInIntent
        startActivityForResult(signInIntent,100)
    }

    override fun onStart() {
        super.onStart()
        auth?.addAuthStateListener { authListener!! }
    }

    override fun onPause() {
        super.onPause()
        auth?.removeAuthStateListener { authListener!! }
    }



    private fun createUserId(email:String, password:String){
        auth?.createUserWithEmailAndPassword(email,password)
            ?.addOnCompleteListener(this) { task->
                if(task.isSuccessful){
                    Toast.makeText(this, "회원가입 성공",Toast.LENGTH_SHORT).show()
                    //아이디 생성이 완료 되었을 때
                    val user=auth?.currentUser
                }else{
                    Toast.makeText(this, "회원가입 실패",Toast.LENGTH_SHORT).show()
                    //아이디 생성이 실패했을 경우
                }
            }
    }

    private fun loginUserId(email:String, password: String){
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this){
                if(it.isSuccessful){
                    Toast.makeText(this, "로그인 성공",Toast.LENGTH_SHORT).show()
                    //로그인 성공시 이벤트발생
                    startActivity<MainActivity>()
                }else{
                    Toast.makeText(this, "로그인 실패",Toast.LENGTH_SHORT).show()
                    //로그인 실패시 이벤트 발생
                    Pswd_EditText.setText("")
                }
            }
    }


    private fun updatePassword(newPassword: String) {
        auth?.currentUser?.updatePassword(newPassword)
            ?.addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "패스워드 변경 성공",Toast.LENGTH_SHORT).show()
                    //패스워드 변경이 성공했을 때 발생하는 이벤트}
                }
            }
    }
    private fun updateEmail(newEmail:String){
        auth?.currentUser?.updateEmail(newEmail)
            ?.addOnCompleteListener(this){
                if(it.isSuccessful){
                    Toast.makeText(this, "ID 변경 성공",Toast.LENGTH_SHORT).show()
                    //이메일 변경이 성공햇을때 발생하는 이벤트
                }
            }
    }

    private fun resetPassword(email:String){
        auth?.sendPasswordResetEmail(email)
            ?.addOnCompleteListener(this){
                if(it.isSuccessful){
                    //비밀번호 재설정 메일을 보내기가 성공했을때 이벤트
                }
            }
    }


    private fun deleteId(){
        auth?.currentUser?.delete()
            ?.addOnCompleteListener(this){
                if(it.isSuccessful){
                    //회원 아이디 삭제에 성공했을 때
                }
            }
    }

    private fun reauthenticate(email: String,password: String){
        val credential = EmailAuthProvider
            .getCredential(email,password)

        auth?.currentUser?.reauthenticate(credential)
            ?.addOnCompleteListener(this){
                if(it.isSuccessful){
                    //재인증에 성공했을 때 발생하는 이벤트
                }
            }
    }



}