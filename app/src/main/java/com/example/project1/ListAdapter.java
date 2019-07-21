package com.example.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<CustomClass> {

    Context con;
    ArrayList<CustomClass> list;

    public ListAdapter(Context context, int resource, ArrayList<CustomClass> objects) {
        super(context, resource, objects);
        con=context;
        list=objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView=convertView;
        itemView= LayoutInflater.from(con).inflate(R.layout.people,parent,false);

        CustomClass item=getItem(position);

        ImageView image=itemView.findViewById(R.id.imagev);
        image.setImageBitmap(item.image);
        //image.setImageResource(R.drawable.defaultprofile);

        TextView text=itemView.findViewById(R.id.name);
        text.setText(item.name);

        return itemView;

    }
}
