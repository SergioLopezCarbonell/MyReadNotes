package com.example.myapplication.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.dao.CommentDao;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.R;
import com.example.myapplication.dao.MomentDao;
import com.example.myapplication.entitys.CommentEntity;
import com.example.myapplication.entitys.MomentEntity;

import java.util.List;

public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.MomentViewHolder> {

    private List<MomentEntity> momentList;

    public MomentAdapter(List<MomentEntity> momentList) {
        this.momentList = momentList;
    }

    @NonNull
    @Override
    public MomentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.moment_item, parent, false);
        return new MomentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MomentViewHolder holder, int position) {
        MomentEntity momentEntity = momentList.get(position);
        holder.bind(momentEntity);
    }

    @Override
    public int getItemCount() {
        return momentList.size();
    }

    public class MomentViewHolder extends RecyclerView.ViewHolder {

        private TextView chapterTextView;
        private TextView pageTextView;
        private TextView categoryTextView;
        private TextView bodyTextView;

        private Context context;

        public MomentViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            chapterTextView = itemView.findViewById(R.id.textViewChapter);
            pageTextView = itemView.findViewById(R.id.textViewPage);
            categoryTextView = itemView.findViewById(R.id.textViewCategory);
            bodyTextView = itemView.findViewById(R.id.textViewBody);

            ImageButton btnDeleteMoment = itemView.findViewById(R.id.btnDeleteComment);
            btnDeleteMoment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        MomentEntity momentToDelete = momentList.get(position);

                        showDeleteConfirmationDialog(momentToDelete,position);
                    }
                }
            });
        }

        private void showDeleteConfirmationDialog(MomentEntity momentToDelete, int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder((Activity) itemView.getContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to delete this moment?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteComment(momentToDelete, position);
                }
            });


            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        }

        private void deleteComment(MomentEntity momentToDelete, int position) {
            context = itemView.getContext();
            AppDatabase appDatabase = AppDatabase.getInstance(context);
            MomentDao momentDao = appDatabase.momentDao();

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    momentDao.deleteMoment(momentToDelete);
                }
            });
            if (momentList != null && !momentList.isEmpty() && position >= 0 && position < momentList.size()) {
                momentList.remove(position);
                notifyItemRemoved(position);
            }
        }


        public void bind(MomentEntity momentEntity) {
            chapterTextView.setText(momentEntity.getChapter());
            pageTextView.setText(momentEntity.getPage());
            bodyTextView.setText(momentEntity.getBody());
        }
    }
}
