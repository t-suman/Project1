package com.example.project1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PeopleYouMayKnow extends AppCompatActivity {


    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_you_may_know);

        final ArrayList<CustomClass> items=new ArrayList();

        final ListAdapter adapter=new ListAdapter(PeopleYouMayKnow.this,0,items);

        list=findViewById(R.id.list);

        //list.setAdapter(adapter);


        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.whereNotEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
        query.addAscendingOrder("username");

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null){
                    if(objects!=null){
                        int i=0;
                        for(ParseUser object:objects) {
                            String name=object.getUsername();
                            items.add(new CustomClass(name));
                            ParseQuery<ParseObject> query2;
                            query2=ParseQuery.getQuery("ProfilePic");
                            query2.whereEqualTo("username", name);
                            query2.orderByDescending("updatedAt");
                            try {
                                if(query2.count() == 0){
                                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defaultprofile);
                                    items.get(i).image=bitmap;
                                }
                                else{
                                    final int finalI = i;
                                    query2.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {
                                            if(e==null){
                                                if(objects.size()!=0){
                                                    ParseFile file=objects.get(0).getParseFile("profilepic");

                                                    file.getDataInBackground(new GetDataCallback() {
                                                        @Override
                                                        public void done(byte[] data, ParseException e) {
                                                            Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                                                            items.get(finalI).image=bitmap;
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                                }
                            } catch (ParseException ee) {
                                ee.printStackTrace();
                            }
                            i++;
                        }
                        list.setAdapter(adapter);

                    }
                }
                else {
                    e.printStackTrace();
                }
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(PeopleYouMayKnow.this,ViewOtherProfile.class);
                intent.putExtra("username",items.get(position).name);
                startActivity(intent);
            }
        });


    }
}
