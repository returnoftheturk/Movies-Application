package com.example.akgul.movies_application;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent!=null){
            if (intent.hasExtra("original_title")){
            String movieTitle = intent.getStringExtra("original_title");
            TextView textView = (TextView) rootView.findViewById(R.id.detailfragment_text_movie_title);
            textView.setText(movieTitle);
        }
            if (intent.hasExtra("overview")){
                String overview = intent.getStringExtra("overview");
                TextView textView1 = (TextView) rootView.findViewById((R.id.detailfragment_text_plot_synopsis));
                textView1.setText(overview);

            }
            if (intent.hasExtra("release_date")){
                String releaseDate = intent.getStringExtra("release_date");
                TextView textView2 = (TextView) rootView.findViewById(R.id.detailfragment_text_movie_release_date);
                textView2.setText(releaseDate);
            }
            if (intent.hasExtra("vote_average")){
                String voteAverage = intent.getStringExtra("vote_average");
                TextView textView3 = (TextView) rootView.findViewById(R.id.detailfragment_text_vote_average);
                textView3.setText(voteAverage);
            }
            if (intent.hasExtra("poster_path")){
                String posterPath = intent.getStringExtra("poster_path");
                ImageView imageView = (ImageView) rootView.findViewById(R.id.detailfragment_imageView);
                Picasso.with(getActivity()).load(posterPath).into(imageView);
            }
        }

        return rootView;
    }
}
