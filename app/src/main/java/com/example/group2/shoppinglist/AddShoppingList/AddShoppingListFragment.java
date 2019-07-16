package com.example.group2.shoppinglist.AddShoppingList;

import android.app.Application;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.group2.shoppinglist.AppDefault.AppDefaultFragment;
import com.example.group2.shoppinglist.Main.MainFragment;
import com.example.group2.shoppinglist.R;
import com.example.group2.shoppinglist.Utility.ShoppingList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;

public class AddShoppingListFragment extends AppDefaultFragment {
    private static final String TAG = "AddShoppingListFragment";

    private EditText mShoppingListTextBodyEditText;
    private EditText mShoppingListTextBodyDescription;

    private ShoppingList mUserShoppingList;
    private FloatingActionButton mShoppingListSendFloatingActionButton;

    private String mUserEnteredText;
    private String mUserEnteredDescription;
    private Toolbar mToolbar;
    private int mUserColor;
    private LinearLayout mContainerLayout;
    Application app;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        app = getActivity().getApplication();


        //Show an X in place of <-
        final Drawable cross = getResources().getDrawable(R.drawable.ic_clear_white_24dp);
        if (cross != null) {
            cross.setColorFilter(getResources().getColor(R.color.icons), PorterDuff.Mode.SRC_ATOP);
        }

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(cross);

        }


        mUserShoppingList = (ShoppingList) getActivity().getIntent().getSerializableExtra(MainFragment.SHOPPINGLIST);

        mUserEnteredText = mUserShoppingList.getShoppingListText();
        mUserEnteredDescription = mUserShoppingList.getmShoppingListDescription();
        mUserColor = mUserShoppingList.getShoppinglistColor();


        mContainerLayout = (LinearLayout) view.findViewById(R.id.shoppinglistReminderAndDateContainerLayout);
        mShoppingListTextBodyEditText = (EditText) view.findViewById(R.id.userShoppingListEditText);
        mShoppingListTextBodyDescription= (EditText) view.findViewById(R.id.userShoppingListDescription);
        mShoppingListSendFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.makeShoppingListFloatingActionButton);




        mContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mShoppingListTextBodyEditText);
                hideKeyboard(mShoppingListTextBodyDescription);
            }
        });

        mShoppingListTextBodyEditText.requestFocus();
        mShoppingListTextBodyEditText.setText(mUserEnteredText);
        mShoppingListTextBodyDescription.setText(mUserEnteredDescription);
        InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mShoppingListTextBodyEditText.setSelection(mShoppingListTextBodyEditText.length());


        mShoppingListTextBodyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUserEnteredText = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mShoppingListTextBodyDescription.setText(mUserEnteredDescription);
        mShoppingListTextBodyDescription.setSelection(mShoppingListTextBodyDescription.length());
        mShoppingListTextBodyDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUserEnteredDescription = s.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });


        mShoppingListSendFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShoppingListTextBodyEditText.length() <= 0) {
                    mShoppingListTextBodyEditText.setError(getString(R.string.shoppinglist_error));
                } else {
                    makeResult(RESULT_OK);
                    getActivity().finish();
                }
                hideKeyboard(mShoppingListTextBodyEditText);
                hideKeyboard(mShoppingListTextBodyDescription);
            }
        });
    }

    public void hideKeyboard(EditText et) {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }


    public void makeResult(int result) {
        Log.d(TAG, "makeResult - ok : in");
        Intent i = new Intent();
        if (mUserEnteredText.length() > 0) {

            String capitalizedString = Character.toUpperCase(mUserEnteredText.charAt(0)) + mUserEnteredText.substring(1);
            mUserShoppingList.setShoppingListText(capitalizedString);
            Log.d(TAG, "Description: " + mUserEnteredDescription);
            mUserShoppingList.setmShoppingListDescription(mUserEnteredDescription);
        } else {
            mUserShoppingList.setShoppingListText(mUserEnteredText);
            Log.d(TAG, "Description: " + mUserEnteredDescription);
            mUserShoppingList.setmShoppingListDescription(mUserEnteredDescription);
        }
        mUserShoppingList.setShoppinglistColor(mUserColor);
        i.putExtra(MainFragment.SHOPPINGLIST, mUserShoppingList);
        getActivity().setResult(result, i);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    makeResult(RESULT_CANCELED);
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                hideKeyboard(mShoppingListTextBodyEditText);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_add_shopping_list;
    }

    public static AddShoppingListFragment newInstance() {
        return new AddShoppingListFragment();
    }
}
