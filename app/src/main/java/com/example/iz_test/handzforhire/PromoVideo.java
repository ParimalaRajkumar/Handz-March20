package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;


public class PromoVideo extends Activity {

    VideoView video;
    public static final String API_KEY = "AIzaSyAUcI4AwDX_DF4dGnRN3ls1reqJcA_MtSE";

    public static final String VIDEO_ID1 = "nBERaM6P9rQ";
    public static final String VIDEO_ID2 = "V92fm3pEfQk";
    public static final String VIDEO_ID3 = "QVmYR4nUd0I";
    public static final String VIDEO_ID4 = "aGwbVMGJS7I";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promo_video);

        ImageView logo = (ImageView) findViewById(R.id.logo);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        YouTubePlayerView youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtube_player_view1);
        youTubePlayerView.initialize(
                new YouTubePlayerInitListener() {

                    @Override
                    public void onInitSuccess(
                            final YouTubePlayer initializedYouTubePlayer) {

                        initializedYouTubePlayer.addListener(
                                new AbstractYouTubePlayerListener() {
                                    @Override
                                    public void onReady() {
                                        initializedYouTubePlayer.cueVideo(VIDEO_ID1, 0);
                                    }
                                });
                    }
                }, true);
        YouTubePlayerView youTubePlayerView2 = (YouTubePlayerView)findViewById(R.id.youtube_player_view2);
        youTubePlayerView2.initialize(
                new YouTubePlayerInitListener() {

                    @Override
                    public void onInitSuccess(
                            final YouTubePlayer initializedYouTubePlayer) {

                        initializedYouTubePlayer.addListener(
                                new AbstractYouTubePlayerListener() {
                                    @Override
                                    public void onReady() {
                                        initializedYouTubePlayer.cueVideo(VIDEO_ID2, 0);
                                    }
                                });
                    }
                }, true);

        YouTubePlayerView youTubePlayerView3 = (YouTubePlayerView)findViewById(R.id.youtube_player_view3);
        youTubePlayerView3.initialize(
                new YouTubePlayerInitListener() {

                    @Override
                    public void onInitSuccess(
                            final YouTubePlayer initializedYouTubePlayer) {

                        initializedYouTubePlayer.addListener(
                                new AbstractYouTubePlayerListener() {
                                    @Override
                                    public void onReady() {
                                        initializedYouTubePlayer.cueVideo(VIDEO_ID3, 0);
                                    }
                                });
                    }
                }, true);
        YouTubePlayerView youTubePlayerView4 = (YouTubePlayerView)findViewById(R.id.youtube_player_view4);
        youTubePlayerView4.initialize(
                new YouTubePlayerInitListener() {

                    @Override
                    public void onInitSuccess(
                            final YouTubePlayer initializedYouTubePlayer) {

                        initializedYouTubePlayer.addListener(
                                new AbstractYouTubePlayerListener() {
                                    @Override
                                    public void onReady() {
                                        initializedYouTubePlayer.cueVideo(VIDEO_ID4, 0);
                                    }
                                });
                    }
                }, true);

    }

}
