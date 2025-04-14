package com.example.taxio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FindCAs extends AppCompatActivity {
    private Spinner spinner;
    private Button search;
    private ListView lv_name, lv_phone, lv_email;

    private Map<String, List<CAsHelperClass>> cityDataMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_cas);

        spinner = findViewById(R.id.spinner);
        search = findViewById(R.id.button);
        lv_name = findViewById(R.id.lv_name);
        lv_phone = findViewById(R.id.lv_phone);
        lv_email = findViewById(R.id.lv_email);

        String[] items = new String[]{"Waterloo", "Kitchener", "Cambridge", "Toronto", "Brampton"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        // Initialize data for each city
        initializeCityData();

        final String[] selectedCity = new String[1];
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedCity[0] = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCity[0] = items[0]; // Default to the first city
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateListViews(selectedCity[0]);
            }
        });

        // Initialize the list views with default city data
        updateListViews(items[0]);
    }

    private void initializeCityData() {
        cityDataMap = new HashMap<>();

        List<CAsHelperClass> waterlooData = new ArrayList<>();
        waterlooData.add(new CAsHelperClass("Alice Store", "alice@store.com", "+1 123 456 7890"));
        waterlooData.add(new CAsHelperClass("Bob the restore", "bob@restore.com", "+1 123 456 7891"));
        cityDataMap.put("Waterloo", waterlooData);

        List<CAsHelperClass> kitchenerData = new ArrayList<>();
        kitchenerData.add(new CAsHelperClass("Charlie Store", "charlie@store.com", "+1 987 654 3210"));
        kitchenerData.add(new CAsHelperClass("David's Store", "davids@store.com", "+1 987 654 3211"));
        cityDataMap.put("Kitchener", kitchenerData);

        List<CAsHelperClass> cambridgeData = new ArrayList<>();
        cambridgeData.add(new CAsHelperClass("Emma Supermarket", "emma@supermarket.com", "+1 555 123 4567"));
        cambridgeData.add(new CAsHelperClass("Frankfeet Store", "frankfeet@store.com", "+1 555 123 4568"));
        cityDataMap.put("Cambridge", cambridgeData);

        List<CAsHelperClass> torontoData = new ArrayList<>();
        torontoData.add(new CAsHelperClass("Grace Market", "grace@market.com", "+1 444 234 5678"));
        torontoData.add(new CAsHelperClass("Henry's Store", "henrys@store.com", "+1 444 234 5679"));
        cityDataMap.put("Toronto", torontoData);

        List<CAsHelperClass> bramptonData = new ArrayList<>();
        bramptonData.add(new CAsHelperClass("Ivy Center", "ivy@center.com", "+1 333 345 6789"));
        bramptonData.add(new CAsHelperClass("Westside Store", "westside@store.com", "+1 333 345 6790"));
        cityDataMap.put("Brampton", bramptonData);
    }

    private void updateListViews(String city) {
        List<CAsHelperClass> caList = cityDataMap.get(city);

        if (caList == null) {
            caList = new ArrayList<>();
        }

        String[] CAname = new String[caList.size()];
        String[] CAemail = new String[caList.size()];
        String[] CAphone = new String[caList.size()];

        for (int i = 0; i < caList.size(); i++) {
            CAsHelperClass ca = caList.get(i);
            CAname[i] = ca.getName();
            CAemail[i] = ca.getEmail();
            CAphone[i] = ca.getPhone();
        }

        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, CAname) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                textView.setTextColor(Color.BLACK);
                textView.setTextSize(13);
                return view;
            }
        };
        lv_name.setAdapter(nameAdapter);

        ArrayAdapter<String> emailAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, CAemail) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                textView.setTextColor(Color.BLACK);
                textView.setTextSize(13);
                return view;
            }
        };
        lv_email.setAdapter(emailAdapter);

        ArrayAdapter<String> phoneAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, CAphone) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                textView.setTextColor(Color.BLACK);
                textView.setTextSize(13);
                return view;
            }
        };
        lv_phone.setAdapter(phoneAdapter);
    }
}
