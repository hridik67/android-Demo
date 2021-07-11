package com.example.androidinternassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

public class DisplayData extends AppCompatActivity {

    TextView name , capital , region , subregion , population , boarders , languages;
    ImageView flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        name=findViewById(R.id.name);
        capital=findViewById(R.id.capitcal);
        region=findViewById(R.id.region);
        subregion=findViewById(R.id.subRegion);
        population=findViewById(R.id.population);
        boarders=findViewById(R.id.borders);
        languages=findViewById(R.id.language);
        flag=findViewById(R.id.flag);
        //Display Data
        name.setText("Name :- "+getIntent().getExtras().getString("name"));
        capital.setText("Capital :- "+getIntent().getExtras().getString("capital"));
        region.setText("Region :- "+getIntent().getExtras().getString("region"));
        subregion.setText("Sub Region :- "+getIntent().getExtras().getString("subRegion"));
        population.setText("Population :- "+getIntent().getExtras().getString("population"));
        boarders.setText("Borders :- "+getIntent().getExtras().getString("borders"));
        languages.setText("Languages :- "+getIntent().getExtras().getString("languages"));

        GlideToVectorYou.init().with(this).load(Uri.parse(getIntent().getExtras().getString("flag")),flag);


    }
}