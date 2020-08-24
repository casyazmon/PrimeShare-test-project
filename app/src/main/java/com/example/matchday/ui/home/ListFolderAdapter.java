package com.example.matchday.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matchday.R;
import com.example.matchday.Utils.FileUtils;

import java.util.ArrayList;

public class ListFolderAdapter extends RecyclerView.Adapter<ListFolderAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> foldersAndFiles = FileUtils.getDirectories();

    public ListFolderAdapter( Context context1) {
        this.context = context;
    }

    public ListFolderAdapter(ArrayList<String>foldersAndFiles) {
        this.foldersAndFiles = foldersAndFiles;
    }

    @NonNull
    @Override
    public ListFolderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_folder_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListFolderAdapter.ViewHolder holder, int position) {
        final String myListData = foldersAndFiles.get(position);
        holder.folderName.setText(myListData);
    }

    @Override
    public int getItemCount() {
        return foldersAndFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView folderName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folderName);
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, folderName.toString(), Toast.LENGTH_LONG).show();
                    /*Intent intent = new Intent(context, UploadFileActivity.class);
                    context.startActivity(intent);*/
                }
            });
        }
    }
}
