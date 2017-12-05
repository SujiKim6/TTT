package com.sujikim.ttt;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 김수영 on 2017-12-05.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList data = new ArrayList();

    public ImageAdapter(Context c, ArrayList getImages){
        mContext = c;
        data = getImages;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position){
        return 0;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
        imageView.setPadding(8,8,8,8);
        Bitmap item = (Bitmap) data.get(position);
        imageView.setImageBitmap(item);
        return imageView;
    }
}
