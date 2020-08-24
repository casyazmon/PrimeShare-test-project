package com.example.matchday.ui.file;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.example.matchday.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;

import java.io.File;

public class FileFragment extends Fragment {
    private static final int PDF_SELECTION_CODE = 99;
    private PDFView pdfView;
    private ProgressBar progressBar;

    private FileViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(FileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        pdfView = root.findViewById(R.id.pdfView);
        progressBar = root.findViewById(R.id.progressBar);
        //Intent intent = getIntent();
        //checkPDFAction(intent);
        selectPDFFromStorage();

        PRDownloader.initialize(getContext());
        return root;
    }

    private void showPDFFromAsset(String pdfName) {
        pdfView.fromAsset(pdfName)
                .password(null)
                .defaultPage(0)
                .load();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PDF_SELECTION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedPdfFromStorage = data.getData();
            showPdfFromUri(selectedPdfFromStorage);
        }
    }

    private void showPdfFromUri(Uri selectedPdfFromStorage) {
        pdfView.fromUri(selectedPdfFromStorage)
                .password(null)
                .defaultPage(0)
                .load();
    }

    private void selectPDFFromStorage() {
        Toast.makeText(getContext(), "Select PDF", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF_SELECTION_CODE);
    }

    // Using PRDDownloader to download files from the internet
    private void downloadPdfFromInternet(String url, final String dirPath, final String fileName){
        PRDownloader.download(url, dirPath, fileName)
                .build()
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Toast.makeText(getContext(), "Download Complete",
                                Toast.LENGTH_SHORT).show();
                        File downloadedFile = new File(dirPath, fileName);
                        progressBar.setVisibility(View.GONE);
                        showPDFFromFile(downloadedFile);
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(getContext(),
                                "Error in downloading file :"+ error,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showPDFFromFile(File file) {
        pdfView.fromFile(file)
                .password(null)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {
                        Toast.makeText(getContext(),
                                "Error on Page :" + page, Toast.LENGTH_SHORT).show();
                    }
                })
                .load();

    }
}