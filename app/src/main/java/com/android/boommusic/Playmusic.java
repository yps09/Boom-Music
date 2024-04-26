package com.android.boommusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.boommusic.databinding.ActivityMainBinding;
import com.android.boommusic.databinding.ActivityPlaymusicBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Playmusic extends AppCompatActivity {

    ActivityPlaymusicBinding binding;

    TextView songHeading,currentTime,totalTime;
    SeekBar seekBar;
    private ObjectAnimator rotationAnimator;
    ImageView backButton,pauseButton,forwardButton;

    private  boolean isPlaying = true;
    ArrayList<AudioModel>songsList;

    CardView circleCardView;
    AudioModel currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    int x= 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlaymusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        songHeading = findViewById(R.id.songHeading);
        currentTime = findViewById(R.id.currentTimeTxt);
        totalTime = findViewById(R.id.totalTimeTxt);
        forwardButton = findViewById(R.id.forwardButton);
        backButton = findViewById(R.id.backButton);
        pauseButton =findViewById(R.id.pauseButton);
        seekBar = findViewById(R.id.seekBar);
        circleCardView = findViewById(R.id.circleCardView);

        songHeading.setSelected(true);

        songsList = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");
        setResourcesWithMusic();

        Playmusic.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null){

                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTime.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));
                    if (mediaPlayer.isPlaying()){
                        pauseButton.setImageResource(R.drawable.playbutton);
                        circleCardView.setRotation(x++);
                    }else {
                        pauseButton.setImageResource(R.drawable.pausebutton);
                        circleCardView.setRotation(0);
                    }
                }
                new Handler().postDelayed(this,100);
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });

    }

    private void createRotationAnimation() {
        rotationAnimator = ObjectAnimator.ofFloat(circleCardView, "rotation", 0f, 360f);
        rotationAnimator.setDuration(3000); // Set the duration of the rotation in milliseconds (adjust as needed)
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE); // Set to repeat indefinitely
        rotationAnimator.setInterpolator(new LinearInterpolator());
    }

    private void startRotationCard() {
        if (rotationAnimator == null) {
            createRotationAnimation();
        }
        rotationAnimator.start();
    }

    private void pauseRotationCard() {
        if (rotationAnimator != null && rotationAnimator.isRunning()) {
            rotationAnimator.pause();
        }
    }

    private void resumeRotationCard() {
        if (rotationAnimator != null && rotationAnimator.isPaused()) {
            rotationAnimator.resume();
        }
    }

    void setResourcesWithMusic(){
        currentSong = songsList.get(MyMediaPlayer.currentIndex);
        songHeading.setText(currentSong.getTitle());

        totalTime.setText(convertToMMSS(currentSong.getDuration()));

        pauseButton.setOnClickListener(v -> pausePlay());
        forwardButton.setOnClickListener(v -> playNextSong());
        backButton.setOnClickListener(v -> playPreviousSong());

        playMusic();

    }

    private  void  playMusic(){

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            startRotationCard();
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private  void  playNextSong(){

        if (MyMediaPlayer.currentIndex == songsList.size()-1)
            return;
        MyMediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
        startRotationCard();

    }
    private  void  playPreviousSong(){

        if (MyMediaPlayer.currentIndex ==0)
            return;
        MyMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        startRotationCard();
        setResourcesWithMusic();

    }
    private  void   pausePlay(){

        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            pauseRotationCard();
        }
        else{
            mediaPlayer.start();
            resumeRotationCard();
        }
    }

    public  static String convertToMMSS(String duration){

        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));

    }
}