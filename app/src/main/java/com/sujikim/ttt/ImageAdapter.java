package com.sujikim.ttt;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by 김수영 on 2017-12-05.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

//    public Integer[] mThumbIds = {
//            R.drawable.test1, R.drawable.test2,
//    R.drawable.test3, R.drawable.test4,
//    R.drawable.test5, R.drawable.test6,
//    R.drawable.test7, R.drawable.test8,
//    };
//    public ImageAdapter(Context c){
//         mContext = c;
//    }

//    private Bitmap image = null;
    private Bitmap[] images = null;

    public ImageAdapter(Context c, Bitmap[] getImages){
        mContext = c;
//        image = getImages;
        images = getImages;
    }

    @Override
    public int getCount() {
//        return mThumbIds.length;
//        return 1;
        return images.length;
    }

    @Override
    public Object getItem(int position){
//        return mThumbIds[position];
//        return image;
        return images[position];
    }

    @Override
    public long getItemId(int position){
//        return  0;
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
//        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
        imageView.setPadding(8,8,8,8);
        imageView.setImageBitmap(images[position]);
        return imageView;
    }
}
