package com.amp.Screens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amp.R;
import com.amp.models.Skulist;

import java.util.ArrayList;

public class SKUAdapter extends RecyclerView.Adapter<SKUAdapter.myclass> {
    Context skuCodeActivity;
    ArrayList<Skulist> skulistArrayList;


    public SKUAdapter(SKUCodeActivity skuCodeActivity, ArrayList<Skulist> skulistArrayList) {

        this.skuCodeActivity = skuCodeActivity;
        this.skulistArrayList = skulistArrayList;
    }

    @NonNull
    @Override
    public myclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(skuCodeActivity).inflate(R.layout.skulayout, parent, false);
        myclass mycls = new myclass(view);
        return mycls;
    }

    @Override
    public void onBindViewHolder(@NonNull myclass holder, int position) {
        if (position % 2 == 0) {
            holder.bgmain.setBackgroundColor(skuCodeActivity.getResources().getColor(R.color.colorlight));
        }
        holder.size.setText(skulistArrayList.get(position).getSizeName());
        holder.qty.setText(""+skulistArrayList.get(position).getQty());
    }

    @Override
    public int getItemCount() {
        return skulistArrayList.size();
    }

    public class myclass extends RecyclerView.ViewHolder {
        TextView size;
        TextView qty;
        LinearLayout bgmain;

        public myclass(@NonNull View itemView) {
            super(itemView);
            size = itemView.findViewById(R.id.size);
            qty = itemView.findViewById(R.id.qty);
            bgmain = itemView.findViewById(R.id.bgmain);

        }
    }
}
