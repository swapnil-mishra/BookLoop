package com.mypackage.bookloop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyUploads extends AppCompatActivity {

    FirebaseUser user;
    RecyclerView recyclerView;
    DatabaseReference database;
    MyUploadsAdapter myUploadsAdapter;
    List<BookListModel> bookListModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_uploads);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#45adda\">" + "Book" + "</font>"+"<font color=\"#ffffff\">" + "Loop" + "</font>"));

        user=FirebaseAuth.getInstance().getCurrentUser();
        String myEmail= user.getEmail();

        recyclerView =findViewById(R.id.recycler_list_myUploads);
        database= FirebaseDatabase.getInstance().getReference("UploadedBooks");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookListModelList= new ArrayList<BookListModel>();
        myUploadsAdapter=new MyUploadsAdapter(this,bookListModelList);
        recyclerView.setAdapter(myUploadsAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookListModelList.clear();


                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    BookListModel bookListModel=dataSnapshot.getValue(BookListModel.class);
                    if(bookListModel.getSellerEmail().equals(myEmail)){
                        bookListModelList.add(bookListModel);
                    }
                }
                myUploadsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
                Intent hm = new Intent(MyUploads.this, MainActivity.class);
                startActivity(hm);
                break;

            case R.id.MyProfile_without_search:
                Intent mp = new Intent(MyUploads.this, MyProfileActivity.class);
                startActivity(mp);
                break;

            case R.id.MyUploads_without_search:
                Intent unpmy = new Intent(MyUploads.this, MyUploads.class);
                startActivity(unpmy);
                break;
            case R.id.Upload_new_book_without_search:
                Intent upl = new Intent(MyUploads.this, Upload_NewBook.class);
                startActivity(upl);//message passing object
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MyUploads.this);
        alertBuilder.setTitle("Exit message");
        alertBuilder.setMessage("Confirm to exit");

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //cancel operation
                Toast.makeText(MyUploads.this,"Operation suspended", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        alertBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LocalSession localSession = new LocalSession(MyUploads.this);
                localSession.clearAll();
                Intent r = new Intent(MyUploads.this, Login_Activity.class);
                startActivity(r);//message passing object
                Toast.makeText(MyUploads.this,"Logout successful", Toast.LENGTH_SHORT).show();
                MyUploads.this.finish();
            }
        });
        alertBuilder.setCancelable(false); //auto cancel suspended
        alertBuilder.show();
    }
}
