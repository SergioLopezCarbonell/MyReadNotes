package com.example.myapplication.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.R;
import com.example.myapplication.Utils.GradientUtil;
import com.example.myapplication.adapters.CommentAdapter;
import com.example.myapplication.adapters.MomentAdapter;
import com.example.myapplication.dao.CommentDao;
import com.example.myapplication.dao.MomentDao;
import com.example.myapplication.dao.PersonalBookDao;
import com.example.myapplication.entitys.CommentEntity;
import com.example.myapplication.entitys.MomentEntity;
import com.example.myapplication.entitys.PersonalBookEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BookSelectedFragment extends Fragment {

    private PersonalBookEntity selectedBook;

    public BookSelectedFragment(PersonalBookEntity book) {
        this.selectedBook = book;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_selectedbook, container, false);

        ViewFlipper viewFlipper = view.findViewById(R.id.viewFlipper);

        String title = selectedBook.getTitle();
        String category = selectedBook.getCategory();
        String author = selectedBook.getAuthor();
        String coverResource = selectedBook.getCoverResource();
        String description = selectedBook.getDescription();

        FrameLayout frameLayout = view.findViewById(R.id.gradientCategory);
        GradientDrawable gradient = GradientUtil.getGradientForCategory(category);
        frameLayout.setBackground(gradient);

        LinearLayout linearLayoutButtons = view.findViewById(R.id.buttonRow);
        linearLayoutButtons.setBackground(gradient);

        ImageView imageView = view.findViewById(R.id.BookImage);

        if (isValidImageUrl(coverResource)) {
            Picasso.get().load(coverResource).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.image_not_available);
        }

        TextView textViewCategory = view.findViewById(R.id.CategoryTextView);
        textViewCategory.setText(category);

        TextView textViewTitle = view.findViewById(R.id.TitleTextView);
        textViewTitle.setText(title);

        TextView textViewAuthor = view.findViewById(R.id.AuthorTextView);
        textViewAuthor.setText(author);

        TextView textViewDescription = view.findViewById(R.id.DescriptionText);
        textViewDescription.setText(description);

        Button buttonStats = view.findViewById(R.id.buttonStats);
        Button buttonComments = view.findViewById(R.id.buttonComments);
        Button buttonMoments = view.findViewById(R.id.buttonMoments);
        Button buttonAddComment = view.findViewById(R.id.newCommentButton);
        Button buttonAddMoment = view.findViewById(R.id.newMomentButton);
        Button buttonDelete = view.findViewById(R.id.buttonDelete);

        LinearLayout layoutEndDate = view.findViewById(R.id.endDate);
        CheckBox finishCheckBox = view.findViewById(R.id.finish);

        EditText editStartTextDate = view.findViewById(R.id.editStartTextDate);
        EditText editEndTextDate = view.findViewById(R.id.editEndTextDate);

        editStartTextDate.setText(selectedBook.getStartDate());
        editEndTextDate.setText(selectedBook.getEndDate());

        finishCheckBox.setChecked(selectedBook.isFinish());
        buttonStats.setSelected(true);

        layoutEndDate.setVisibility(selectedBook.isFinish() ? View.VISIBLE : View.GONE);

        ImageButton buttonBack = view.findViewById(R.id.btnBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });


        //COMENTS

        AppDatabase appDatabase = AppDatabase.getInstance(requireContext());
        CommentDao commentDao = appDatabase.commentDao();

        List<CommentEntity> commentsList = new ArrayList<>();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<CommentEntity> commentsFromDatabase = commentDao.getCommentsForPersonalBook(selectedBook.getId());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        commentsList.clear();
                        commentsList.addAll(commentsFromDatabase);

                    }
                });
            }
        });

        RecyclerView commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView);

        CommentAdapter commentAdapter = new CommentAdapter(commentsList);

        commentsRecyclerView.setAdapter(commentAdapter);

        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        //MOMENTS

        MomentDao momentDao = appDatabase.momentDao();

        List<MomentEntity> momentsList = new ArrayList<>();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<MomentEntity> momentsFromDatabase = momentDao.getMomentsForPersonalBook(selectedBook.getId());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        momentsList.clear();
                        momentsList.addAll(momentsFromDatabase);
                    }
                });
            }
        });

        RecyclerView momentsRecyclerView = view.findViewById(R.id.momentsRecyclerView);

        MomentAdapter momentAdapter = new MomentAdapter(momentsList);
        momentsRecyclerView.setAdapter(momentAdapter);

        momentsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        buttonStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStats.setSelected(true);
                buttonComments.setSelected(false);
                buttonMoments.setSelected(false);
                viewFlipper.setDisplayedChild(0);
            }
        });

        buttonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStats.setSelected(false);
                buttonComments.setSelected(true);
                buttonMoments.setSelected(false);
                viewFlipper.setDisplayedChild(1);
            }
        });

        buttonMoments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStats.setSelected(false);
                buttonComments.setSelected(false);
                buttonMoments.setSelected(true);
                viewFlipper.setDisplayedChild(2);
            }
        });

        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                NewCommentFragment newCommentFragment = new NewCommentFragment();
                newCommentFragment.setPersonalBookId(selectedBook.getId());

                transaction.replace(R.id.container, newCommentFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        buttonAddMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                NewMomentFragment newMomentFragment = new NewMomentFragment();
                newMomentFragment.setPersonalBookId(selectedBook.getId());

                transaction.replace(R.id.container, newMomentFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Caution");
                builder.setMessage("Are you sure you want to delete the book");

                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PersonalBookDao personalBookDao = AppDatabase.getInstance(requireContext()).personalBookDao();

                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                long bookIdToDelete = selectedBook.getId();
                                personalBookDao.deletePersonalBookById(bookIdToDelete);
                            }
                        });

                        if (getActivity() != null && !getActivity().isFinishing() && isAdded()) {
                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                            if (fragmentManager.getBackStackEntryCount() > 0) {
                                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            }
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });



        editStartTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);


                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = String.format(Locale.US, "%02d/%02d/%04d", dayOfMonth, month + 1 , year);

                        editStartTextDate.setText(selectedDate);

                        selectedBook.setStartDate(selectedDate);
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                AppDatabase.getInstance(requireContext()).personalBookDao().updatePersonalBook(selectedBook);
                            }
                        });
                    }
                }, year, month, day).show();
            }
        });

        editEndTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);


                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = String.format(Locale.US, "%02d/%02d/%04d", dayOfMonth, month + 1 , year);


                        selectedBook.setEndDate(selectedDate);
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                AppDatabase.getInstance(requireContext()).personalBookDao().updatePersonalBook(selectedBook);
                            }
                        });
                        editEndTextDate.setText(selectedDate);
                    }
                }, year, month, day).show();
            }
        });

        //CHECKBOX


        finishCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                selectedBook.setFinish(isChecked);

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getInstance(requireContext()).personalBookDao().updatePersonalBook(selectedBook);
                    }
                });

                layoutEndDate.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });


        return view;
    }
    private boolean isValidImageUrl(String imageUrl) {
        return imageUrl != null && !imageUrl.isEmpty();
    }

}

