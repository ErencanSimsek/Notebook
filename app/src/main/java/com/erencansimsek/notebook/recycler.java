package com.erencansimsek.notebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.erencansimsek.notebook.databinding.RecyclerBinding;

import java.util.ArrayList;

public class recycler extends RecyclerView.Adapter<recycler.recycler2> {
    ArrayList<dosya> dosyaArrayList;

    public recycler(ArrayList<dosya> dosyaArrayList) {
        this.dosyaArrayList = dosyaArrayList;
    }

    @Override
    public recycler2 onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerBinding recyclerGosterBinding = RecyclerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new recycler2(recyclerGosterBinding);
    }

    @Override
    public void onBindViewHolder(recycler.recycler2 holder, int position) {
        holder.binding.textView.setText(dosyaArrayList.get(position).name);
        //holder.binding.card.setCardBackgroundColor(Color.RED);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), NotActivity.class);
                intent.putExtra("notId", dosyaArrayList.get(holder.getAdapterPosition()).Id);
                intent.putExtra("info", "old");
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dosyaArrayList.size();
    }

    public class recycler2 extends RecyclerView.ViewHolder {
        RecyclerBinding binding;

        public recycler2(RecyclerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
