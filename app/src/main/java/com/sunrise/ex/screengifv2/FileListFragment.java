package com.sunrise.ex.screengifv2;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class FileListFragment extends ListFragment {

    private ArrayList<File> mFiles;
    private String mPrevDir;
    private File files = new File(Environment.getExternalStorageDirectory().toString());
    private FileAdapter Fadapter;
    public static String IMG_PATH = "com.sunrise.ex.screengifv2.IMG_PATH";

    PrevBack myPrevBack;

    interface PrevBack {
        void updatePrev(String PrevDir);
        void updateCur(String CurrentDir);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            myPrevBack = (PrevBack) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement myPrevBack");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            files = new File(getArguments().getString("FILE_DIRECTORY", Environment.getExternalStorageDirectory().toString()));
        }

        FileStructure fs = new FileStructure();
        mFiles = fs.getFiles(files);
        OrganiseFiles();
        Fadapter = new FileAdapter(mFiles);
        setListAdapter(Fadapter);
    }

    static FileListFragment newInstance(String FilDir) {
        FileListFragment f = new FileListFragment();

        Bundle args = new Bundle();
        args.putString("FILE_DIRECTORY", FilDir);
        f.setArguments(args);
        return f;
    }

    private void OrganiseFiles() {

        FileStructure fs = new FileStructure();

        if (Fadapter != null) {
            Fadapter.clear();
        }

        if (mFiles != null) {
            mFiles.clear();
        }
        mFiles = fs.getFiles(files);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(files.toString());
        mPrevDir = getActivity().getTitle().toString();
        myPrevBack.updatePrev(mPrevDir);
        Fadapter.notifyDataSetChanged();

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        TextView StringView = (TextView) v.findViewById(R.id.file_path_txt);

        File iFile = new File(StringView.getText().toString());
        if (iFile.isFile()) {
            if(iFile.toString().endsWith(".gif")) {
                Intent i = new Intent();
                i.putExtra(IMG_PATH, StringView.getText().toString());
                getActivity().setResult(Activity.RESULT_OK, i);
                getActivity().finish();
            }else if(!iFile.toString().endsWith(".gif")){
                 Toast.makeText(getActivity().getApplicationContext(),"Not a .Gif file",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(iFile.isDirectory()){
            if(iFile.listFiles().length == 0){

                Toast.makeText(getActivity().getApplicationContext(),"Empty Folder",Toast.LENGTH_SHORT).show();

                return;
            }
        }

        files = new File(StringView.getText().toString());
        getActivity().setTitle(StringView.getText());
        myPrevBack.updateCur(StringView.getText().toString());
        mPrevDir = getActivity().getTitle().toString();
        myPrevBack.updatePrev(mPrevDir);

        myPrevBack.updateCur(files.toString());

        OrganiseFiles();

        Fadapter.clear();
        for (File inF : mFiles) {
            Fadapter.add(inF);
        }
    }

    private class FileAdapter extends ArrayAdapter<File> {

        public FileAdapter(ArrayList<File> cFiles) {
            super(getActivity(), 0, cFiles);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.file_list_item, null);
            }

            File f = getItem(position);
            TextView folderPath = (TextView) convertView.findViewById(R.id.file_path_txt);
            ImageView ItemImg = (ImageView) convertView.findViewById(R.id.folder_img);

            if (f.isFile()) {
                ItemImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_file));
            } else {
                ItemImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_folder));
            }
            folderPath.setText(f.toString());

            return convertView;
        }
    }
}