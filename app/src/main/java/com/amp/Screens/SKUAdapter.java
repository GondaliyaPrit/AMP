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

public class SKUAdapter extends RecyclerView.Adapter<SKUAdapter.myclass> {
    Context skuCodeActivity;
    String[]  Size ;
    String[]  Qty ;
    public SKUAdapter(SKUCodeActivity skuCodeActivity, String[] size, String[] qty) {
        this.skuCodeActivity =skuCodeActivity ;
        this.Size =size ;
        this.Qty =qty ;

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
        if (position %2 ==0)
        {
            holder.bgmain.setBackgroundColor(skuCodeActivity.getResources().getColor(R.color.colorlight));
        }
        holder.size.setText(Size[position]);
        holder.qty.setText(Qty[position]);
    }

    @Override
    public int getItemCount() {
        return Size.length;
    }

    public class myclass extends RecyclerView.ViewHolder {
        TextView size;
        TextView qty;
        LinearLayout bgmain ;

        public myclass(@NonNull View itemView) {
            super(itemView);
            size = itemView.findViewById(R.id.size);
            qty = itemView.findViewById(R.id.qty);
            bgmain = itemView.findViewById(R.id.bgmain);

        }
    }
}
