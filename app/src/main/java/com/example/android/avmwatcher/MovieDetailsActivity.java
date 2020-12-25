package com.example.android.avmwatcher;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.avmwatcher.Adapter.MovieAdapter;
import com.example.android.avmwatcher.Model.GetVideoDetails;
import com.example.android.avmwatcher.Model.MoviesItemClickListenerNew;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements MoviesItemClickListenerNew {
    private ImageView MovieThumbnail,MovieCoverImg;
    TextView tv_title,tv_description;
    FloatingActionButton play_fab;
    RecyclerView RvCast,rv_similarMovies;
    MovieAdapter movieAdapter;
    DatabaseReference mDatabaseReference;
    List<GetVideoDetails> uploads,actionMovies,sportMovies,comedyMovies,romanticMovies,adventureMovies;
    String current_video_url;
    String current_video_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        tv_title = findViewById(R.id.detail_movies_title);
        play_fab = findViewById(R.id.play_fab);
        tv_description = findViewById(R.id.detail_movies_desc);
        MovieThumbnail = findViewById(R.id.detail_movies_img);
        MovieCoverImg = findViewById(R.id.detail_movies);
        rv_similarMovies = findViewById(R.id.recycler_similar);
        String moviesTitle = getIntent().getExtras().getString("title");
        String imgRecoresId = getIntent().getExtras().getString("imgUrl");
        String imgCover = getIntent().getExtras().getString("imgCover");
        String moviesDetailsText = getIntent().getExtras().getString("movieDetails");
        String moviesUrl = getIntent().getExtras().getString("movieUrl");
        String moviesCategory = getIntent().getExtras().getString("movieCategory");
        current_video_url = moviesUrl;
        current_video_category = moviesCategory;
        Glide.with(this).load(imgCover).into(MovieThumbnail);
        Glide.with(this).load(imgCover).into(MovieCoverImg);
        similarMoviesRecycler();
        similarMovies();
        tv_title.setText(moviesTitle);
        tv_description.setText(moviesDetailsText);
        getSupportActionBar().setTitle(moviesTitle);



        play_fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetailsActivity.this,MoviePlayerActivity.class);
                intent.putExtra("videoUri",current_video_url);
                startActivity(intent);
            }
        });
    }

    private void similarMovies() {
        if(current_video_category.equals("Action")){
            movieAdapter = new MovieAdapter(this,actionMovies,this);
            rv_similarMovies.setAdapter(movieAdapter);
            rv_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            movieAdapter.notifyDataSetChanged();
        }

        if(current_video_category.equals("Adventure")){
            movieAdapter = new MovieAdapter(this,adventureMovies,this);
            rv_similarMovies.setAdapter(movieAdapter);
            rv_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            movieAdapter.notifyDataSetChanged();
        }

        if(current_video_category.equals("Comedy")){
            movieAdapter = new MovieAdapter(this,comedyMovies,this);
            rv_similarMovies.setAdapter(movieAdapter);
            rv_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            movieAdapter.notifyDataSetChanged();
        }
        if(current_video_category.equals("Romantic")){
            movieAdapter = new MovieAdapter(this,romanticMovies,this);
            rv_similarMovies.setAdapter(movieAdapter);
            rv_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            movieAdapter.notifyDataSetChanged();
        }

        if(current_video_category.equals("Sport")){
            movieAdapter = new MovieAdapter(this,sportMovies,this);
            rv_similarMovies.setAdapter(movieAdapter);
            rv_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            movieAdapter.notifyDataSetChanged();
        }

    }

    private void similarMoviesRecycler() {
        uploads = new ArrayList<>();
        sportMovies = new ArrayList<>();
        comedyMovies = new ArrayList<>();
        romanticMovies = new ArrayList<>();
        adventureMovies = new ArrayList<>();
        actionMovies = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("videos");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    GetVideoDetails upload = postSnapshot.getValue(GetVideoDetails.class);
                    if(upload.getVideo_category().equals("Action")){
                        actionMovies.add(upload);
                    }
                    if(upload.getVideo_category().equals("Adventure")){
                        adventureMovies.add(upload);
                    }
                    if(upload.getVideo_category().equals("Comedy")){
                        comedyMovies.add(upload);
                    }
                    if(upload.getVideo_category().equals("Romantic")){
                        romanticMovies.add(upload);
                    }
                    if(upload.getVideo_category().equals("Sports")){
                        sportMovies.add(upload);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMovieClick(GetVideoDetails movie, ImageView imageView) {
        tv_title.setText(movie.getVideo_name());
        getSupportActionBar().setTitle(movie.getVideo_name());
        Glide.with(this).load(movie.getVideo_thumb()).into(MovieThumbnail);
        Glide.with(this).load(movie.getVideo_thumb()).into(MovieCoverImg);
        tv_description.setText(movie.getVideo_description());
        current_video_url = movie.getVideo_url();
        current_video_category = movie.getVideo_category();
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MovieDetailsActivity.this,
                imageView,"sharedName");
        options.toBundle();
    }
}
