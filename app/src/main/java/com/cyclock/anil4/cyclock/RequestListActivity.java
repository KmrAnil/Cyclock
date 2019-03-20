package com.cyclock.anil4.cyclock;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference rldatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        rldatabase=FirebaseDatabase.getInstance().getReference().child("Request_List");
        rldatabase.keepSynced(true);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ListData,BlogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<ListData, BlogViewHolder>
                (ListData.class,R.layout.requset_list,BlogViewHolder.class,rldatabase) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, final ListData model, int position) {
                viewHolder.setEmail(model.getEmail());

                final String user_id = getRef(position).getKey();

                viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent = new Intent(RequestListActivity.this, PopUpWindow.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);
                    }
                });


            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View view;
        public RelativeLayout relativeLayout;
        private OnItemClickListener listener;
        public interface OnItemClickListener{
            void OnItemClick(int position);
        }
        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener=listener;
        }
        public BlogViewHolder(View itemView){

            super(itemView);
            view =itemView;
            relativeLayout =itemView.findViewById(R.id.relativeLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!= RecyclerView.NO_POSITION && listener !=null){
                        listener.OnItemClick(position);
                    }
                }
            });
        }
        public void setEmail(String title){
            TextView post_tite=view.findViewById(R.id.requestEmail);
            post_tite.setText(title);
        }
        }
    }


