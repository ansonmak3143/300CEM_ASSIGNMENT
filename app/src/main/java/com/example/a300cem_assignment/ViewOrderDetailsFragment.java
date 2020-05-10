package com.example.a300cem_assignment;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;

public class ViewOrderDetailsFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "date";
    private static final String ARG_PARAM3 = "description";
    private static final String ARG_PARAM4 = "audioFileName";
    private static final String ARG_PARAM5 = "photoPath";
    private String title;
    private String date;
    private String description;
    private String audioFileName;
    private String photoPath;
    private Button recordPlayAudioBtn;
    private MediaPlayer  player = null;
    private boolean mStartPlaying = true;


    public ViewOrderDetailsFragment() {
    }

    static ViewOrderDetailsFragment newInstance(String param1, String param2,String param3, String param4,String param5) {
        ViewOrderDetailsFragment fragment = new ViewOrderDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            date = getArguments().getString(ARG_PARAM2);
            description = getArguments().getString(ARG_PARAM3);
            audioFileName = getArguments().getString(ARG_PARAM4);
            photoPath = getArguments().getString(ARG_PARAM5);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView recordViewDetailsImages = view.findViewById(R.id.recordViewDetailsImages);
        EditText viewRecordDescription = view.findViewById(R.id.viewRecordDescription);
        TextView viewRecordTitleTxt = view.findViewById(R.id.viewRecordTitleTxt);
        TextView viewRecordDateTxt = view.findViewById(R.id.viewRecordDateTxt);
        Button viewRecordCancelBtn = view.findViewById(R.id.viewRecordCancelBtn);
        recordPlayAudioBtn = view.findViewById(R.id.recordPlayAudioBtn);
        viewRecordCancelBtn.setOnClickListener(this);
        viewRecordTitleTxt.setText(title);
        viewRecordDateTxt.setText(date);
        viewRecordDescription.setText(description);
        if(audioFileName.length() != 0){
            recordPlayAudioBtn.setOnClickListener(this);
            recordPlayAudioBtn.setEnabled(true);
        }
        if(photoPath.length() != 0){
            try {
                Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(photoPath));
                recordViewDetailsImages.setImageBitmap(mImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            recordViewDetailsImages.setImageResource(R.drawable.order);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewRecordCancelBtn:
                openFragment(ViewOrderFragment.newInstance());
                break;
            case R.id.recordPlayAudioBtn:
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    recordPlayAudioBtn.setText(getString(R.string.stop_playing));
                } else {
                    recordPlayAudioBtn.setText(getString(R.string.start_playing));
                }
                mStartPlaying = !mStartPlaying;
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vieworderdetails, container, false);
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(audioFileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e("playing audio", "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }
}
