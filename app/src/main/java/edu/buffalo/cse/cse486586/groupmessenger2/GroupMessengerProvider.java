package edu.buffalo.cse.cse486586.groupmessenger2;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObservable;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static edu.buffalo.cse.cse486586.groupmessenger2.GroupMessengerActivity.TAG;

/**
 * GroupMessengerProvider is a key-value table. Once again, please note that we do not implement
 * full support for SQL as a usual ContentProvider does. We re-purpose ContentProvider's interface
 * to use it as a key-value table.
 * 
 * Please read:
 * 
 * http://developer.android.com/guide/topics/providers/content-providers.html
 * http://developer.android.com/reference/android/content/ContentProvider.html
 * 
 * before you start to get yourself familiarized with ContentProvider.
 * 
 * There are two methods you need to implement---insert() and query(). Others are optional and
 * will not be tested.
 * 
 * @author stevko
 *
 */
public class GroupMessengerProvider extends ContentProvider {

    private static int keyHolder = 0;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // You do not need to implement this.
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        String fileName = values.get("key").toString();
        String fileContents = values.get("value").toString();

//        Log.i("filname" , fileName);
//        Log.i("data", fileContents);

        FileOutputStream outputStream = null;
        // file write reference : https://developer.android.com/training/data-storage/app-specific#java
        try {
            outputStream = getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        }
         catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }finally {
            keyHolder++;
        }

        /*
         * TODO: You need to implement this method. Note that values will have two columns (a key
         * column and a value column) and one row that contains the actual (key, value) pair to be
         * inserted.
         * 
         * For actual storage, you can use any option. If you know how to use SQL, then you can use
         * SQLite. But this is not a requirement. You can use other storage options, such as the
         * internal storage option that we used in PA1. If you want to use that option, please
         * take a look at the code for PA1.
         */
//        Log.v("insert", values.toString());
        return uri;
    }

    @Override
    public boolean onCreate() {
        // If you need to perform any one-time initialization task, please do it here.
        return false;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        //fileRead reference : https://developer.android.com/training/data-storage/app-specific#java
        MatrixCursor matrixCursor = new MatrixCursor(new String[] {"key", "value"});
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try
        {
            fileInputStream = getContext().openFileInput(selection); //selection gives the file name
            inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            reader = new BufferedReader(inputStreamReader);

            String readLine = reader.readLine();
//            Log.i("fileContent", String.valueOf(readLine));
            matrixCursor.addRow(new String[] {selection, readLine});

            reader.close();
            inputStreamReader.close();
            fileInputStream.close();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }



//        Log.v("query", selection);
        return matrixCursor;

        /*
         * TODO: You need to implement this method. Note that you need to return a Cursor object
         * with the right format. If the formatting is not correct, then it is not going to work.
         *
         * If you use SQLite, whatever is returned from SQLite is a Cursor object. However, you
         * still need to be careful because the formatting might still be incorrect.
         *
         * If you use a file storage option, then it is your job to build a Cursor * object. I
         * recommend building a MatrixCursor described at:
         * http://developer.android.com/reference/android/database/MatrixCursor.html
         */
//        Log.v("query", selection);
//        return null;
    }
}
