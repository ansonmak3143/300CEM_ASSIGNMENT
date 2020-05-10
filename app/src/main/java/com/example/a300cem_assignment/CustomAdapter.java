package com.example.a300cem_assignment;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import java.io.IOException;
import java.util.ArrayList;

public class CustomAdapter implements ListAdapter {
    private ArrayList<RecordObject> arrayList;
    private Context context;
    CustomAdapter(Context context, ArrayList<RecordObject> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final RecordObject subjectData = arrayList.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.vieworder_list_item, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFragment(ViewOrderDetailsFragment.newInstance(subjectData.getTitle(),subjectData.getDate(),subjectData.getDescription(),subjectData.getAudioFileName(),subjectData.getPhotoPath()));
                }
            });
            TextView tittle = convertView.findViewById(R.id.recordListTitleTxt);
            TextView date = convertView.findViewById(R.id.recordListDateTxt);
            ImageView imag = convertView.findViewById(R.id.recordListItemImage);
            tittle.setText(subjectData.getTitle());
            date.setText(subjectData.getDate());
            Bitmap mImageBitmap;
            try {
                if(subjectData.getPhotoPath().length() != 0){
                    mImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(subjectData.getPhotoPath()));
                    imag.setImageBitmap(mImageBitmap);
                }else{
                    imag.setImageResource(R.drawable.vieworder);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }
    @Override
    public boolean isEmpty() {
        return false;
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = MainActivity.getManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
