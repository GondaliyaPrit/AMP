package com.amp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amp.R;

public class FabricListAdapter extends RecyclerView.Adapter<FabricListAdapter.ListClass> {
    Context context;
    public FabricListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ListClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.infabric_items,parent,false);
        ListClass listClass = new ListClass(view);
        return listClass;
    }

    @Override
    public void onBindViewHolder(@NonNull ListClass holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ListClass extends RecyclerView.ViewHolder {
        public ListClass(@NonNull View itemView) {
            super(itemView);
        }
    }
}
