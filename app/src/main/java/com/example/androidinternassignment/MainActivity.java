package com.example.androidinternassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.androidinternassignment.EntityClass.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String name , capital , flag , region , subregion , population , boarders , languages;
    Button button;
    ArrayList<String> regionName;
    ArrayAdapter<String> arrayAdapter;
    private ArrayList<UserModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView=findViewById(R.id.listView);
        button=findViewById(R.id.button);
        button.setOnClickListener(this);
        regionName=new ArrayList<>();
        arrayAdapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,regionName);
        listView.setAdapter(arrayAdapter);
        getData();
        listView.setOnItemClickListener((parent, view, position, id) -> {
            list = (ArrayList<UserModel>) DatabaseClass.getDatabase(getApplicationContext()).getDao().getAllData();
            Intent intent=new Intent(getApplicationContext(),DisplayData.class);
            intent.putExtra("name", String.valueOf(list.get(position).getName()));
            intent.putExtra("capital", String.valueOf(list.get(position).getCapital()));
            intent.putExtra("flag", String.valueOf(list.get(position).getFlag()));
            intent.putExtra("region", String.valueOf(list.get(position).getRegion()));
            intent.putExtra("subRegion", String.valueOf(list.get(position).getSubregion()));
            intent.putExtra("population", String.valueOf(list.get(position).getPopulation()));
            intent.putExtra("borders", String.valueOf(list.get(position).getBoarders()));
            intent.putExtra("languages", String.valueOf(list.get(position).getLanguages()));
            startActivity(intent);


        });
    }

    private void getData() {
        regionName.clear();
        list = new ArrayList<>();
        list = (ArrayList<UserModel>) DatabaseClass.getDatabase(getApplicationContext()).getDao().getAllData();
        if (!list.isEmpty()) {
            for (int i=0;i< list.size();i++){
                UserModel name=list.get(i);
                regionName.add(name.getName());
            }
        }
        else if (list.isEmpty()){
            Toast.makeText(MainActivity.this, "Please wait data is loading from Rest API", Toast.LENGTH_SHORT).show();
            getContent();
        }
        arrayAdapter.notifyDataSetChanged();

    }

    public void getContent(){

        ExecutorService executorService= Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            String result = urlgetcontent("http://restcountries.eu/rest/v2/region/asia");
            runOnUiThread(() -> {
                try {
                    JSONArray jsonArray=new JSONArray(result);
                    Log.i("hello" + jsonArray.length(),jsonArray.toString());
                    for (int i=0; i < jsonArray.length(); i++){
                        JSONObject jsonObject =jsonArray.getJSONObject(i);
                        regionName.add(jsonObject.getString("name"));
                        name=jsonObject.getString("name");
                        capital=jsonObject.getString("capital");
                        flag=jsonObject.getString("flag");
                        region=jsonObject.getString("region");
                        subregion=jsonObject.getString("subregion");
                        population=jsonObject.getString("population");
                        boarders=jsonObject.getString("borders");
                        JSONArray array=new JSONArray(jsonObject.getString("languages"));
                        String languagesOfSpecificCountry = new String();
                        for (int j=0; j < array.length(); j++){
                            JSONObject object=array.getJSONObject(j);
                             languagesOfSpecificCountry += object.getString("name");
                        }
                        languages=languagesOfSpecificCountry;
                        saveData();
                    }
                    Toast.makeText(this, "Data is Downloaded and saved", Toast.LENGTH_SHORT).show();
                    arrayAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });
        });

    }

    public String urlgetcontent(String string){
        URL url;
        HttpURLConnection urlConnection;
        StringBuilder result= new StringBuilder();
        try {
            url=new URL(string);
            urlConnection=(HttpURLConnection)url.openConnection();
            InputStream inputStream=urlConnection.getInputStream();
            InputStreamReader reader=new InputStreamReader(inputStream);
            int data=reader.read();
            while (data!=-1){
                char current =(char) data;
                result.append(current);
                data=reader.read();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return result.toString();

    }

    private void saveData() {

        String name_txt = name.trim();
        String flag_txt = flag.trim();
        String subregion_txt = subregion.trim();
        String region_txt = region.trim();
        String languages_txt = languages.trim();
        String population_txt = population.trim();
        String boarders_txt = boarders.trim();
        String capital_txt = capital.trim();




        UserModel model = new UserModel();
        model.setName(name_txt);
        model.setFlag(flag_txt);
        model.setSubregion(subregion_txt);
        model.setRegion(region_txt);
        model.setLanguages(languages_txt);
        model.setPopulation(population_txt);
        model.setBoarders(boarders_txt);
        model.setCapital(capital_txt);
        DatabaseClass.getDatabase(getApplicationContext()).getDao().insertAllData(model);


    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.button) {


            for (int i = 0; i < list.size(); i++) {
                UserModel key = list.get(i);
                DatabaseClass.getDatabase(getApplicationContext()).getDao().deleteData(key.getKey());
            }
            list = (ArrayList<UserModel>) DatabaseClass.getDatabase(getApplicationContext()).getDao().getAllData();
            regionName.clear();
            arrayAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Database is deleted to get the data restart the app with internet connection ", Toast.LENGTH_SHORT).show();
        }

    }
}