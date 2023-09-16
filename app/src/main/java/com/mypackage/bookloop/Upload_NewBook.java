package com.mypackage.bookloop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Upload_NewBook extends AppCompatActivity {
    TextView uploadbook;
    Button btnUpload;
    TextInputLayout bknameInpt, authorInpt, publisherInpt, semInpt, descripInpt, priceInpt;
    TextInputEditText bknameEdit, authorEdit, publisherEdit, semEdit, descripEdit, priceEdit;

    LocalSession session;

    DatabaseReference node, ref;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__new_book);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#45adda\">" + "Book" + "</font>"+"<font color=\"#ffffff\">" + "Loop" + "</font>"));

        db=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        String uid=user.getUid();

        uploadbook=findViewById(R.id.txt_user);
        btnUpload=findViewById(R.id.upload_button);
        bknameInpt=findViewById(R.id.bookname);
        bknameEdit=findViewById(R.id.bookname_edit);
        authorInpt=findViewById(R.id.Author);
        authorEdit=findViewById(R.id.author_edit);
        publisherInpt=findViewById(R.id.publisher);
        publisherEdit=findViewById(R.id.publisher_edit);
        semInpt=findViewById(R.id.classorsem);
        semEdit=findViewById(R.id.classorsem_edit);
        descripInpt=findViewById(R.id.description);
        descripEdit=findViewById(R.id.description_edit);
        priceInpt=findViewById(R.id.Price);
        priceEdit=findViewById(R.id.price_edit);

        session=new LocalSession(Upload_NewBook.this);
        //String sellerName=session.getInfo(ConstantKeys.KEY_NAME);

        //ON CLICKING UPLOAD BUTTON
        btnUpload.setOnClickListener(v -> {
            String bookName=bknameEdit.getText().toString();
            String authorName=authorEdit.getText().toString().trim();
            String publisherName=publisherEdit.getText().toString();
            String sem=semEdit.getText().toString().trim();
            String description=descripEdit.getText().toString().trim();
//            if(description.isEmpty())
//            {
//                description="Not available";
//            }
            String price=priceEdit.getText().toString().trim();

            ref=db.getReference("BLUserAccount").getRef().child(mAuth.getCurrentUser().getUid());

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String sn=snapshot.child(ConstantKeys.KEY_NAME).getValue().toString();
                    String sc=snapshot.child(ConstantKeys.KEY_PHONE).getValue().toString();
                    String mail=snapshot.child(ConstantKeys.KEY_EMAIL).getValue().toString();
                    addBook(bookName, authorName, publisherName, sem,description,price,sn,sc,mail);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });
    }


    private void addBook(String bookName, String authorName, String publisherName, String sem, String description, String price, String sellerName,String sellerPhone,String sellerEmail) {

        db=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        String uid=user.getUid();

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yy HH:mm");
        String timeStamp=sdf.format(calendar.getTime());

        String bookId=uid+timeStamp;


        Map<String , String> bookJson=new HashMap<>();
        bookJson.put(ConstantKeys.KEY_BOOK_NAME, bookName);
        bookJson.put(ConstantKeys.KEY_AUTHOR_NAME, authorName);
        bookJson.put(ConstantKeys.KEY_PUBLISHER_NAME, publisherName);
        bookJson.put(ConstantKeys.KEY_SEM, sem);
        bookJson.put(ConstantKeys.KEY_BOOK_DESCRIPTION, description);
        bookJson.put(ConstantKeys.KEY_BOOK_ID, bookId);
        bookJson.put(ConstantKeys.KEY_BOOK_PRICE, price);
        bookJson.put(ConstantKeys.KEY_SELLER_NAME, sellerName);
        bookJson.put(ConstantKeys.KEY_SELLER_PHONE, sellerPhone);
        bookJson.put(ConstantKeys.KEY_SELLER_EMAIL,sellerEmail);
        //bookJson.put(ConstantKeys.KEY_SELLER_EMAIL,sellerEmail);

        bknameInpt.setError(null);
        authorInpt.setError(null);
        publisherInpt.setError(null);
        semInpt.setError(null);
        descripInpt.setError(null);
        priceInpt.setError(null);

        node= db.getReference("UploadedBooks");

        if(bookName.isEmpty()){
            bknameInpt.setError("Can't be empty");
        }
        else if(authorName.isEmpty()){
            authorInpt.setError("Can't be empty");
        }
        else if(sem.isEmpty()){
            semInpt.setError("Can't be empty");
        }
        else if(price.isEmpty()){
            priceInpt.setError("Can't be empty");
        }
        else {
            node.child(bookId).setValue(bookJson)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "BOOK UPLOADED", Toast.LENGTH_SHORT).show();

                            //NAVIGATING SIGNUP TO LOGIN AFTER SUCCESSFUL REGISTRATION
                            Intent r = new Intent(Upload_NewBook.this, MainActivity.class);
                            startActivity(r);
                            this.finish();
                        } else {
                            Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show();
                            //mAuth.getCurrentUser().delete(); //TO DELETE THE CURRENT USER ACCOUNT FROM AUTHENTICATION
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed Due To " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        }
    }

    /**
     * adding menu to the app or action bar
     */
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
                Intent hm = new Intent(Upload_NewBook.this, MainActivity.class);
                startActivity(hm);
                break;

            case R.id.MyProfile_without_search:
                Intent mp = new Intent(Upload_NewBook.this, MyProfileActivity.class);
                startActivity(mp);
                break;

            case R.id.MyUploads_without_search:
                Intent unpmy = new Intent(Upload_NewBook.this, MyUploads.class);
                startActivity(unpmy);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Upload_NewBook.this);
        alertBuilder.setTitle("Exit message");
        alertBuilder.setMessage("Confirm to exit");

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //cancel operation
                Toast.makeText(Upload_NewBook.this,"Operation suspended", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        alertBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LocalSession localSession = new LocalSession(Upload_NewBook.this);
                localSession.clearAll();
                Intent r = new Intent(Upload_NewBook.this, Login_Activity.class);
                startActivity(r);//message passing object
                Toast.makeText(Upload_NewBook.this,"Logout successful", Toast.LENGTH_SHORT).show();
                Upload_NewBook.this.finish();
            }
        });
        alertBuilder.setCancelable(false); //auto cancel suspended
        alertBuilder.show();
    }
}