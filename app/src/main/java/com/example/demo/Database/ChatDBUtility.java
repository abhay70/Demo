package com.example.demo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.demo.Model.DataRecords;

import java.util.ArrayList;


public class ChatDBUtility {

    public static ChatDBHelper chatDBHelper;

    public ChatDBHelper CreateChatDB(Context context)
    {
        if (chatDBHelper == null) {
            chatDBHelper = new ChatDBHelper(context);
        }

        return chatDBHelper;

    }


    public long AddToDataListDB(ChatDBHelper chatDBHelper, DataRecords dataResponse) {
        // Gets the data repository in write mode
        SQLiteDatabase db = chatDBHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        //values.put(FeedReaderContract.DataList.COLUMN_NAME_ID, dataResponse.getId());
        values.put(FeedReaderContract.DataList.COLUMN_NAME_AGE, dataResponse.getAge());
        values.put(FeedReaderContract.DataList.COLUMN_NAME_EMAIL, dataResponse.getEmail());
        values.put(FeedReaderContract.DataList.COLUMN_NAME_IMAGE_URL, dataResponse.getImage_url());
        values.put(FeedReaderContract.DataList.COLUMN_NAME_PHONE, dataResponse.getPhone_number());
        values.put(FeedReaderContract.DataList.COLUMN_NAME_NAME, dataResponse.getName());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FeedReaderContract.DataList.TABLE_NAME,
                null,
                values);
        return newRowId;
    }


    public ArrayList<DataRecords> GetDataList(ChatDBHelper DBHelper) {
        Cursor cursor = GetRowsDataListDB(DBHelper);

        ArrayList<DataRecords> dataResponse = new ArrayList<DataRecords>();
        DataRecords dataResponse1;

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            dataResponse1 = new DataRecords();
            dataResponse1.setId(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.DataList.COLUMN_NAME_ID)));
            dataResponse1.setAge(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.DataList.COLUMN_NAME_AGE)));
            dataResponse1.setPhone_number(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.DataList.COLUMN_NAME_PHONE)));
            dataResponse1.setName(cursor.getString(cursor.getColumnIndex(FeedReaderContract.DataList.COLUMN_NAME_NAME)));
            dataResponse1.setEmail(cursor.getString(cursor.getColumnIndex(FeedReaderContract.DataList.COLUMN_NAME_EMAIL)));
            dataResponse1.setImage_url(cursor.getString(cursor.getColumnIndex(FeedReaderContract.DataList.COLUMN_NAME_IMAGE_URL)));


            dataResponse.add(dataResponse1);

            cursor.moveToNext();
        }


        cursor.close();
        return dataResponse;
    }


    Cursor GetRowsDataListDB(ChatDBHelper chatDBHelper) {
        SQLiteDatabase db = chatDBHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedReaderContract.DataList.COLUMN_NAME_IMAGE_URL,
                FeedReaderContract.DataList.COLUMN_NAME_PHONE,
                FeedReaderContract.DataList.COLUMN_NAME_EMAIL,
                FeedReaderContract.DataList.COLUMN_NAME_NAME,
                FeedReaderContract.DataList.COLUMN_NAME_AGE,
                FeedReaderContract.DataList.COLUMN_NAME_ID,


        };

        // How you want the results sorted in the resulting Cursor
        // String sortOrder =
        //
        String whereClause = "";


        Cursor c = db.query(
                FeedReaderContract.DataList.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );


        return c;
    }

    public void DeleteFromData(ChatDBHelper chatDBHelper) {
        SQLiteDatabase db = chatDBHelper.getWritableDatabase();
        db.execSQL("delete from  " + FeedReaderContract.DataList.TABLE_NAME );
    }


    public void DeleteFromDataAccTOID(ChatDBHelper chatDBHelper ,int id) {
        SQLiteDatabase db = chatDBHelper.getWritableDatabase();
        db.execSQL("delete from  " + FeedReaderContract.DataList.TABLE_NAME +" where "+ FeedReaderContract.DataList.COLUMN_NAME_ID +" = "+id );
    }


    public void UpdateName(ChatDBHelper chatDBHelper, String name, int id) {

        SQLiteDatabase db = chatDBHelper.getWritableDatabase();

        String strSQL = "UPDATE " + FeedReaderContract.DataList.TABLE_NAME + " Set " + FeedReaderContract.DataList.COLUMN_NAME_NAME + " = '" + name +"'"+
                " where " + FeedReaderContract.DataList.COLUMN_NAME_ID + " = " + id ;
        db.execSQL(strSQL);


    }

    public void UpdateEmail(ChatDBHelper chatDBHelper, String email, int id) {

        SQLiteDatabase db = chatDBHelper.getWritableDatabase();

        String strSQL = "UPDATE " + FeedReaderContract.DataList.TABLE_NAME + " Set " + FeedReaderContract.DataList.COLUMN_NAME_EMAIL + " = '" + email +"'"+
                " where " + FeedReaderContract.DataList.COLUMN_NAME_ID + " = " + id ;
        db.execSQL(strSQL);


    }

    public void UpdateAge(ChatDBHelper chatDBHelper, int age, int id) {

        SQLiteDatabase db = chatDBHelper.getWritableDatabase();

        String strSQL = "UPDATE " + FeedReaderContract.DataList.TABLE_NAME + " Set " + FeedReaderContract.DataList.COLUMN_NAME_AGE + " = " + age +
                " where " + FeedReaderContract.DataList.COLUMN_NAME_ID + " = " + id ;
        db.execSQL(strSQL);


    }

    public void UpdatePhone(ChatDBHelper chatDBHelper, int phone, int id) {

        SQLiteDatabase db = chatDBHelper.getWritableDatabase();

        String strSQL = "UPDATE " + FeedReaderContract.DataList.TABLE_NAME + " Set " + FeedReaderContract.DataList.COLUMN_NAME_PHONE + " = " + phone +
                " where " + FeedReaderContract.DataList.COLUMN_NAME_ID + " = " + id ;
        db.execSQL(strSQL);


    }


    public void UpdateImageUrl(ChatDBHelper chatDBHelper, String image_url, int id) {

        SQLiteDatabase db = chatDBHelper.getWritableDatabase();

        String strSQL = "UPDATE " + FeedReaderContract.DataList.TABLE_NAME + " Set " + FeedReaderContract.DataList.COLUMN_NAME_IMAGE_URL + " = '" + image_url +"'"+
                " where " + FeedReaderContract.DataList.COLUMN_NAME_ID + " = " + id ;
        db.execSQL(strSQL);


    }
}
