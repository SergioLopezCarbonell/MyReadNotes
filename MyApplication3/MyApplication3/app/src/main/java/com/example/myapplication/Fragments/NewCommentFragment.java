package com.example.myapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.R;
import com.example.myapplication.dao.CommentDao;
import com.example.myapplication.entitys.CommentEntity;

public class NewCommentFragment extends Fragment {
    private EditText editTitle;
    private Spinner spinner;
    private EditText editBody;

    private int personalBookId;

    public void setPersonalBookId(int personalBookId) {
        this.personalBookId = personalBookId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.newcomment, container, false);

        spinner = view.findViewById(R.id.spinner);
        editTitle = view.findViewById(R.id.editTitle);
        editBody = view.findViewById(R.id.editBody);
        Button saveButton = view.findViewById(R.id.saveButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComment();
            }
        });

        String[] categories = {"Emotional", "Romantic", "Inspirational","Mysterious",
                                "Humorous","Informative","Adventure","Magical","Philosophical","Others"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return view;
    }

    private void saveComment() {

        String title = editTitle.getText().toString();
        String category = spinner.getSelectedItem().toString();
        String body = editBody.getText().toString();

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setTitle(title);
        commentEntity.setCategory(category);
        commentEntity.setBody(body);
        commentEntity.setPersonalBookId(personalBookId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase appDatabase = AppDatabase.getInstance(requireContext());
                CommentDao commentDao = appDatabase.commentDao();
                commentDao.insertComment(commentEntity);

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                });
            }
        }).start();
    }


}
