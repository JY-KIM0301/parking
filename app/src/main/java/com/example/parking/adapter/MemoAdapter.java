package com.example.parking.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parking.R;
import com.example.parking.model.Memo;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ItemViewHolder> {
    private List<Memo> listdata;

    public MemoAdapter(List<Memo> listdata) {
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Memo memo = listdata.get(position);

        holder.maintext.setText(memo.getMaintext());
        holder.subtext.setText(memo.getSubtext());

        if (memo.getIsdone() == 0) {
            holder.img.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.img.setBackgroundColor(Color.GREEN);
        }
    }

    public void addItem(Memo memo) {
        listdata.add(memo);
    }

    void removeItem(int position) {
        listdata.remove(position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView maintext;
        private TextView subtext;
        private ImageView img;

        public ItemViewHolder(@NonNull final View itemview) {
            super(itemview);

            maintext = itemview.findViewById(R.id.item_maintext);
            subtext = itemview.findViewById(R.id.item_subtext);
            img = itemview.findViewById(R.id.item_image);
        }
    }

}
