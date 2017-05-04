package com.hfad.cs63d;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


public class FavoriteListFragment extends ListFragment {
    private static final String TAG = "FavoriteListFragment";
    private static final String LIST_LIMIT = "50";

    private SQLiteDatabase db;
    private Cursor cursor;
    private String term;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "ENTERING FAVORITE DISPLAY");

        super.onCreate(savedInstanceState);
        try {
            SQLiteOpenHelper favoriteDatabaseHelper = new FavoriteDatabaseHelper(this.getContext());
            db = favoriteDatabaseHelper.getReadableDatabase();
            cursor = db.query(
                    true,//select distinct values
                    FavoriteDatabaseHelper.DICTIONARY_TABLE,//table to query
                    new String[]{FavoriteDatabaseHelper.ID_COL, FavoriteDatabaseHelper.TERM_COL},//columns to return
                    null,
                    null,
                    FavoriteDatabaseHelper.FAVORITE_COL,//group by term
                    null,
                    FavoriteDatabaseHelper.ID_COL + " DESC",//specify descending sort order
                    LIST_LIMIT);//set a limit of 50 terms

            Log.d(TAG, "onCreate(): cursor: " + cursor.getCount());

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int row = cursor.getInt(0);
                    String result = cursor.getString(1);
//                    Log.d(TAG, "Term " + result);
//                    Log.d(TAG, "Term row: " + row);
                    cursor.moveToNext();
                }
            }
            Log.d(TAG, "onCreate(): context: " + getContext());
            CursorAdapter cursorAdapter = new SimpleCursorAdapter(getContext(),
                    R.layout.fragment_favorite_list,
                    cursor,
                    new String[]{FavoriteDatabaseHelper.TERM_COL},
                    new int[]{R.id.favorite_text},
                    0);
            Log.d(TAG, "onCreate(): cursorAdapter: " + cursorAdapter);
            setListAdapter(cursorAdapter);
        } catch (SQLiteException e) {
            Toast toast;
            toast = Toast.makeText(this.getContext(), "Database Unavailable", Toast.LENGTH_LONG);
            toast.show();
        }


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d(TAG, "onListItemClick() long id = " + id);

        Toast toast = Toast.makeText(this.getContext(), "testing", Toast.LENGTH_LONG);
        toast.show();

        cursor = db.query(
                FavoriteDatabaseHelper.DICTIONARY_TABLE,
                new String[]{FavoriteDatabaseHelper.FAVORITE_COL},
                FavoriteDatabaseHelper.ID_COL + " = ?",
                new String[] {Long.toString(id)},
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            term = cursor.getString(0);
            Log.v(TAG, "id to term: " + term);
        }
//        resultActivity.doTermSearch(term);
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.content_frame, resultActivity).commit();

        Intent intent = new Intent(getActivity(), ResultActivity.class);
        intent.putExtra(ResultActivity.TERM_ON_CLICK, term);
        getActivity().startActivity(intent);
//        resultActivity.doTermSearch(term);
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.content_frame, resultActivity).commit();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.content_frame, resultActivity).commit();
    }
}
