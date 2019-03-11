package com.example.demo.Database;

import android.provider.BaseColumns;

public class FeedReaderContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {
    }

    /* Inner class that defines the table contents */


    public static abstract class DataList implements BaseColumns {
        public static final String TABLE_NAME = "data_list";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_IMAGE_URL = "image";
        public static final String COLUMN_NAME_AGE = "age";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_EMAIL = "email";


    }
}
