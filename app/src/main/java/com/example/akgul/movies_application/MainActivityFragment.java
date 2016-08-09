package com.example.akgul.movies_application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public ArrayList<String> resultStr = new ArrayList<>();
    public ImageAdapter mImageAdapter;
    final String ORIGINAL_TITLE = "original_title";
    final String PLOT_SYNOPSIS = "overview";
    final String RELEASE_DATE = "release_date";
    final String USER_RATING = "vote_average";
    final String POSTER_PATH = "poster_path";

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String LOG_TAG = "onCreateView";

        View rootView = inflater.inflate(R. layout.fragment_main, container, false);


        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_poster);

        mImageAdapter = new ImageAdapter(getActivity());
        gridView.setAdapter(mImageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(getActivity(), "" + i, Toast.LENGTH_SHORT).show();
                String[] movieData = mImageAdapter.getData(i);
                Intent intent = new Intent(getActivity(),DetailActivity.class );
                intent.putExtra(ORIGINAL_TITLE, movieData[0])
                        .putExtra(PLOT_SYNOPSIS, movieData[1])
                        .putExtra(RELEASE_DATE, movieData[2])
                        .putExtra(USER_RATING, movieData[3])
                        .putExtra(POSTER_PATH, resultStr.get(i));
                startActivity(intent);
                Toast.makeText(getActivity(), "" + movieData[0], Toast.LENGTH_SHORT).show();



            }
        });
//        String[] testing = new String[5];
//        testing = mImageAdapter.getData(0);
//        Log.v("Show if works", "Hello:  " + testing);


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);

    }

    private void updatePosters(){
        FetchMovieTask fetchMovieTask = new FetchMovieTask();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = null;
        sortType = sharedPreferences.getString("order", "popularity.desc");
        fetchMovieTask.execute(sortType);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh){
            updatePosters();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        updatePosters();
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<String> myData = new ArrayList<>();
        private String[] returnData = new String[5];

        public ImageAdapter(Context c){
            mContext = c;
        }

        public void populateData(String movieData){
//            for (int i = 0; i <movieData.length ; i++) {
//                myData.add(i, movieData[i]);
//            }
                myData.add(movieData);
        }

        public String[] getData(int position){
            for (int i = 0; i<5; i++){
                returnData[i] = myData.get(position*5+i).toString();

            }
            return returnData;
        }

        public int getCount(){
            int numberElements = resultStr.size();
            return numberElements;
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
                //imageView.setImageResource(mThumbIds[position]);
                //Picasso.with(getActivity()).load(resultStr[position]).into(imageView);
            }
            else {
                imageView = (ImageView)convertView;
            }
            Picasso.with(getActivity()).load(resultStr.get(position).toString()).into(imageView);


            return imageView;

        }

        public long getItemId(int position){
            return position;
        }

    }

    public class FetchMovieTask extends AsyncTask<String, Void, String[]>{
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        private String[] getDataFromJson(String moviesJsonStr) throws JSONException{
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray("results");

            int numMovies = moviesArray.length();
            String[] newResults = new String[numMovies];
            //resultStr = new String[numMovies];
            String posterPath;

            final String BASE_URL = "http://image.tmdb.org/t/p/w185";

            for (int i = 0; i <numMovies ; i++) {

                JSONObject movie = moviesArray.getJSONObject(i);
                mImageAdapter.populateData(movie.getString(ORIGINAL_TITLE));
                mImageAdapter.populateData(movie.getString(PLOT_SYNOPSIS));
                mImageAdapter.populateData(movie.getString(RELEASE_DATE));
                mImageAdapter.populateData(movie.getString(USER_RATING));
                mImageAdapter.populateData(movie.getString(POSTER_PATH));

                posterPath = movie.getString("poster_path");
                //resultStr.add(i, BASE_URL + posterPath);
                newResults[i]= BASE_URL + posterPath;
            }
            Log.v(LOG_TAG, "RESULT STR: " + resultStr);
            String[] testing = new String[5];
            testing = mImageAdapter.getData(6);
            Log.v(LOG_TAG, testing[0].toString());
            Log.v(LOG_TAG, testing[1].toString());
            Log.v(LOG_TAG, testing[2].toString());
            Log.v(LOG_TAG, testing[3].toString());
            Log.v(LOG_TAG, testing[4].toString());
            return newResults;

        }

        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;
            try{
                final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
                final String QUERY_PARAM = "sort_by";
                final String APPIP_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(APPIP_PARAM, "891863ba3b17302582171ead3487b06c")
                        .build();

                URL url = new URL (builtUri.toString());

                //URL url = new URL("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=891863ba3b17302582171ead3487b06c");
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
                Log.v(LOG_TAG, params[0]);

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
                return getDataFromJson(moviesJsonStr);
            }
            catch (JSONException e){
                Log.v(LOG_TAG, "Error: " + e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            //super.onPostExecute(strings);
            if (strings!=null){
                resultStr.clear();
                for (int i = 0; i<strings.length; i++){
                    resultStr.add(i, strings[i]);

                }
                mImageAdapter.notifyDataSetChanged();
            }
        }
    }
}
