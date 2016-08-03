package com.example.akgul.movies_application;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        View rootView = inflater.inflate(R. layout.fragment_main, container, false);

//        Integer[] mThumbIds = { R.drawable.sample_0, R.drawable.sample_7,
//                R.drawable.sample_1, R.drawable.sample_4,
//                R.drawable.sample_2, R.drawable.sample_6,
//                R.drawable.sample_3, R.drawable.sample_1,
//                R.drawable.sample_4, R.drawable.sample_0,
//                R.drawable.sample_5, R.drawable.sample_3,
//                R.drawable.sample_6, R.drawable.sample_5,
//                R.drawable.sample_7, R.drawable.sample_2,
//        };
//        ArrayAdapter<Integer> myAdapter = new ArrayAdapter<>(getActivity(), R.layout.movie_item_poster,
//                R.id.poster_item_imageview, mThumbIds);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_poster);
        gridView.setAdapter(new ImageAdapter(getActivity()));

        return rootView;
    }

    public class ImageAdapter extends BaseAdapter{
        private Context mContext;

        public ImageAdapter(Context c){
            mContext = c;
        }

        public int getCount(){
            return mThumbIds.length;
        }

        public Object getItem(int position){
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            ImageView imageView;
            if (convertView == null){
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(300,300));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(1,1,1,1);
            }
            else {
                imageView = (ImageView)convertView;
            }
            imageView.setImageResource(mThumbIds[position]);
            return imageView;

        }

        public long getItemId(int position){
            return position;
        }

        private Integer[] mThumbIds = {
            R.drawable.sample_0, R.drawable.sample_7,
            R.drawable.sample_1, R.drawable.sample_4,
            R.drawable.sample_2, R.drawable.sample_6,
            R.drawable.sample_3, R.drawable.sample_1,
            R.drawable.sample_4, R.drawable.sample_0,
            R.drawable.sample_5, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_5,
            R.drawable.sample_7, R.drawable.sample_2,
        };


    }
}
