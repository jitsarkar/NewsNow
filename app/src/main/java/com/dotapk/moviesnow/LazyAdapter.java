package com.dotapk.moviesnow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dotapk.moviesnow.R;
import com.dotapk.moviesnow.imageutils.ImageLoader;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private String[] data;
    private String[] img;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, String[] d,String[] i) {
        activity = a;
        img=d;
        data=i;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.single, null);

        TextView text=(TextView)vi.findViewById(R.id.textView1);;
        ImageView image=(ImageView)vi.findViewById(R.id.imageView1);
        text.setText(data[position]);
        imageLoader.DisplayImage(img[position], image);
        return vi;
    }
}