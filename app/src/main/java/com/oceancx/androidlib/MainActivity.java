package com.oceancx.androidlib;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends ListActivity {


    private ActivityInfo target;
    private ArrayList<ActivityInfo> mActivities;
    private String packageName = "com.oceancx.androidlib";
    private String desiredPackageName = "MediaPlayerActivity";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> activity_names = new ArrayList<String>();
        mActivities = new ArrayList<ActivityInfo>();
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES);
            ArrayList<ActivityInfo> allActivities = new ArrayList<ActivityInfo>(
                    Arrays.asList(pi.activities));

            mActivities.add(allActivities.get(0));

            for (int i = 0; i < allActivities.size(); i++) {
                String name = allActivities.get(i).name;
                mActivities.add(allActivities.get(i));
                activity_names.add(name.substring(name.lastIndexOf(".") + 1, name.length()));
                if (name.contains(desiredPackageName)) {
                    target = allActivities.get(i);
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                activity_names);

        getListView().setAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.setClassName(packageName, mActivities.get(position + 1).name);
                startActivity(intent);
            }

        });
        if (target != null) {
            Intent intent = new Intent();
            intent.setClassName(packageName, target.name);
            startActivity(intent);
        }
    }


}
