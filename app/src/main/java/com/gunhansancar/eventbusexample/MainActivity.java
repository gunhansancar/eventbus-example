package com.gunhansancar.eventbusexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.gunhansancar.eventbusexample.adapter.SimpleRecyclerAdapter;
import com.gunhansancar.eventbusexample.event.ServiceEvent;
import com.gunhansancar.eventbusexample.event.SimpleEvent;
import com.gunhansancar.eventbusexample.service.MyService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    private SimpleRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        adapter = new SimpleRecyclerAdapter();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @OnClick(R.id.startButton)
    public void onStartClicked() {
        Toast.makeText(this, "Timer is started.", Toast.LENGTH_SHORT).show();
        startService(new Intent(this, MyService.class));
    }

    @OnClick(R.id.stopButton)
    public void onStopClicked() {
        Toast.makeText(this, "Timer is stopped.", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new ServiceEvent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(SimpleEvent event) {
        adapter.append(event);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            adapter.delete(viewHolder.getAdapterPosition());
        }
    };

}
