package com.example.group2.shoppinglist.AddShoppingList;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.example.group2.shoppinglist.AddShoppingList.AddShoppingListFragment;
import com.example.group2.shoppinglist.AppDefault.AppDefaultActivity;
import com.example.group2.shoppinglist.R;

public class AddShoppingListActivity extends AppDefaultActivity {
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int contentViewLayoutRes() {
        return R.layout.activity_add_shopping_list;
    }

    @NonNull
    @Override
    protected Fragment createInitialFragment() {
        return AddShoppingListFragment.newInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

