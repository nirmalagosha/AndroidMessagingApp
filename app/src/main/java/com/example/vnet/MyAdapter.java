package com.example.vnet;

import android.content.ActivityNotFoundException;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ListItem> listItems;
    private Context context;

    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
   final ListItem item=listItems.get(i);
    viewHolder.title1.setText(item.getTitle());

    viewHolder.description1.setText(item.getDescription());
    viewHolder.author1.setText(item.getAuthor());
       Picasso.with(context).load(item.getUrl()).fit()
                .centerCrop().into(viewHolder.imageView1);
       viewHolder.layout1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String urlString = item.getUrltoNews();
               Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(urlString));
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               intent.setPackage("com.android.chrome");
               try {
                   context.startActivity(intent);
               } catch (ActivityNotFoundException ex) {
                   // Chrome browser presumably not installed so allow user to choose instead
                   intent.setPackage(null);
                   context.startActivity(intent);
               }
           }
       });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

public TextView title1,description1,author1;
public ImageView imageView1;
public RelativeLayout layout1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title1=(TextView)itemView.findViewById(R.id.title);

            description1=(TextView)itemView.findViewById(R.id.description);
            author1=(TextView)itemView.findViewById(R.id.author);
            imageView1=(ImageView)itemView.findViewById(R.id.image);
            layout1=(RelativeLayout) itemView.findViewById(R.id.recycleritem1234);

        }
    }
}
