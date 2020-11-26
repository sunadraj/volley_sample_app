package com.example.volleysampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<MainData> dataArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listview);

        String url="https://picsum.photos/v2/list";

        //initialize progress dialog
        final ProgressDialog dialog=new ProgressDialog(this);

        dialog.setMessage("please wait...");
        //setcancelable true
        dialog.setCancelable(true);

        dialog.show();

        StringRequest request=new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response!=null){

                            dialog.dismiss();

                            try {
                                JSONArray jsonArray=new JSONArray(response);
                                parseArray(jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void parseArray(JSONArray jsonArray) {

        for(int i=1;i<=jsonArray.length();i++){
            try{
                //initialize jsonobject
                JSONObject jsonObject=jsonArray.getJSONObject(i);

                //initialize main data

                MainData data=new MainData();

                //set name
                data.setName(jsonObject.getString("author"));

                //
                data.setImage(jsonObject.getString("download_url"));
                //adding data in arraylist

                dataArrayList.add(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            listView.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return dataArrayList.size();
                }

                @Override
                public Object getItem(int position) {
                    return 0;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
View view=getLayoutInflater().inflate(
        R.layout.item_main,null);
            //initialize main data
                    MainData data=dataArrayList.get(position);

                    //initialize and assign variables

                    ImageView imageView=view.findViewById(R.id.imageview);
                    TextView textView=view.findViewById(R.id.textview);

                    //set image with imageview

                    Glide.with(getApplicationContext())
                            .load(data.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
                    textView.setText(data.getName());

                    return view;
                }
            });
        }

    }
}