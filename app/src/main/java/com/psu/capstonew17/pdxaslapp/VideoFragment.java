package com.psu.capstonew17.pdxaslapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;

import com.psu.capstonew17.backend.api.Video;


public class VideoFragment extends Fragment {

    private CustomVideoView videoView;
    private MediaController mediaController;
    private MediaPlayer mediaPlayer;

    /**
     * This event is called first, before creation of fragment view
     * This fragment is called when a fragment instance associated with
     * an activity. Activity not fully initialized
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * Called to to do initial setup of fragment that does not require the activity
     * fully created
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_view, container, false);
        videoView = (CustomVideoView) view.findViewById(R.id.videoView);
        return view;
    }

    /**
     * Called after view created, any view set up go here (eg. listenter, ..)
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Called immediately prior to the fragment no longer being associated with its activity
     * Any reference in onAttach should set to null.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        this.videoView = null;
        this.mediaPlayer = null;
        this.mediaController = null;
    }


    /**
     * Called when the fragment's activity has been created and this fragment's view
     * hierarchy instantiated.
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mediaPlayer = new MediaPlayer();
        mediaController = new MediaController(getContext());
    }

    public void setDisplayVideo(Video video) {
        video.configurePlayer(mediaPlayer);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                videoView.setMediaController(mediaController);
                mp.start();
            }
        });
    }
}
