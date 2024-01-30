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

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.R;
import com.example.myapplication.dao.CommentDao;
import com.example.myapplication.entitys.CommentEntity;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<CommentEntity> commentList;

    public CommentAdapter(List<CommentEntity> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentEntity CommentEntity = commentList.get(position);
        holder.bind(CommentEntity);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView categoryTextView;
        private TextView bodyTextView;

        private Context context;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewTitle);
            categoryTextView = itemView.findViewById(R.id.textViewCategory);
            bodyTextView = itemView.findViewById(R.id.textViewBody);

            ImageButton btnDeleteComment = itemView.findViewById(R.id.btnDeleteComment);
            btnDeleteComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        CommentEntity commentToDelete = commentList.get(position);

                        showDeleteConfirmationDialog(commentToDelete, position);
                    }
                }
            });


        }

        private void showDeleteConfirmationDialog(CommentEntity commentToDelete, int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder((Activity) itemView.getContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to delete this comment?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteComment(commentToDelete, position);
                }
            });


            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        }

        private void deleteComment(CommentEntity commentToDelete, int position) {
            context = itemView.getContext();
            AppDatabase appDatabase = AppDatabase.getInstance(context);
            CommentDao commentDao = appDatabase.commentDao();

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    commentDao.deleteComment(commentToDelete);
                }
            });
            if (commentList != null && !commentList.isEmpty() && position >= 0 && position < commentList.size()) {
                commentList.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void bind(CommentEntity comment) {
            titleTextView.setText(comment.getTitle());
            categoryTextView.setText(comment.getCategory());
            bodyTextView.setText(comment.getBody());
        }
    }
}