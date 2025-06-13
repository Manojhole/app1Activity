package com.example.fayaz.graphicalpassword;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String ImageType;
    public ImageAdapter(Context c,String type)
    {
        mContext = c;
        ImageType = type;

    }
    @Override
    public int getCount() {
        switch (ImageType)
        {
            case "Flowers":
                return mFlowers.length;
            case "Fruits":
                return mFruits.length;
            case "Smileys":
                return mSmileys.length;
            default:
                return mFlowers.length;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }
    public Integer getSelectedItem(int position) {
        switch (ImageType)
        {
            case "Flowers":
                return mFlowers[position];
            case "Fruits":
                return mFruits[position];
            case "Smileys":
                return mSmileys[position];
            default:
                return mFlowers[position];
        }
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null)
        {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200,200));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(8,8,8,8);
        }
        else
        {
            imageView = (ImageView)convertView;
        }
        switch (ImageType)
        {
            case "Flowers":
                imageView.setImageResource(mFlowers[position]);
                break;
            case "Fruits":
                imageView.setImageResource(mFruits[position]);
                break;
            case "Smileys":
                imageView.setImageResource(mSmileys[position]);
                break;
            default:
                imageView.setImageResource(mFlowers[position]);
                break;
        }

        return imageView;
    }

    public Integer[] mFlowers =
            {R.drawable.flower1,R.drawable.flower2,R.drawable.flower3,R.drawable.flower4,R.drawable.flower5,R.drawable.flower6,R.drawable.flower7,R.drawable.flower8,R.drawable.flower9};
    public Integer[] mFruits =
            {R.drawable.fruit1,R.drawable.fruit2,R.drawable.fruit3,R.drawable.fruit4,R.drawable.fruit5,R.drawable.fruit6,R.drawable.fruit7,R.drawable.fruit8,R.drawable.fruit9};
    public Integer[] mSmileys =
            {R.drawable.smily1,R.drawable.smily2,R.drawable.smily3,R.drawable.smily4,R.drawable.smily5,R.drawable.smily6,R.drawable.smily7,R.drawable.smily8,R.drawable.smily9};
}
