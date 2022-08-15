package com.amp.Screens;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amp.R;
import com.amp.models.Skulist;

import java.util.ArrayList;

public class SKUAdapter extends RecyclerView.Adapter<SKUAdapter.myclass> {
    Context skuCodeActivity;
    ArrayList<Skulist> skulistArrayList;
    int sum =0 ;
    int  total  ;


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
        Log.e("position",""+position);
        Log.e("size",""+skulistArrayList.size());
        if(position != skulistArrayList.size()){
            if (position % 2 == 0) {
                holder.bgmain.setBackgroundColor(skuCodeActivity.getResources().getColor(R.color.colorlight));
            }
            holder.size.setText(skulistArrayList.get(position).getSizeName());
            holder.qty.setText(""+skulistArrayList.get(position).getQty());
            holder.totalsum.setText(""+skulistArrayList.get(position).getSum());

        }
        if(position == skulistArrayList.size()-1){
            holder.totalll.setVisibility(View.VISIBLE);
            holder.submitbtnlayout.setVisibility(View.VISIBLE);
        }

        holder.qtyedt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.equals("") ) {
                    //do your work here
                    Log.e("onTextChanged",""+s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("afterTextChanged",""+s.toString());
                 total  = Integer.parseInt(s.toString());


            }
        });

        sum+= total ;
        Log.e("sasasasdsdsdsdsdsdsds",""+sum);


    }

    @Override
    public int getItemCount() {
        return skulistArrayList.size();
    }

    public class myclass extends RecyclerView.ViewHolder {
        TextView size;
        TextView qty;
        TextView totalsum;
        EditText qtyedt ;
        LinearLayout bgmain;
        LinearLayout totalll;
        LinearLayout submitbtnlayout;

        public myclass(@NonNull View itemView) {
            super(itemView);
            size = itemView.findViewById(R.id.size);
            qty = itemView.findViewById(R.id.qty);
            bgmain = itemView.findViewById(R.id.bgmain);
            totalsum = itemView.findViewById(R.id.totalsum);
            totalll = itemView.findViewById(R.id.totalll);
            submitbtnlayout = itemView.findViewById(R.id.submitbtnlayout);
            qtyedt = itemView.findViewById(R.id.qtyedt);

        }
    }
}
