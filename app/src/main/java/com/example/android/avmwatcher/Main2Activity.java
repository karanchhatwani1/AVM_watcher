package com.example.android.avmwatcher;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.android.avmwatcher.Adapter.MovieAdapter;
import com.example.android.avmwatcher.Adapter.SlidePagerAdapter;
import com.example.android.avmwatcher.Model.GetVideoDetails;
import com.example.android.avmwatcher.Model.MoviesItemClickListenerNew;
import com.example.android.avmwatcher.Model.SlideSide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity implements MoviesItemClickListenerNew {
    MovieAdapter movieAdapter;
    DatabaseReference mDatabaseReference;
    private List<GetVideoDetails> uploads, uploadsListLatest,uploadsListPopular;
    private List<GetVideoDetails> actionMovies,sportMovies,comedyMovies,romanticMovies,adventureMovies;
    private ViewPager sliderPager;
    private List<SlideSide> uploadsSlider;
    private TabLayout indicator,tabMoviesAction;
    private RecyclerView MoviesAvm,MoviesAvmWeek,tab;
    ProgressDialog progressDialog;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tabMoviesAction = findViewById(R.id.tabActionMovies);
        sliderPager = findViewById(R.id.slider_pager);
        indicator = findViewById(R.id.indicator);
        MoviesAvmWeek = findViewById(R.id.Avm_movies_week);
        MoviesAvm = findViewById(R.id.Avm_movies);
        tab = findViewById(R.id.tab_recycler);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        progressDialog = new ProgressDialog(this);

        addAllMovies();
        iniPopularMovies();
        iniWeekMovies();
        moviesViewTab();

    }

    private void addAllMovies(){
        uploads = new ArrayList<>();
        uploadsListLatest = new ArrayList<>();
        uploadsListPopular = new ArrayList<>();
        actionMovies = new ArrayList<>();
        adventureMovies = new ArrayList<>();
        comedyMovies = new ArrayList<>();
        sportMovies = new ArrayList<>();
        romanticMovies = new ArrayList<>();
        uploadsSlider = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("videos");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    GetVideoDetails upload = postSnapshot.getValue(GetVideoDetails.class);
                    SlideSide slide = postSnapshot.getValue(SlideSide.class);
                    if(upload.getVideo_type().equals("Latest Movies")){
                        uploadsListLatest.add(upload);
                    }
                    if(upload.getVideo_type().equals("Popular Movies")){
                        uploadsListPopular.add(upload);
                    }
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
                    if(slide.getVideo_slide().equals("Slide Movies")){
                        uploadsSlider.add(slide);
                    }
                    uploads.add(upload);

                }
                iniSlider();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void moviesViewTab(){

        getActionMovies();
        tabMoviesAction.addTab(tabMoviesAction.newTab().setText("Action"));
        tabMoviesAction.addTab(tabMoviesAction.newTab().setText("Adventure"));
        tabMoviesAction.addTab(tabMoviesAction.newTab().setText("Sport"));
        tabMoviesAction.addTab(tabMoviesAction.newTab().setText("Comedy"));
        tabMoviesAction.addTab(tabMoviesAction.newTab().setText("Romantic"));
        tabMoviesAction.setTabGravity(TabLayout.GRAVITY_FILL);
        tabMoviesAction.setTabTextColors(ColorStateList.valueOf(Color.WHITE));

        tabMoviesAction.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        getActionMovies();
                        break;
                    case 1:
                        getAdventureMovie();
                        break;
                    case 2:
                        getComedyMovies();
                        break;
                    case 3:
                        getRomanticMovies();
                        break;
                    case 4:
                        getSportMovies();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                getActionMovies();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void iniSlider() {
        SlidePagerAdapter slidePagerAdapter = new SlidePagerAdapter(this,uploadsSlider);
        sliderPager.setAdapter(slidePagerAdapter);
        slidePagerAdapter.notifyDataSetChanged();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(),4000,6000);
        indicator.setupWithViewPager(sliderPager,true);
    }
    private void iniWeekMovies(){
        movieAdapter = new MovieAdapter(this,uploadsListLatest,this);
        MoviesAvmWeek.setAdapter(movieAdapter);
        MoviesAvmWeek.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        movieAdapter.notifyDataSetChanged();
    }

    private void iniPopularMovies(){
        movieAdapter = new MovieAdapter(this,uploadsListPopular,this);
        MoviesAvm.setAdapter(movieAdapter);
        MoviesAvm.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        movieAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMovieClick(GetVideoDetails movie, ImageView imageView) {
        Intent in = new Intent(this,MovieDetailsActivity.class);
        in.putExtra("title",movie.getVideo_name());
        in.putExtra("imgURL",movie.getVideo_thumb());
        in.putExtra("imgCover",movie.getVideo_thumb());
        in.putExtra("movieDetails",movie.getVideo_description());
        in.putExtra("movieUrl",movie.getVideo_url());
        in.putExtra("movieCategory",movie.getVideo_category());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Main2Activity.this,imageView,"sharedName");
        startActivity(in);

    }

    public class SliderTimer extends TimerTask {
        public void run(){
            Main2Activity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(sliderPager.getCurrentItem()<uploadsSlider.size()-1){
                        sliderPager.setCurrentItem(sliderPager.getCurrentItem()+1);
                    }else{
                        sliderPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
    private void getActionMovies(){
        movieAdapter = new MovieAdapter(this,actionMovies,this);
        tab.setAdapter(movieAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        movieAdapter.notifyDataSetChanged();
    }

    private void getSportMovies(){
        movieAdapter = new MovieAdapter(this,sportMovies,this);
        tab.setAdapter(movieAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        movieAdapter.notifyDataSetChanged();
    }

    private void getRomanticMovies(){
        movieAdapter = new MovieAdapter(this,uploadsListLatest,this);
        tab.setAdapter(movieAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        movieAdapter.notifyDataSetChanged();
    }

    private void getComedyMovies(){
        movieAdapter = new MovieAdapter(this,comedyMovies,this);
        tab.setAdapter(movieAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        movieAdapter.notifyDataSetChanged();
    }

    private void getAdventureMovie(){
        movieAdapter = new MovieAdapter(this,adventureMovies,this);
        tab.setAdapter(movieAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        movieAdapter.notifyDataSetChanged();
    }
}
