package com.example.matchday.ui.upload;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.matchday.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadFragment extends Fragment {

    private static final int PDF_SELECTION_CODE = 9901;
    private UploadViewModel dashboardViewModel;

    private Button mUploadBtn;
    private TextView mFileName;
    private ImageView mSelectFileBtn;
    private StorageReference mStorageRef;
    private ProgressBar progressBar;
    private Uri file;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(UploadViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        progressBar = root.findViewById(R.id.progrss);

        mUploadBtn = root.findViewById(R.id.uploadBtn);
        mSelectFileBtn = root.findViewById(R.id.mSelectFileBtn);
        mFileName = root.findViewById(R.id.file_name);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadFileToFirebase();
            }
        });
        mSelectFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectPDFFromStorage();
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PDF_SELECTION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            file = data.getData();
            mFileName.setText(data.getDataString());

        }
    }



    private void selectPDFFromStorage() {
        Toast.makeText(getContext(), "Select File to Upload", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select File to Upload"), PDF_SELECTION_CODE);
    }

    public void uploadFileToFirebase(){
        progressBar.setVisibility(View.VISIBLE);
        StorageReference riversRef = mStorageRef.child(file.getLastPathSegment());

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Upload Successful", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads

                        Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}