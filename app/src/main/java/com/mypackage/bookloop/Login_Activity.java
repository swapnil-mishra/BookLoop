package com.mypackage.bookloop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {

    TextInputLayout layoutUser, layoutPass;
    public TextInputEditText editUser, editPass;
    Button btnLogin;
    TextView navTextSignUp, forgotPass;
    LocalSession session;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();

        layoutUser=findViewById(R.id.username_ip_layout);
        layoutPass=findViewById(R.id.inputlayout2);
        editUser=findViewById(R.id.edit_user);
        editPass=findViewById(R.id.edit_password);
        btnLogin=findViewById(R.id.login_btn);
        navTextSignUp=findViewById(R.id.Signup);
        forgotPass=findViewById(R.id.forgotPassword);

        //INITIALIZING THE OBJECT FOR SESSION HANDLING
        session=new LocalSession(Login_Activity.this);

        navTextSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LOGIC TO NAVIGATE TO SIGN UP SCREEN
                Intent signUp= new Intent(Login_Activity.this,SignUpActivity.class); //EXPLICIT INTENT
                startActivity(signUp);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,pwd;

                email=editUser.getText().toString().trim();
                pwd=editPass.getText().toString().trim();

                layoutUser.setError(null);
                layoutPass.setError(null);

                //ADD SOME VALIDATIONS
                if(email.isEmpty())
                {
                    layoutUser.setError("Can't be empty");
                }
                else if(pwd.isEmpty())
                {
                    layoutPass.setError("Can't be empty");
                }
                else if(pwd.length()<5)
                {
                    layoutPass.setError("Password Should Be Minimum Of 6 Characters");
                }
                else
                {
                    login(email,pwd);

                }

            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fp = new Intent(Login_Activity.this,ResetPassword.class);
                startActivity(fp);
            }
        });

    }

    private void login(String email, String password){
        auth=FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        //USER IS FOUND
                        if( auth.getCurrentUser().isEmailVerified())
                        {
                            //EMAIL IS VERIFIED

                            String uid=auth.getCurrentUser().getUid();  //add it to shared preference
                            session.saveInfo(uid);
                            Intent r=new Intent(Login_Activity.this,MainActivity.class);
                            startActivity(r);
                            Login_Activity.this.finish();
                        }
                        else
                        {
                            //EMAIL IS NOT VERIFIED
                            Toast.makeText(this,"EMAIL IS NOT VERIFIED, SIGNUP AGAIN.",Toast.LENGTH_SHORT).show();
                            auth.getCurrentUser().delete();
                        }

                    }
                    else
                    {
                        //USER NOT FOUND
                        Toast.makeText(this,"USER NOT FOUND",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,"Failed Due To "+e.getMessage(),Toast.LENGTH_SHORT).show()
                );

    }
}