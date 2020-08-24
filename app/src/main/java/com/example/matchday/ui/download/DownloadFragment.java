package com.example.matchday.ui.download;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.example.matchday.R;
import com.example.matchday.Utils.FileUtils;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;

import java.io.File;


public class DownloadFragment extends Fragment {


    private PDFView pdfView;
    private ProgressBar progressBar;
    private Button mDownloadBtn;
    private EditText mDownloadUrl;


    public DownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_download, container, false);

        mDownloadBtn = root.findViewById(R.id.downloadBtn);
        mDownloadUrl = root.findViewById(R.id.file_url);
        progressBar = root.findViewById(R.id.progress);
        pdfView = root.findViewById(R.id.pdfView);


        PRDownloader.initialize(getContext());

        mDownloadUrl.setText(FileUtils.getPDF());
        mDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                downloadPdfFromInternet(
                        FileUtils.getPDF(),
                        FileUtils.getRootDirPath(getContext()),
                        "newfile.pdf"

                );
            }
        });
        return root;
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
                        mDownloadBtn.setVisibility(View.INVISIBLE);
                        mDownloadUrl.setVisibility(View.INVISIBLE);
                        showPDFFromFile(downloadedFile);
                    }

                    @Override
                    public void onError(Error error) {
                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(getContext(),
                                "Error in downloading file :"+ error,
                                Toast.LENGTH_LONG).show();
                        Log.d("Download Error", "onError: "+error);

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