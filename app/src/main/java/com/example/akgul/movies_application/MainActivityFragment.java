package com.example.akgul.movies_application;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public String[] resultStr;

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


        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_poster);
        gridView.setAdapter(new ImageAdapter(getActivity()));

        new FetchMovieTask().execute();

        return rootView;
    }

    public class ImageAdapter extends BaseAdapter {
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
            //Picasso.with(context).load(resultStr[position].toString()).into(imageView);
            //Picasso.with(context).load(resultStr[position].toString()).into(imageView);
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

    public class FetchMovieTask extends AsyncTask<Void, Void, Void>{
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();



        private String[] getDataFromJson(String moviesJsonStr) throws JSONException{
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray("results");

            int numMovies = moviesArray.length();
            resultStr = new String[numMovies];
            String posterPath;
            final String BASE_URL = "http://image.tmdb.org/t/p/w185";

            for (int i = 0; i <numMovies ; i++) {

                JSONObject movie = moviesArray.getJSONObject(i);
                posterPath = movie.getString("poster_path");
                resultStr[i] = BASE_URL + posterPath;

            }
            Log.v(LOG_TAG, "RESULT STR: " + resultStr);
            return resultStr;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;
            try{
                URL url = new URL("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=891863ba3b17302582171ead3487b06c");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream==null)
                    return null;
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                if (buffer.length()==0){
                    return null;
                }
                moviesJsonStr = buffer.toString();
                Log.v(LOG_TAG, moviesJsonStr);

            }
            catch (IOException e){
                Log.v(LOG_TAG, "Error: ", e);
                return null;
            }
            finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if (reader!=null){
                    try{
                        reader.close();
                    }
                    catch(final IOException e){
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }

            }
            try {
                getDataFromJson(moviesJsonStr);
            }
            catch (JSONException e){
                Log.v(LOG_TAG, "Error: " + e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
