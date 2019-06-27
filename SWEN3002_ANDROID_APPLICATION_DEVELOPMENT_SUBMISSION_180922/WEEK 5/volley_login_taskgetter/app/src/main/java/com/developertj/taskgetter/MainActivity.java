package com.developertj.taskgetter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "Main";

    private ArrayList<Task> mTaskList = new ArrayList<>();
    private TaskDisplayFragment taskDisplayFragment = new TaskDisplayFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //begin fetch tasks.
        new TaskGetter(taskDisplayFragment).execute();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, taskDisplayFragment, "TaskDisplayFragment")
                .addToBackStack(null)
                .commit();
    }

    public static class TaskGetter extends AsyncTask<Void, Void, Void>{
        private final String TAG = "fetchTasks";
        private TaskDisplayFragment taskDisplayFragment;

        public TaskGetter(TaskDisplayFragment taskDisplayFragment) {
            this.taskDisplayFragment = taskDisplayFragment;
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "Fetching tasks...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
          TaskFetcher mTaskFetcher = new TaskFetcher(taskDisplayFragment);
            mTaskFetcher.fetchItems_volley();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, "Completed fetching tasks.");
        }
    }
}
