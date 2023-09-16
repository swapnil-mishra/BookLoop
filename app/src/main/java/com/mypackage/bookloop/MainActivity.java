package com.mypackage.bookloop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView recyclerView;
    DatabaseReference database;
    RVAdapter rvAdapter;
    List<BookListModel> bookListModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#45adda\">" + "Book" + "</font>"+"<font color=\"#ffffff\">" + "Loop" + "</font>"));

        auth=FirebaseAuth.getInstance();

        //RecyclerView start

        recyclerView =findViewById(R.id.recycler_list);
        database= FirebaseDatabase.getInstance().getReference("UploadedBooks");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookListModelList= new ArrayList<BookListModel>();
        rvAdapter=new RVAdapter(this,bookListModelList);
        recyclerView.setAdapter(rvAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookListModelList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    BookListModel bookListModel=dataSnapshot.getValue(BookListModel.class);
                    bookListModelList.add(bookListModel);
                }
                rvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //RecyclerViewEnd
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("search here");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return true;
    }


    private void filter(String searchKey) {
        ArrayList<BookListModel> filteredlist = new ArrayList<>();
        for (BookListModel book : bookListModelList) {
            if(book.getBookName().toLowerCase().contains(searchKey) || book.getAuthorName().toLowerCase().contains(searchKey)
                    || book.getBookDescription().toLowerCase().contains(searchKey) || book.getPublisherName().toLowerCase().contains(searchKey)
                    || book.getBookPrice().toLowerCase().contains(searchKey) || book.getSellerName().toLowerCase().contains(searchKey)){
                filteredlist.add(book);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            rvAdapter.filterList(filteredlist);
        }
    }

    //method to generate event against the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) //allow us to use the id against the menu itself
        {
            case R.id.Logout:
                logout();
                break;
            case R.id.MyProfile:
                Intent mp = new Intent(MainActivity.this, MyProfileActivity.class);
                startActivity(mp);
                break;
            case R.id.MyUploads:
                Intent mu = new Intent(MainActivity.this, MyUploads.class);
                startActivity(mu);
                break;
            case R.id.Upload_new_book:
                Intent upl = new Intent(MainActivity.this, Upload_NewBook.class);
                startActivity(upl);//message passing object
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        alertBuilder.setTitle("Exit message");
        alertBuilder.setMessage("Confirm to exit");

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //cancel operation
                Toast.makeText(MainActivity.this,"Operation suspended", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LocalSession localSession = new LocalSession(MainActivity.this);
                localSession.clearAll();
                Intent r = new Intent(MainActivity.this, Login_Activity.class);
                startActivity(r);//message passing object
                Toast.makeText(MainActivity.this,"Logged out successfully", Toast.LENGTH_SHORT).show();
                MainActivity.this.finish();
            }
        });
        alertBuilder.setCancelable(false); //auto cancel suspended
        alertBuilder.show();

    }
}




