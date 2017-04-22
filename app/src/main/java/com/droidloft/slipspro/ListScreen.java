package com.droidloft.slipspro;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DroidLoft2 on 4/22/2017.
 */

public class ListScreen extends AppCompatActivity {

    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference entryRef = rootRef.child("entryRef");
    private ArrayList<String> fbDataArrayList;
    private RecyclerView recyclerView;
    private ArrayList listData;
    private ArrayList<String> dateArraylist;
    private ArrayList<String> typeArrayList;
    private ArrayList<String> descriptionArrayList;
    private ArrayList<String> amountArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        dateArraylist = new ArrayList<>();
        descriptionArrayList = new ArrayList<String>();
        amountArrayList = new ArrayList<String>();
        typeArrayList = new ArrayList<String>();



        entryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fbDataArrayList = new ArrayList<String>();
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    fbDataArrayList.add(String.valueOf(dsp.getValue()));
                }

                int count = 0;

                for (int i = 0; i < fbDataArrayList.size(); i++){
                    if(count < 5) {
                        String text = fbDataArrayList.get(i);
                        if(count == 0) {
                            dateArraylist.add(text);
                        }
                        if(count == 1) {
                            descriptionArrayList.add(text);
                        }
                        if(count == 2) {
                            amountArrayList.add(text);
                        }
                        if(count == 3) {
                            typeArrayList.add(text);
                            count = -1;
                        }
                        count++;
                    }


                }

                Log.e("DATES", String.valueOf(dateArraylist));
                Log.e("DESCRS", String.valueOf(descriptionArrayList));
                Log.e("AMOUNTS", String.valueOf(amountArrayList));
                Log.e("TYPES", String.valueOf(typeArrayList));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        listData = (ArrayList)getListData();
        recyclerView.setLayoutManager(new LinearLayoutManager(ListScreen.this));
        MyAdapter adapter = new MyAdapter(getListData(), ListScreen.this);
        recyclerView.setAdapter(adapter);




    }

    private List<ListItem> getListData() {
        String[] dates;
        String[] descriptions;
        String[] amounts;
        String[] types;

        List<ListItem> data = new ArrayList<>();

        dates = dateArraylist.toArray(new String[dateArraylist.size()]);
        descriptions = descriptionArrayList.toArray(new String[descriptionArrayList.size()]);
        amounts = amountArrayList.toArray(new String[amountArrayList.size()]);
        types = typeArrayList.toArray(new String[typeArrayList.size()]);

        for (int i = 0; i < dates.length && i < descriptions.length && i < amounts.length && i < types.length; i++){
            ListItem item = new ListItem();
            item.setDateText(dates[i]);
            item.setDescriptionText(descriptions[i]);
            item.setAmountText(amounts[i]);
            item.setTypeText(types[i]);
            data.add(item);
        }

        return data;
    }
}
