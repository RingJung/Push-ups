package com.example.push_ups

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class Signup : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var authListener: FirebaseAuth.AuthStateListener //리스너 선언


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


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
                }else{
                    Toast.makeText(this, "로그인 실패",Toast.LENGTH_SHORT).show()
                    //로그인 실패시 이벤트 발생
                }
            }
    }

    private fun verifyEmail(){
        auth?.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                if(it.isSuccessful){

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
