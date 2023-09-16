package com.mypackage.bookloop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends AppCompatActivity {

    LocalSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        session=new LocalSession(this);

        //WRITE THE CODE TO AUTOMATICALLY ROUTE TO DESIRED SCREEN
        /**
         * Post delayed requires 2 parameters
         *  1. Thread
         *  2. Delay Duration
         */
        new Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    @Override
                    public void run() {

                        //CHECKING USER CURRENT STATUS
                        if(session.checkLog())
                        {
                            //USER ALREADY LOGGED IN
                            //String currentUserEmail=session.getInfo(ConstantKeys.KEY_UID); //retrieving the data
                            Intent intent=new Intent(Splash.this,MainActivity.class);//defining the intent
                            //intent.putExtra(ConstantKeys.KEY_EMAIL,currentUserEmail); //setting the data for intent
                            startActivity(intent);//routing to a new activity
                            Splash.this.finish();
                        }

                        else
                        {
                            //NO CURRENT USER FOUND
                            Intent intent=new Intent(Splash.this,Login_Activity.class);//defining the intent
                            startActivity(intent);//routing to a new activity
                        }
                        Splash.this.finish();
                    }

                },2000
        );
    }
}