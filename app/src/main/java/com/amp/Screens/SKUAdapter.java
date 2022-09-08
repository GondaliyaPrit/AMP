package com.amp.Screens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amp.R;
import com.amp.models.Skulist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SKUAdapter extends RecyclerView.Adapter<SKUAdapter.myclass> {
    Context skuCodeActivity;
    ArrayList<Skulist> skulistArrayList;
    HashMap<Integer, Integer> summap = new HashMap<>();

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

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull myclass holder, int position) {
        int[] sumdata = new int[skulistArrayList.size()];

        if (position != skulistArrayList.size()) {
            if (position % 2 == 0) {
                holder.bgmain.setBackgroundColor(skuCodeActivity.getResources().getColor(R.color.colorlight));
            }
            holder.size.setText(skulistArrayList.get(position).getSizeName());
            holder.qty.setText("" + skulistArrayList.get(position).getQty());
            holder.totalsum.setText("" + skulistArrayList.get(position).getSum());
        }
        if (position == skulistArrayList.size() - 1) {
            holder.totalll.setVisibility(View.VISIBLE);
            holder.submitbtnlayout.setVisibility(View.VISIBLE);
        }

        holder.qtyedt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("datadatdat",charSequence.toString());
                if(!charSequence.toString().isEmpty()){
                    summap.put(position, Integer.parseInt(charSequence.toString()));
                    Log.e("Map data", "" + summap);
                    holder.txttotalqty.setText("" + summap.get(position));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = 0;
                JSONArray jsonArray = new JSONArray();
                for(Map.Entry<Integer, Integer> entry : summap.entrySet()){
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("SizeID",skulistArrayList.get(entry.getKey()).getSizeID());
                        jsonObject.put("Qty",Integer.parseInt(entry.getValue().toString()));
                        jsonArray.put(jsonObject);
                        index++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.e("json array", "" + jsonArray);
            }
        });
    }

    @Override
    public int getItemCount() {
        return skulistArrayList.size();
    }


    public class myclass extends RecyclerView.ViewHolder {
        TextView size;
        TextView qty;
        TextView totalsum, txttotalqty, btnsubmit;
        EditText qtyedt;
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
            txttotalqty = itemView.findViewById(R.id.txttotalqty);
            btnsubmit = itemView.findViewById(R.id.btnsubmit);
        }
    }
}
