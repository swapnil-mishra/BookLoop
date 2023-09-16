package com.mypackage.bookloop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MyUploadsAdapter extends RecyclerView.Adapter<MyUploadsAdapter.MyUploadsHolder> {

    Context mContext;
    private List<BookListModel> bookListFull;
    FirebaseAuth auth;
    DatabaseReference database;

    public MyUploadsAdapter(Context mContext, List<BookListModel> bookList) {
        this.mContext = mContext;
        this.bookListFull = bookList;
    }

    @NonNull
    @Override
    public MyUploadsAdapter.MyUploadsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        database= FirebaseDatabase.getInstance().getReference("UploadedBooks");

        LayoutInflater inflater= LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.single_row_my_uploads,parent,false);
        MyUploadsAdapter.MyUploadsHolder myHolder= new MyUploadsHolder(itemView);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyUploadsHolder holder, int position) {

        BookListModel bookListModel= bookListFull.get(position);
        holder.bookName.setText(bookListModel.getBookName());
        holder.bookPrice.setText("Price: "+bookListModel.getBookPrice());
        String thisBookId=bookListModel.getBookID();


        holder.editMyUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(mContext,"Edit clicked",Toast.LENGTH_SHORT).show();
                /**
                 * CODE TO EDIT BOOK DETAILS SHOULD BE ADDED!
                 */
                Intent intent=new Intent(mContext,EditBookDetails.class);

                intent.putExtra(ConstantKeys.KEY_AUTHOR_NAME, bookListModel.getAuthorName());
                intent.putExtra(ConstantKeys.KEY_BOOK_DESCRIPTION, bookListModel.getBookDescription());
                intent.putExtra(ConstantKeys.KEY_BOOK_ID, bookListModel.getBookID());
                intent.putExtra(ConstantKeys.KEY_BOOK_NAME, bookListModel.getBookName());
                intent.putExtra(ConstantKeys.KEY_BOOK_PRICE, bookListModel.getBookPrice());
                intent.putExtra(ConstantKeys.KEY_PUBLISHER_NAME, bookListModel.getPublisherName());
                intent.putExtra(ConstantKeys.KEY_SELLER_NAME, bookListModel.getSellerName());
                intent.putExtra(ConstantKeys.KEY_SELLER_PHONE, bookListModel.getSellerPhone());
                intent.putExtra(ConstantKeys.KEY_SEM, bookListModel.getSem());
                mContext.startActivity(intent);
            }
        });


        holder.deleteMyUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * CODE TO DELETE BOOK ITEM FROM MyUploads.
                 * ALERT DIALOG BOX NEED TO BE ADDED!
                 */
                //database.child(thisBookId).removeValue();

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                alertBuilder.setTitle("DELETE MESSAGE");
                alertBuilder.setMessage("You cannot retrieve the book item later.");

                alertBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancel operation
                        Toast.makeText(mContext,"Cancelled", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                alertBuilder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        database.child(thisBookId).removeValue();
                        Toast.makeText(mContext,"Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                alertBuilder.setCancelable(false); //auto cancel suspended
                alertBuilder.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return bookListFull.size();
    }

    public class MyUploadsHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView bookName, bookPrice, editMyUploads, deleteMyUploads;


        public MyUploadsHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.ui_card_my_uploads);
            bookName=itemView.findViewById(R.id.ui_book_name_mu);
            bookPrice=itemView.findViewById(R.id.ui_book_price_mu);
            editMyUploads=itemView.findViewById(R.id.edit_myuploads);
            deleteMyUploads=itemView.findViewById(R.id.delete_myuploads);
        }
    }

}
