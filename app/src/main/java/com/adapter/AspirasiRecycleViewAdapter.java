package com.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.Model.Aspirasi;
import com.adapter.dprdbottom.R;
import com.example.dprdbottom.DetailAspirasi;

import java.util.ArrayList;


public class AspirasiRecycleViewAdapter extends RecyclerView.Adapter<AspirasiRecycleViewAdapter.AspirasiViewHolder> {
    private ArrayList<Aspirasi> dataList;
    public AspirasiRecycleViewAdapter(ArrayList<Aspirasi> dataList) {
        this.dataList = dataList;
    }
    @Override
    public AspirasiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_contact_item, parent, false);
        return new AspirasiViewHolder(view);
    }
    @Override
    public void onBindViewHolder(AspirasiViewHolder holder, int position) {

        final  Aspirasi detail = dataList.get(position);

        holder.userName.setText(dataList.get(position).getUserName());
        holder.komisi.setText(dataList.get(position).getKomisi());
        holder.message.setText(dataList.get(position).getMessage());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                Toast.makeText(v.getContext(), "TES", Toast.LENGTH_SHORT).show();
                intent = new Intent(v.getContext(), DetailAspirasi.class);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }


    public class AspirasiViewHolder extends RecyclerView.ViewHolder{
        private TextView userName, komisi, message;
        private CardView cardview;

        public AspirasiViewHolder(View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.nama);
            komisi = (TextView) itemView.findViewById(R.id.komisiname);
            message = (TextView) itemView.findViewById(R.id.message);
            cardview = itemView.findViewById(R.id.cardviewaspirasi);


        }
    }
}
