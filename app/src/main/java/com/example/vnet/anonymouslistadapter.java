package com.example.vnet;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.chinalwb.are.render.AreTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class anonymouslistadapter extends RecyclerView.Adapter<anonymouslistadapter.ViewHolder> {
    private List<anonymouslistitem> listItems;
    private Context context;
    DatabaseReference dbr,user,userdis;
    FirebaseAuth auth;
    boolean checklike=false,checkdislike=false;


    public anonymouslistadapter(List<anonymouslistitem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
        auth=FirebaseAuth.getInstance();
        user=FirebaseDatabase.getInstance().getReference("UserLikesandDislikes").child("Likes").child(auth.getCurrentUser().getUid());
        userdis=FirebaseDatabase.getInstance().getReference("UserLikesandDislikes").child("Dislikes").child(auth.getCurrentUser().getUid());

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.anonymous_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final anonymouslistitem item=listItems.get(position);
        dbr= FirebaseDatabase.getInstance().getReference(item.getLocation());
        holder.edit.fromHtml(item.getPost());
        holder.likes.setText(item.getLikes());
        holder.dislikes.setText(item.getDislikes());

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot d:dataSnapshot.getChildren()){
                            if(item.getId().equals(d.getValue().toString())){
                                checklike=true;
                                break;
                            }
                        }
                        if(checklike==false){
                            item.setLikes((Integer.parseInt(item.getLikes()) + 1) + "");


                            user.child(user.push().getKey()).setValue(item.getId());

                            //if the same dislikes who has liked then only we deduct it and add it to dislike
                            //  item.setDislikes((Integer.parseInt(item.getDislikes())-1)+"");
                            dbr.child(item.getId()).setValue(item);
                            checklike=false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        }
        });
        holder.dislike.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //item.setLikes((Integer.parseInt(item.getLikes())-1)+"");
                userdis.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot d:dataSnapshot.getChildren()){
                            if(item.getId().equals(d.getValue().toString())){
                                checkdislike=true;
                                break;
                            }
                        }
                        if(checkdislike==false){
                            userdis.child(userdis.push().getKey()).setValue(item.getId());
                            item.setDislikes((Integer.parseInt(item.getDislikes())+1)+"");

                            dbr.child(item.getId()).setValue(item);
                            checkdislike=false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }




    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView likes,dislikes;
        public AreTextView edit;

        public ImageButton like,dislike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
edit=(AreTextView) itemView.findViewById(R.id.areditoreial);

            likes=(TextView)itemView.findViewById(R.id.likes);
            like=(ImageButton)itemView.findViewById(R.id.Like);
            dislike=(ImageButton)itemView.findViewById(R.id.Dislike);
            dislikes=(TextView)itemView.findViewById(R.id.dislikes);

        }
    }
}
