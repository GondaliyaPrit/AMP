package com.amp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amp.R;

import java.util.ArrayList;

public class FabricImagesAdapter extends RecyclerView.Adapter<FabricImagesAdapter.ImageClass> {
    Context context;
    ArrayList<Uri> images;

    public FabricImagesAdapter(Context context, ArrayList<Uri> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.images_items, parent, false);
        ImageClass imageClass = new ImageClass(view);
        return imageClass;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageClass holder, int position) {
        holder.imgfadle.setImageURI(images.get(position));
    }


    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ImageClass extends RecyclerView.ViewHolder {
        ImageView imgfadle;

        public ImageClass(@NonNull View itemView) {
            super(itemView);
            imgfadle = itemView.findViewById(R.id.imgfadle);
        }
    }
}
