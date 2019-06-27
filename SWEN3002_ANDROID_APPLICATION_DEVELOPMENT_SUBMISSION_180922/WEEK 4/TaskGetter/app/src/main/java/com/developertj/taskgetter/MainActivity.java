package com.developertj.taskgetter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "Main";

    private ArrayList<Task> mTaskList = new ArrayList<>();
    private TaskDisplayFragment taskDisplayFragment = new TaskDisplayFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //begin fetch tasks.
        new TaskFetcher(taskDisplayFragment).execute();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, taskDisplayFragment, "TaskDisplayFragment")
                .addToBackStack(null)
                .commit();
    }

    public static class TaskFetcher extends AsyncTask<Void, Void, List<Task>>{
        private final String TAG = "fetchTasks";
        private TaskDisplayFragment taskDisplayFragment;

        public TaskFetcher(TaskDisplayFragment taskDisplayFragment) {
            this.taskDisplayFragment = taskDisplayFragment;
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "Fetching tasks...");
        }

        @Override
        protected List<Task> doInBackground(Void... voids) {
            com.developertj.taskgetter.TaskFetcher mTaskFetcher = new com.developertj.taskgetter.TaskFetcher();
            return mTaskFetcher.fetchItems();
        }

        @Override
        protected void onPostExecute(List<Task> tasks) {
            Log.i(TAG, "Adding " + tasks.size() + " tasks...");
            for (Task task: tasks) {
                taskDisplayFragment.addTaskCard(task);
                Log.i(TAG, "Added TaskCard: " + task);
            }
            Log.d(TAG, "Completed fetching tasks.");
        }
    }
}
