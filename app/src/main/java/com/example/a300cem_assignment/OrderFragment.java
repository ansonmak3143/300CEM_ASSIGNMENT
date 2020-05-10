package com.example.a300cem_assignment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class OrderFragment extends Fragment implements View.OnClickListener{
    private String mCurrentPhotoPath = "";
    private ImageView recordImage;
    private static String audioFileName = "";
    private EditText recordTitleEtxt, recordDescriptionETxt;
    private Button recordAudioBtn;
    private static final int Image_Capture_Code = 1;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private MediaRecorder recorder = null;
    private boolean mStartRecording = true;
    private Time today = new Time(Time.getCurrentTimezone());
    private DBHelper DH = null;

    public OrderFragment() {
    }

    static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        closeDB();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recordImage = view.findViewById(R.id.recordImage);
        ImageButton recordCameraButton = view.findViewById(R.id.recordCameraButton);
        recordAudioBtn = view.findViewById(R.id.recordAudioBtn);
        recordTitleEtxt = view.findViewById(R.id.recordTitleEtxt);
        recordDescriptionETxt = view.findViewById(R.id.recordDescriptionETxt);
        Button orderSubmit = view.findViewById(R.id.orderSubmit);
        orderSubmit.setOnClickListener(this);
        recordAudioBtn.setOnClickListener(this);
        openDB();
        recordCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    cameraBtnClicked();
                }else {
                    cameraBtnClicked();
                }
            }
        });
    }

    private void openDB(){
        DH = new DBHelper(getContext());
    }
    private void closeDB(){
        DH.close();
    }

    private void cameraBtnClicked(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("cameraBtnClicked", "IOException");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent, Image_Capture_Code);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        today.setToNow();
        String imageFileName = "JPEG_" + today.year + today.month + today.monthDay + today.format("%k%M%S") + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(mCurrentPhotoPath));
                    recordImage.setImageBitmap(mImageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity(), getString(R.string.toastCancel), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orderSubmit:
                String recordTitleTmp, recordDescriptionTmp;
                recordTitleTmp = recordTitleEtxt.getText().toString().trim();
                recordDescriptionTmp = recordDescriptionETxt.getText().toString().trim();
                today.setToNow();
                String recordDate = today.year + "-" + today.month + "-" + today.monthDay;
                RecordObject ro;
                if (isEmptyString(recordTitleTmp)) {
                    Toast.makeText(getActivity(), getString(R.string.toastEmptyTitle), Toast.LENGTH_SHORT).show();
                }else if(isEmptyString(recordDescriptionTmp)){
                    Toast.makeText(getActivity(), getString(R.string.toastEmptyDescription), Toast.LENGTH_SHORT).show();
                }else{
                    ro = new RecordObject(recordTitleTmp,recordDescriptionTmp, recordDate,audioFileName,mCurrentPhotoPath);
                    if(DH.insertRecord(ro)){
                        Toast.makeText(getActivity(), getString(R.string.toastEmptyRecordAdded), Toast.LENGTH_SHORT).show();
                        openFragment(ViewOrderFragment.newInstance());
                    }else{
                        Log.e("Record add", "add record failed");
                    }
                }
                break;
            case R.id.recordAudioBtn:
                today.setToNow();
                audioFileName = getActivity().getExternalCacheDir().getAbsolutePath();
                audioFileName += "/" + today.year + today.month +today.monthDay + today.format("%k%M%S") + "audioRecord.3gp";
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
                    onRecord(mStartRecording);
                    if (mStartRecording) {
                        recordAudioBtn.setText(getString(R.string.stop_recording));
                    } else {
                        recordAudioBtn.setText(getString(R.string.start_recording));
                    }
                    mStartRecording = !mStartRecording;
                }else {
                    onRecord(mStartRecording);
                    if (mStartRecording) {
                        recordAudioBtn.setText(getString(R.string.stop_recording));
                    } else {
                        recordAudioBtn.setText(getString(R.string.start_recording));
                    }
                    mStartRecording = !mStartRecording;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(audioFileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("Record Audio", "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    private static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}