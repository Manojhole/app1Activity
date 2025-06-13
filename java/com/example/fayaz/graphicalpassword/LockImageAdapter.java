package com.example.fayaz.graphicalpassword;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class LockImageAdapter extends BaseAdapter {
    private Context mContext;
    private String ImageType;

    public LockImageAdapter(Context c,String type)
    {
        mContext = c;
        ImageType = type;
        SetList(mFlowers,type);
    }
    @Override
    public int getCount() {
        switch (ImageType)
        {
            case "Flowers":
                return mFlowers.size();
            case "Fruits":
                return mFruits.size();
            case "Smileys":
                return mSmileys.size();
            default:
                return mFlowers.size();
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
                return mFlowers.get(position);
            case "Fruits":
                return mFruits.get(position);
            case "Smileys":
                return mSmileys.get(position);
            default:
                return mFlowers.get(position);
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
                imageView.setImageResource(mFlowers.get(position));
                break;
            case "Fruits":
                imageView.setImageResource(mFruits.get(position));
                break;
            case "Smileys":
                imageView.setImageResource(mSmileys.get(position));
                break;
            default:
                imageView.setImageResource(mFlowers.get(position));
                break;
        }

        return imageView;
    }

    public ArrayList<Integer> mFlowers = new ArrayList<Integer>();
    public ArrayList<Integer> mFruits = new ArrayList<Integer>();
    public ArrayList<Integer> mSmileys = new ArrayList<Integer>();
    public void SetList(ArrayList<Integer> list,String Type)
    {
        switch (Type) {
            case "Flowers":
                mFlowers.add(R.drawable.flower1);
                mFlowers.add(R.drawable.flower2);
                mFlowers.add(R.drawable.flower3);
                mFlowers.add(R.drawable.flower4);
                mFlowers.add(R.drawable.flower5);
                mFlowers.add(R.drawable.flower6);
                mFlowers.add(R.drawable.flower7);
                mFlowers.add(R.drawable.flower8);
                mFlowers.add(R.drawable.flower9);
                Collections.shuffle(mFlowers);
                break;
            case "Fruits":
                mFruits.add(R.drawable.fruit1);
                mFruits.add(R.drawable.fruit2);
                mFruits.add(R.drawable.fruit3);
                mFruits.add(R.drawable.fruit4);
                mFruits.add(R.drawable.fruit5);
                mFruits.add(R.drawable.fruit6);
                mFruits.add(R.drawable.fruit7);
                mFruits.add(R.drawable.fruit8);
                mFruits.add(R.drawable.fruit9);
                Collections.shuffle(mFruits);
                break;
            case "Smileys":
                mSmileys.add(R.drawable.smily1);
                mSmileys.add(R.drawable.smily2);
                mSmileys.add(R.drawable.smily3);
                mSmileys.add(R.drawable.smily4);
                mSmileys.add(R.drawable.smily5);
                mSmileys.add(R.drawable.smily6);
                mSmileys.add(R.drawable.smily7);
                mSmileys.add(R.drawable.smily8);
                mSmileys.add(R.drawable.smily9);
                Collections.shuffle(mSmileys);
                break;
            default:

                break;
        }
    }
}

