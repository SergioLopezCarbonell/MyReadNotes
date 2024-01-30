package com.example.myapplication.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.R;
import com.example.myapplication.dao.PersonalBookDao;
import com.example.myapplication.Utils.CircleProgressBar;

import java.lang.ref.WeakReference;

public class UserFragment extends Fragment {

    private WeakReference<Fragment> weakFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);
        CircleProgressBar circleProgressBar = view.findViewById(R.id.circleProgressBar);
        TextView progressNumberText = view.findViewById(R.id.centerTextViewNumber);
        TextView completedNumberText = view.findViewById(R.id.completedNumber);
        TextView inProgressNumberText = view.findViewById(R.id.progressNumber);


        TextView fictionPercentage = view.findViewById(R.id.fictionPercentage);
        TextView nonFictionPercentage = view.findViewById(R.id.nonFictionPercentage);
        TextView literaryGenrePercentage = view.findViewById(R.id.literaryGenrePercentage);
        TextView adventuresPercentage = view.findViewById(R.id.adventurePercentage);
        TextView misteryPercentage = view.findViewById(R.id.misteryPercentage);
        TextView romancePercentage = view.findViewById(R.id.romancePercentage);
        TextView terrorPercentage = view.findViewById(R.id.terrorPercentage);
        TextView othersPercentage = view.findViewById(R.id.othersPercentage);





        weakFragment = new WeakReference<>(this);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (weakFragment.get() != null && weakFragment.get().isAdded()) {
                    PersonalBookDao personalBookDao = AppDatabase.getInstance(requireContext()).personalBookDao();

                    int finishedBooks = personalBookDao.getFinishedBookCount();
                    int totalBooks = personalBookDao.getBookCount();

                    int fictionCount = personalBookDao.getBookCountByCategory("Fiction");
                    int nonFictionCount = personalBookDao.getBookCountByCategory("Non-Fiction");
                    int literaryGenreCount = personalBookDao.getBookCountByCategory("Literary genre");
                    int adventuresCount = personalBookDao.getBookCountByCategory("Adventures");
                    int misteryCount = personalBookDao.getBookCountByCategory("Mistery");
                    int romanceCount = personalBookDao.getBookCountByCategory("Romance");
                    int terrorCount = personalBookDao.getBookCountByCategory("Terror");
                    int othersCount = personalBookDao.getBookCountByCategory("Others");


                    if (totalBooks > 0) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Actualiza los porcentajes correctamente
                                fictionPercentage.setText((fictionCount * 100) / totalBooks + "%");
                                nonFictionPercentage.setText((nonFictionCount * 100) / totalBooks + "%");
                                literaryGenrePercentage.setText((literaryGenreCount * 100) / totalBooks + "%");
                                adventuresPercentage.setText((adventuresCount * 100) / totalBooks + "%");
                                misteryPercentage.setText((misteryCount * 100) / totalBooks + "%");
                                romancePercentage.setText((romanceCount * 100) / totalBooks + "%");
                                terrorPercentage.setText((terrorCount * 100) / totalBooks + "%");
                                othersPercentage.setText((othersCount * 100) / totalBooks + "%");
                            }
                        });
                    } else {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Puedes establecer los porcentajes a cero o manejarlo de otra manera.
                                fictionPercentage.setText("0%");
                                nonFictionPercentage.setText("0%");
                                literaryGenrePercentage.setText("0%");
                                adventuresPercentage.setText("0%");
                                misteryPercentage.setText("0%");
                                romancePercentage.setText("0%");
                                terrorPercentage.setText("0%");
                                othersPercentage.setText("0%");
                            }
                        });
                    }


                    if (totalBooks > 0) {
                        int completed = (finishedBooks * 100) / totalBooks;
                        int progress = 100 - completed;


                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressNumberText.setText(finishedBooks + "/" + totalBooks);
                                completedNumberText.setText(completed + "%");
                                inProgressNumberText.setText(progress + "%");
                                circleProgressBar.setProgress(completed);
                            }
                        });
                    } else {

                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressNumberText.setText("0/0");
                                completedNumberText.setText("0%");
                                inProgressNumberText.setText("0%");
                                circleProgressBar.setProgress(0);
                            }
                        });
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        weakFragment.clear();
    }
}
