package com.droidloft.slipspro;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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

public class ListScreen extends AppCompatActivity implements MyAdapter.ItemClickCallback {

    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference entryRef = rootRef.child("entryRef");
    private ArrayList<String> fbDataArrayList;
    private ArrayList<String> keyArrayList;
    private RecyclerView recyclerView;
    private ArrayList listData;
    private ArrayList<String> dateArraylist;
    private ArrayList<String> typeArrayList;
    private ArrayList<String> descriptionArrayList;
    private ArrayList<String> amountArrayList;
    MyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        dateArraylist = new ArrayList<>();
        descriptionArrayList = new ArrayList<>();
        amountArrayList = new ArrayList<>();
        typeArrayList = new ArrayList<>();



        entryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fbDataArrayList = new ArrayList<>();
                keyArrayList = new ArrayList<>();

                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    keyArrayList.add(String.valueOf(dsp.getKey()));

                }

                for(int i = 0; i < keyArrayList.size(); i++){
                    String key = keyArrayList.get(i);
                    //fbDataArrayList.add(String.valueOf(dataSnapshot.child(key).getValue().toString()));
                    String date = dataSnapshot.child(key).child("dateRef").getValue().toString();
                    String amount = dataSnapshot.child(key).child("amountRef").getValue().toString();
                    String description = dataSnapshot.child(key).child("descRef").getValue().toString();
                    String type = dataSnapshot.child(key).child("typeRef").getValue().toString();

                    dateArraylist.add(date);
                    amountArrayList.add(amount);
                    descriptionArrayList.add(description);
                    typeArrayList.add(type);


                }

                Log.e("DATELIST: ", String.valueOf(dateArraylist));
                Log.e("AMOUNTLIST: ", String.valueOf(amountArrayList));
                Log.e("DESCRLIST: ", String.valueOf(descriptionArrayList));
                Log.e("TYPELIST: ", String.valueOf(typeArrayList));



                listData = (ArrayList) getListData();
                recyclerView.setLayoutManager(new LinearLayoutManager(ListScreen.this));
                adapter = new MyAdapter(getListData(), ListScreen.this);
                //adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                adapter.setItemClickCallback(ListScreen.this);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





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


        for (int i = 0; i < dates.length && i < descriptions.length && i < amounts.length && i < types.length; i++) {
            ListItem item = new ListItem();
            item.setDateText(dates[i]);
            item.setDescriptionText(descriptions[i]);
            item.setAmountText(amounts[i]);
            item.setTypeText(types[i]);
            data.add(item);
        }

        return data;
    }

    @Override
    public void onItemClick(int p) {
        ListItem item = (ListItem)listData.get(p);
        //ListItem item = (ListItem)listData.remove(p);
        String deleteKey = keyArrayList.get(p);
        entryRef.child(deleteKey).removeValue();
        String toastDescr = item.getDescriptionText();
        Toast.makeText(ListScreen.this, "deleted: " + toastDescr, Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
        getListData();

    }
}
