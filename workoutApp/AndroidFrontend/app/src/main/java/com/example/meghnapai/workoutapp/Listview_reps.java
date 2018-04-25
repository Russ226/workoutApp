package com.example.meghnapai.workoutapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.APICaller.base.WorkoutAPI;
import com.APICaller.exercise.GetExercise;
import com.APICaller.sets.GetSet;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Listview_reps extends AppCompatActivity {

    private ArrayList<GetSet> arrayList;// for the listview dynamic adding
    //private ArrayList<int> arrayList2;
    private ArrayAdapter<GetSet> adapter;
    ListView LVreps;
    TextView ExerciseName;
    String nameEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_reps);

        LVreps= (ListView) findViewById(R.id.LVReps);
        ExerciseName= (TextView) findViewById(R.id.ExerciseNameTV);
        arrayList=new ArrayList<GetSet>();
        adapter = new ArrayAdapter<GetSet>(this, R.layout.custom_listview_ex, R.id.textView, arrayList);
        LVreps.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();

        nameEx = extras.getString("ExerciseName");
//        String reps = RepsVal.getText().toString();
//        String weights = WeightsVal.getText().toString();

       ExerciseName.setText(nameEx);//to set the name of exercie

        Session session = new Session(Listview_reps.this);
        OkHttpClient httpClient = new OkHttpClient();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(WorkoutAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient);

        Retrofit retrofit = builder.build();
        final WorkoutAPI requests = retrofit.create(WorkoutAPI.class);

        Call<List<GetSet>> call = requests.getSetList(session.getUsername(),session.getDate(),session.getTime(),nameEx);

        call.enqueue(new Callback<List<GetSet>>() {
            @Override
            public void onResponse(Call<List<GetSet>> call, Response<List<GetSet>> response) {
                if(response.body() != null) {
                    ArrayList<GetSet> sets = new ArrayList<>(response.body());
                    for (GetSet set : sets) {
                        arrayList.add(set);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<GetSet>> call, Throwable t) {
                System.out.println("Throws: " + t);
            }
        });






    }
}