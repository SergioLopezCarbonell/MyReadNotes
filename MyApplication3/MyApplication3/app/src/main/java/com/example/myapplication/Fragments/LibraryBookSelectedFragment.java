package com.example.myapplication.Fragments;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.R;
import com.example.myapplication.classes.Book;
import com.example.myapplication.dao.PersonalBookDao;
import com.example.myapplication.entitys.PersonalBookEntity;
import com.squareup.picasso.Picasso;

public class LibraryBookSelectedFragment extends Fragment {

    private Book selectedBook;

    public LibraryBookSelectedFragment(Book book) {
        this.selectedBook = book;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_library_selectedbook, container, false);


        String title = selectedBook.getTitle();
        String author = selectedBook.getAuthor();
        String coverResource = selectedBook.getCoverResource();
        String description = selectedBook.getDescription();

        ImageView imageView = view.findViewById(R.id.BookImage);

        if (isValidImageUrl(coverResource)) {
            Picasso.get().load(coverResource).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.image_not_available);
        }

        String imageUrl = coverResource.replace("http://", "https://");


        if (isValidImageUrl(imageUrl)) {
            Picasso.get().load(imageUrl).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.image_not_available);
        }


        TextView textViewTitle = view.findViewById(R.id.TitleTextView);
        textViewTitle.setText(title);

        TextView textViewAuthor = view.findViewById(R.id.AuthorTextView);
        textViewAuthor.setText(author);

        TextView textViewDescription = view.findViewById(R.id.DescriptionText);
        textViewDescription.setText(description);

        Button buttonAdd = view.findViewById(R.id.buttonAdd);

        ImageButton buttonBack = view.findViewById(R.id.btnBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        PersonalBookDao personalBookDao = AppDatabase.getInstance(requireContext()).personalBookDao();

                        int count = personalBookDao.getBookCountByTitleAndAuthor(selectedBook.getTitle(),
                                selectedBook.getAuthor());
                        if (count > 0) {
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showBookAlreadyAddedDialog();
                                }
                            });
                        } else {
                            showCategorySelectionDialog();
                        }
                    }
                });
            }
        });

        return view;
    }
    private void showBookAlreadyAddedDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Caution")
                .setMessage("Book already in your Personal Books")
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void showCategorySelectionDialog() {
        String[] categories = {"Fiction", "Non-Fiction","Literary genre", "Adventures", "Mistery", "Romance", "Terror","Others"};

        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Select Category")
                        .setSingleChoiceItems(categories, 0, null)
                        .setPositiveButton("Add", (dialog, which) -> {
                            int selectedCategoryIndex = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                            String selectedCategory = categories[selectedCategoryIndex];

                            saveBookToPersonalBooks(selectedCategory);
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });
    }

    private void saveBookToPersonalBooks(String selectedCategory) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run(){
                PersonalBookEntity personalBookEntity = new PersonalBookEntity(
                        selectedBook.getTitle(),
                        selectedBook.getAuthor(),
                        selectedBook.getCoverResource(),
                        selectedCategory);
                personalBookEntity.setDescription(selectedBook.getDescription());

                AppDatabase.getInstance(requireContext()).personalBookDao().insertPersonalBook(personalBookEntity);
            }
        });

    }

    private boolean isValidImageUrl(String imageUrl) {
        return imageUrl != null && !imageUrl.isEmpty();
    }


}
