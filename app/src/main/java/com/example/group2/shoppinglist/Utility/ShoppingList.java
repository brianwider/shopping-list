package com.example.group2.shoppinglist.Utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class ShoppingList implements Serializable {
    private String mShoppingListText;
    private boolean mHasReminder;
    //add description
    private String mShoppingListDescription;
    //    private Date mLastEdited;
    private int mShoppinglistColor;
    private Date mShoppingListDate;
    private UUID mShoppinglistIdentifier;
    private ArrayList<ToDoItem> toDoItems;
    //add description
    private static final String SHOPPINGLISTDESCRIPTION = "shoppinglistdescription";
    private static final String SHOPPINGLISTTEXT = "shoppinglisttext";
    private static final String SHOPPINGLISTREMINDER = "shoppinglistreminder";
    private static final String SHOPPINGLISTTODOITEMS = "shoppinglisttodoitems";
    //    private static final String SHOPPINGLISTLASTEDITED = "shoppinglistlastedited";
    private static final String SHOPPINGLISTCOLOR = "shoppinglistcolor";
    private static final String SHOPPINGLISTDATE = "shoppinglistdate";
    private static final String SHOPPINGLISTIDENTIFIER = "shoppinglistidentifier";


    public ShoppingList(String shoppinglistBody, String shoppinglistdescription, boolean hasReminder, Date toDoDate) {
        mShoppingListText = shoppinglistBody;
        mHasReminder = hasReminder;
        mShoppingListDate = toDoDate;
        mShoppingListDescription = shoppinglistdescription;
        mShoppinglistColor = 1677725;
        mShoppinglistIdentifier = UUID.randomUUID();
    }

    public ShoppingList(JSONObject jsonObject) throws JSONException {
        mShoppingListText = jsonObject.getString(SHOPPINGLISTTEXT);
        mShoppingListDescription = jsonObject.getString(SHOPPINGLISTDESCRIPTION);
        mHasReminder = jsonObject.getBoolean(SHOPPINGLISTREMINDER);
        mShoppinglistColor = jsonObject.getInt(SHOPPINGLISTCOLOR);
        Gson a = new Gson();
        ArrayList<ToDoItem> items = new ArrayList<>();
        JSONArray jsonArray = null;
        if (jsonObject.has(SHOPPINGLISTTODOITEMS)) {
            jsonArray = new JSONArray(jsonObject.getString(SHOPPINGLISTTODOITEMS));
            for (int i = 0; i < jsonArray.length(); i++) {
                ToDoItem item = new ToDoItem(jsonArray.getJSONObject(i));
                items.add(item);
            }
            //items = new ArrayList<>(Arrays.asList(a.fromJson(jsonObject.getString(SHOPPINGLISTTODOITEMS), ToDoItem[].class)));
        } else {
            items = new ArrayList<>();
        }

        this.setToDoItems(items);

        mShoppinglistIdentifier = UUID.fromString(jsonObject.getString(SHOPPINGLISTIDENTIFIER));

//        if(jsonObject.has(SHOPPINGLISTLASTEDITED)){
//            mLastEdited = new Date(jsonObject.getLong(SHOPPINGLISTLASTEDITED));
//        }
        if (jsonObject.has(SHOPPINGLISTDATE)) {
            mShoppingListDate = new Date(jsonObject.getLong(SHOPPINGLISTDATE));
        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(SHOPPINGLISTTEXT, mShoppingListText);
        jsonObject.put(SHOPPINGLISTREMINDER, mHasReminder);
        jsonObject.put(SHOPPINGLISTDESCRIPTION, mShoppingListDescription);
        if (toDoItems != null && toDoItems.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (ToDoItem todo : toDoItems) {
                JSONObject jsonTodoObject = todo.toJSON();
                jsonArray.put(jsonTodoObject);
            }
            jsonObject.put(SHOPPINGLISTTODOITEMS, jsonArray.toString());
        }
//        jsonObject.put(SHOPPINGLISTLASTEDITED, mLastEdited.getTime());
        if (mShoppingListDate != null) {
            jsonObject.put(SHOPPINGLISTDATE, mShoppingListDate.getTime());
        }
        jsonObject.put(SHOPPINGLISTCOLOR, mShoppinglistColor);
        jsonObject.put(SHOPPINGLISTIDENTIFIER, mShoppinglistIdentifier.toString());

        return jsonObject;
    }


    public ShoppingList() {
        this("Clean my room","Sweep and Mop my Room", true, new Date());
    }

    public String getmShoppingListDescription() { return mShoppingListDescription;}

    public void setmShoppingListDescription(String mShoppingListDescription){this.mShoppingListDescription = mShoppingListDescription;}

    public String getShoppingListText() {
        return mShoppingListText;
    }

    public void setShoppingListText(String mShoppingListText) {
        this.mShoppingListText = mShoppingListText;
    }

    public void setToDoItems(ArrayList<ToDoItem> items) {
        this.toDoItems = items;
    }

    public ArrayList<ToDoItem> getToDoItems() {
        return this.toDoItems;
    }

    public boolean hasReminder() {
        return mHasReminder;
    }

    public void setHasReminder(boolean mHasReminder) {
        this.mHasReminder = mHasReminder;
    }

    public Date getShoppingListDate() {
        return mShoppingListDate;
    }

    public int getShoppinglistColor() {
        return mShoppinglistColor;
    }

    public void setShoppinglistColor(int mShoppinglistColor) {
        this.mShoppinglistColor = mShoppinglistColor;
    }

    public void setShoppingListDate(Date mShoppingListDate) {
        this.mShoppingListDate = mShoppingListDate;
    }


    public UUID getIdentifier() {
        return mShoppinglistIdentifier;
    }
}

