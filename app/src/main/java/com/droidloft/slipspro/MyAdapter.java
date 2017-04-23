package com.droidloft.slipspro;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DroidLoft2 on 4/22/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.RecyclerHolder> {

    private List<ListItem> listData;
    private LayoutInflater inflater;
    private ItemClickCallback itemClickCallback;

    interface ItemClickCallback {
        void onItemClick(int p);
    }

    MyAdapter(List<ListItem> listData, Context c){
        this.inflater = LayoutInflater.from(c);
        this.listData = listData;
    }

    void setItemClickCallback(final ItemClickCallback inItemClickCallback){
        this.itemClickCallback = inItemClickCallback;
    }

    @Override
    public MyAdapter.RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.RecyclerHolder holder, int position) {
        ListItem item = listData.get(position);
        holder.dateText.setText(item.getDateText());
        holder.descriptionText.setText(item.getDescriptionText());
        holder.amountText.setText(item.getAmountText());
        holder.typeText.setText(item.getTypeText());


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView dateText;
        private TextView typeText;
        private TextView descriptionText;
        private TextView amountText;
        private View container;

        public RecyclerHolder(View itemView) {
            super(itemView);
            dateText = (TextView)itemView.findViewById(R.id.item_date_textview);
            typeText = (TextView)itemView.findViewById(R.id.item_type_textview);
            descriptionText = (TextView)itemView.findViewById(R.id.item_description_textview);
            amountText = (TextView)itemView.findViewById(R.id.item_amount_textview);
            container = itemView.findViewById(R.id.item_root_container);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.item_root_container){
                itemClickCallback.onItemClick(getAdapterPosition());
            }

        }
    }
}

