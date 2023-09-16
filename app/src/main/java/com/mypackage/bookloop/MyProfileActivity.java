package com.mypackage.bookloop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MyProfileActivity extends AppCompatActivity {

    private LocalSession session;
    private DatabaseReference reference;
    private Button btnUpdate, resetPass;
    private String uid,name,email,phone;
    private TextInputLayout layName, layPhone;
    private TextInputEditText edit_name, edit_email,edit_phone;

    private FirebaseAuth fAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#45adda\">" + "Book" + "</font>"+"<font color=\"#ffffff\">" + "Loop" + "</font>"));
        session=new LocalSession(MyProfileActivity.this);
        layName = findViewById(R.id.lay_name);
        layPhone = findViewById(R.id.lay_phone);


        edit_email=findViewById(R.id.inp_email);
        edit_name=findViewById(R.id.inp_name);
        edit_phone=findViewById(R.id.inp_phone);
        btnUpdate=findViewById(R.id.btn_update);
        resetPass = findViewById(R.id.resetPassword);

        uid=session.getInfo(ConstantKeys.KEY_UID);

        reference = FirebaseDatabase.getInstance().getReference("BLUserAccount");
        fAuth=FirebaseAuth.getInstance();
        user=fAuth.getCurrentUser();

        getAllInfo();

        btnUpdate.setOnClickListener(v -> {
            updateProfile();
        });

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.sendPasswordResetEmail(edit_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MyProfileActivity.this,"Reset password link sent to your email",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(MyProfileActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    private void updateProfile() {

        Map<String , Object> userJson=new HashMap<>();
        name = edit_name.getText().toString().trim();
        phone=edit_phone.getText().toString().trim();
        userJson.put(ConstantKeys.KEY_NAME, name);
        userJson.put(ConstantKeys.KEY_PHONE, phone);

        layName.setError(null);
        layPhone.setError(null);

        if(name.isEmpty()){
            layName.setError("Can't be empty");
        }
        else if(phone.isEmpty()){
            layPhone.setError("Can't be empty");
        }
        else if(phone.length()!=10){
            layPhone.setError("Enter 10 digits valid phone number");
        }
        else {
            reference.child(uid).updateChildren(userJson, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error == null) {
                        Toast.makeText(MyProfileActivity.this, "Updated", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MyProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void getAllInfo() {
        Intent data = getIntent();
        reference.getRef().child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, String> user=(Map<String, String>)snapshot.getValue();

                BLUserAccount account = new BLUserAccount(user.get(ConstantKeys.KEY_NAME).toString(),
                        user.get(ConstantKeys.KEY_EMAIL).toString(), user.get(ConstantKeys.KEY_PHONE).toString());

                name=account.getUserName();
                email=account.getUserEmail();
                phone=account.getuserPhone();
                edit_name.setText(name);
                edit_phone.setText(phone);
                edit_email.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //define the menu file
        getMenuInflater().inflate(R.menu.without_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /**
     * method to generate event against the menu
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) //allow us to use the id against the menu itself
        {
            case R.id.Logout_without_search:
                logout();
                break;

            case R.id.home_menu_without_search:
                Intent hm = new Intent(MyProfileActivity.this, MainActivity.class);
                startActivity(hm);
                break;

            case R.id.MyProfile_without_search:
                Intent mp = new Intent(MyProfileActivity.this, MyProfileActivity.class);
                startActivity(mp);
                break;

            case R.id.Upload_new_book_without_search:
                Intent upl = new Intent(MyProfileActivity.this, Upload_NewBook.class);
                startActivity(upl);//message passing object
                break;

            case R.id.MyUploads_without_search:
                Intent myp = new Intent(MyProfileActivity.this, MyUploads.class);
                startActivity(myp);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MyProfileActivity.this);
        alertBuilder.setTitle("Exit message");
        alertBuilder.setMessage("Confirm to exit");

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //cancel operation
                Toast.makeText(MyProfileActivity.this,"Operation suspended", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        alertBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LocalSession localSession = new LocalSession(MyProfileActivity.this);
                localSession.clearAll();
                Intent r = new Intent(MyProfileActivity.this, Login_Activity.class);
                startActivity(r);//message passing object
                Toast.makeText(MyProfileActivity.this,"Logout successful", Toast.LENGTH_SHORT).show();
                MyProfileActivity.this.finish();
            }
        });
        alertBuilder.setCancelable(false); //auto cancel suspended
        alertBuilder.show();
    }


}


