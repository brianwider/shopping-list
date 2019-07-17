package com.example.group2.shoppinglist.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.group2.shoppinglist.About.AboutActivity;
import com.example.group2.shoppinglist.AppDefault.AppDefaultActivity;
import com.example.group2.shoppinglist.R;
import com.example.group2.shoppinglist.Utility.ShoppingList;
import com.example.group2.shoppinglist.Utility.StoreRetrieveData;
import com.example.group2.shoppinglist.Utility.ToDoItem;

import java.util.ArrayList;

public class ToDoListActivity extends AppDefaultActivity {

    private ShoppingList shoppingList;
    private ToDoListFragment toDoListFragment = new ToDoListFragment();
    private StoreRetrieveData storeRetrieveData = new StoreRetrieveData(toDoListFragment.getContext(), ToDoListFragment.FILENAME);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected int contentViewLayoutRes() {
        return R.layout.activity_todo_list;
    }

    @NonNull
    @Override
    protected Fragment createInitialFragment() {
        shoppingList = (ShoppingList) getIntent().getExtras().getSerializable(MainFragment.SHOPPINGLIST);
        return ToDoListFragment.newInstance(shoppingList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.aboutMeMenuItem) {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ToDoListFragment.REQUEST_ID_TODO_ITEM) {
            if (resultCode == Activity.RESULT_OK){
                ToDoItem item = (ToDoItem) data.getSerializableExtra(ToDoListFragment.TODOITEM);
                ArrayList<ToDoItem> items = shoppingList.getToDoItems();
                items.add(item);
                shoppingList.setToDoItems(items);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}


