package com.example.group2.shoppinglist.Main;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.group2.shoppinglist.About.AboutActivity;
import com.example.group2.shoppinglist.AddToDo.AddToDoActivity;
import com.example.group2.shoppinglist.AppDefault.AppDefaultFragment;
import com.example.group2.shoppinglist.R;
import com.example.group2.shoppinglist.Utility.ItemTouchHelperClass;
import com.example.group2.shoppinglist.Utility.RecyclerViewEmptySupport;
import com.example.group2.shoppinglist.Utility.ShoppingList;
import com.example.group2.shoppinglist.Utility.StoreRetrieveData;
import com.example.group2.shoppinglist.Utility.ToDoItem;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.MODE_PRIVATE;

public class ToDoListFragment extends AppDefaultFragment {
    private RecyclerViewEmptySupport mRecyclerView;
    private FloatingActionButton mAddShoppingListItemFAB;
    private ArrayList<ToDoItem> mToDoItemsArrayList;
    private ShoppingList shoppingList;
    private CoordinatorLayout mCoordLayout;
    public static final String TODOITEM = "com.group2.com.group2.shoppinglist.MainActivity";
    public static final String SHOPPINGLIST = "com.group2.com.group2.shoppinglist.MainActivity";
    private ToDoListFragment.ShoppingListAdapter adapter;
    public static final int REQUEST_ID_TODO_ITEM = 100;
    private ToDoItem mJustDeletedToDoItem;
    private int mIndexOfDeletedToDoItem;
    public static final String FILENAME = "todoitems.json";
    private StoreRetrieveData storeRetrieveData;
    public ItemTouchHelper itemTouchHelper;
    private CustomRecyclerScrollViewListener customRecyclerScrollViewListener;
    public static final String SHARED_PREF_DATA_SET_CHANGED = "com.group2.datasetchanged";
    public static final String CHANGE_OCCURED = "com.group2.changeoccured";
    public static final String THEME_PREFERENCES = "com.group2.themepref";
    public static final String RECREATE_ACTIVITY = "com.group2.recreateactivity";
    public static final String THEME_SAVED = "com.group2.savedtheme";
    public static final String LIGHTTHEME = "com.group2.lighttheme";
    private Application app;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        app = getActivity().getApplication();
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        shoppingList = (ShoppingList) args.getSerializable("shoppingList");


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CHANGE_OCCURED, false);
        editor.apply();

        mToDoItemsArrayList = shoppingList.getToDoItems();
        adapter = new ToDoListFragment.ShoppingListAdapter(mToDoItemsArrayList);

        mCoordLayout = (CoordinatorLayout) view.findViewById(R.id.myCoordinatorLayout);
        mAddShoppingListItemFAB = (FloatingActionButton) view.findViewById(R.id.addShoppingListFAB);

        mAddShoppingListItemFAB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent newTodo = new Intent(getContext(), AddToDoActivity.class);
                ToDoItem item = new ToDoItem("","", false, null);
                int color = ColorGenerator.MATERIAL.getRandomColor();
                item.setTodoColor(color);
                newTodo.putExtra(TODOITEM, item);
                startActivityForResult(newTodo, REQUEST_ID_TODO_ITEM);
            }
        });

        mRecyclerView = (RecyclerViewEmptySupport) view.findViewById(R.id.toDoRecyclerView);
        mRecyclerView.setBackgroundColor(getResources().getColor(R.color.primary_lightest));
        mRecyclerView.setEmptyView(view.findViewById(R.id.toDoEmptyView));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        customRecyclerScrollViewListener = new CustomRecyclerScrollViewListener() {
            @Override
            public void show() {

                mAddShoppingListItemFAB.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {

                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mAddShoppingListItemFAB.getLayoutParams();
                int fabMargin = lp.bottomMargin;
                mAddShoppingListItemFAB.animate().translationY(mAddShoppingListItemFAB.getHeight() + fabMargin).setInterpolator(new AccelerateInterpolator(2.0f)).start();
            }
        };
        mRecyclerView.addOnScrollListener(customRecyclerScrollViewListener);


        ItemTouchHelper.Callback callback = new ItemTouchHelperClass(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


        mRecyclerView.setAdapter(adapter);
    }

    public static ArrayList<ShoppingList> getLocallyStoredData(StoreRetrieveData storeRetrieveData) {
        ArrayList<ShoppingList> items = null;

        try {
            items = storeRetrieveData.loadFromFile();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        if (items == null) {
            items = new ArrayList<>();
        }
        return items;

    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity().getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE).getBoolean(RECREATE_ACTIVITY, false)) {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE).edit();
            editor.putBoolean(RECREATE_ACTIVITY, false);
            editor.apply();
            getActivity().recreate();
        }
    }

    @Override
    public void onStart() {
        app = getActivity().getApplication();
        super.onStart();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(CHANGE_OCCURED, false)) {

            mToDoItemsArrayList = shoppingList.getToDoItems();
            adapter = new ToDoListFragment.ShoppingListAdapter(mToDoItemsArrayList);
            mRecyclerView.setAdapter(adapter);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(CHANGE_OCCURED, false);
            editor.apply();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.aboutMeMenuItem) {
            Intent i = new Intent(getContext(), AboutActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED && requestCode == REQUEST_ID_TODO_ITEM) {
            ToDoItem item = (ToDoItem) data.getSerializableExtra(TODOITEM);
            if (item.getToDoText().length() <= 0) {
                return;
            }
            boolean existed = false;

            for (int i = 0; i < mToDoItemsArrayList.size(); i++) {
                if (item.getIdentifier().equals(mToDoItemsArrayList.get(i).getIdentifier())) {
                    mToDoItemsArrayList.set(i, item);
                    existed = true;
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
            if (!existed) {
                addToDataStore(item);
            }


        }
    }

    private void addToDataStore(ToDoItem item) {
        mToDoItemsArrayList.add(item);
        adapter.notifyItemInserted(mToDoItemsArrayList.size() - 1);

    }

    public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> implements ItemTouchHelperClass.ItemTouchHelperAdapter {
        private ArrayList<ToDoItem> lists;

        @Override
        public void onItemMoved(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(lists, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(lists, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRemoved(final int position) {
            mJustDeletedToDoItem = lists.remove(position);
            mIndexOfDeletedToDoItem = position;
            notifyItemRemoved(position);

            String toShow = "Todo";
            Snackbar.make(mCoordLayout, "Deleted " + toShow, Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            lists.add(mIndexOfDeletedToDoItem, mJustDeletedToDoItem);
                            notifyItemInserted(mIndexOfDeletedToDoItem);
                        }
                    }).show();
        }

        @Override
        public ShoppingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_circle_try, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ShoppingListAdapter.ViewHolder holder, final int position) {
            ToDoItem list = lists.get(position);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE);
            int bgColor;
            int todoTextColor;
            if (sharedPreferences.getString(THEME_SAVED, LIGHTTHEME).equals(LIGHTTHEME)) {
                bgColor = Color.WHITE;
                todoTextColor = getResources().getColor(R.color.secondary_text);
            } else {
                bgColor = Color.DKGRAY;
                todoTextColor = Color.WHITE;
            }
            holder.linearLayout.setBackgroundColor(bgColor);

            if (list.hasReminder() && list.getToDoDate() != null) {
                holder.mToDoTextview.setMaxLines(1);
                holder.mTimeTextView.setVisibility(View.VISIBLE);
            } else {
                holder.mTimeTextView.setVisibility(View.GONE);
                holder.mToDoTextview.setMaxLines(2);
            }
            holder.mToDoTextview.setText(list.getToDoText());
            holder.mToDoTextview.setTextColor(todoTextColor);
            TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(list.getToDoText().substring(0, 1), list.getTodoColor());

            holder.mColorImageView.setImageDrawable(myDrawable);
        }

        @Override
        public int getItemCount() {
            if (lists != null) {
                return lists.size();
            } else {
                return 0;
            }
        }

        ShoppingListAdapter(ArrayList<ToDoItem> lists) {
            this.lists = lists;
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            TextView mToDoTextview;
            ImageView mColorImageView;
            TextView mTimeTextView;

            ViewHolder(View v) {
                super(v);
                mView = v;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToDoItem item = lists.get(ViewHolder.this.getAdapterPosition());
                        Intent i = new Intent(getContext(), AddToDoActivity.class);
                        i.putExtra(TODOITEM, item);
                        i.putExtra("showTodo", true);
                        startActivityForResult(i, REQUEST_ID_TODO_ITEM);
                    }
                });
                mToDoTextview = (TextView) v.findViewById(R.id.toDoListItemTextview);
                mTimeTextView = (TextView) v.findViewById(R.id.todoListItemTimeTextView);
                mColorImageView = (ImageView) v.findViewById(R.id.toDoListItemColorImageView);
                linearLayout = (LinearLayout) v.findViewById(R.id.listItemLinearLayout);
            }


        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ShoppingList a = shoppingList;
        /*try {

            storeRetrieveData.saveToFile(shoppingList);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }*/
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
        mRecyclerView.removeOnScrollListener(customRecyclerScrollViewListener);
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_main;
    }

    public static ToDoListFragment newInstance(ShoppingList shoppingList) {
        ToDoListFragment f = new ToDoListFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("shoppingList", shoppingList);
        f.setArguments(args);
        return f;
    }
}
