package com.gunhansancar.eventbusexample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gunhansancar.eventbusexample.R;
import com.gunhansancar.eventbusexample.event.SimpleEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gunhansancar on 06/04/16.
 */
public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.ItemHolder> {
    private static int[] COLORS = {
            R.color.blue_500,
            R.color.green_500,
            R.color.orange_500,
            R.color.pink_500
    };
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);


    private List<SimpleEvent> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @Override
    public SimpleRecyclerAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_simple_event, parent, false);
        return new ItemHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(SimpleRecyclerAdapter.ItemHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void append(SimpleEvent event) {
        list.add(event);
        notifyItemInserted(getItemCount());
    }

    public void delete(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(ItemHolder item, int position);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.textView1) TextView textView1;
        @Bind(R.id.textView2) TextView textView2;
        @Bind(R.id.parentLayout) View parentLayout;

        private SimpleRecyclerAdapter adapter;

        public ItemHolder(View itemView, SimpleRecyclerAdapter parent) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            this.adapter = parent;
        }

        public void bind(SimpleEvent item) {
            parentLayout.setBackgroundResource(COLORS[item.getId() % COLORS.length]);
            textView1.setText(String.format(Locale.ENGLISH, "Event ID: %d", item.getId()));
            textView2.setText(DATE_FORMAT.format(item.getDate()));
        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = adapter.getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(this, getAdapterPosition());
            }
        }
    }
}