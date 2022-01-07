package com.vaibhav.telementary;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    PackageManager packageManager;
    private final appProperties app;
    private final int sizeTotal;
    private final DrAdb adb;
    private final String suspndR = "pm suspend ";
    private final String unsuspndR = "pm unsuspend ";

    public MyAdapter(@NonNull Context ct) throws PackageManager.NameNotFoundException {
        context = ct;
        adb = new DrAdb(ct);
        this.app = new appProperties(context);
        packageManager = app.pm;
        sizeTotal = app.packageSize;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.app_child, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder main_view, int position) {
        main_view.icon.setImageDrawable(app.appicons.get(position));
        main_view.appName.setText(app.appNames.get(position));
        main_view.package_address.setText(app.pkgNames.get(position));
        String packageAdd = app.pkgNames.get(position);
        main_view.info_more.setOnClickListener(v -> context.startActivity(app.infoIntent.get(position)));
        main_view.play_store.setOnClickListener(v -> context.startActivity(app.playstore.get(position)));
        main_view.icon.setOnClickListener(v -> Toast.makeText(context, "Launching App Not Supported Right Now!", Toast.LENGTH_SHORT).show());
        main_view.delete.setOnClickListener(v -> context.startActivity (new Intent(Intent.ACTION_UNINSTALL_PACKAGE).setData(Uri.parse("package:"+app.pkgNames.get(position)))));
        main_view.suspend.setOnClickListener(v -> {
            adb.Commander(suspndR + packageAdd);
//            packageManager.setApplicationEnabledSetting(packageAdd, PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER, PackageManager.SYNCHRONOUS);
        });
        main_view.unsuspend.setOnClickListener(v -> adb.Commander(unsuspndR + packageAdd));
        if (position == sizeTotal) {
            Toast.makeText(context, " --- MyAdapter Loading Finished ---", Toast.LENGTH_SHORT).show();
            Log.d("MyAdapter", " Loading of MyAdapter Completed");
        }
    }

    @Override
    public int getItemCount() {
        return sizeTotal;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView appName, package_address, appsize;
        Button suspend, unsuspend, delete, play_store;
        ImageView icon;
        ImageButton info_more;

        public MyViewHolder(@NonNull View recycler_view) {
            super(recycler_view);
            appName = recycler_view.findViewById(R.id.app_name);
            appsize = recycler_view.findViewById(R.id.appSize);
            package_address = recycler_view.findViewById(R.id.package_address);
            suspend = recycler_view.findViewById(R.id.suspend);
            unsuspend = recycler_view.findViewById(R.id.unsuspend);
            delete = recycler_view.findViewById(R.id.delete);
            icon = recycler_view.findViewById(R.id.app_icon);
            info_more = recycler_view.findViewById(R.id.info_more);
            play_store = recycler_view.findViewById(R.id.play_store);
        }

    }
}
