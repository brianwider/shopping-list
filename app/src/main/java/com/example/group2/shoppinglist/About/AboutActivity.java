package com.example.group2.shoppinglist.About;

import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.group2.shoppinglist.AppDefault.AppDefaultActivity;
import com.example.group2.shoppinglist.R;

public class AboutActivity extends AppDefaultActivity {

    private String appVersion = "0.1";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final Drawable backArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        if (backArrow != null) {
            backArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(backArrow);
        }
    }

    @Override
    protected int contentViewLayoutRes() {
        return R.layout.about_layout;
    }

    @NonNull
    protected Fragment createInitialFragment() {
        return AboutFragment.newInstance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(this) != null) {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
