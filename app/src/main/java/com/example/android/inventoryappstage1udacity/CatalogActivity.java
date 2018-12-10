package com.example.android.inventoryappstage1udacity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.android.inventoryappstage1udacity.data.GameInventoryContract.GameInventoryEntry;
import com.example.android.inventoryappstage1udacity.data.GameInventoryDbHelper;

/**
 * Displays list of games that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    // Database helper that will provide us access to the database
    private GameInventoryDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_catalog );

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( CatalogActivity.this, ActivityEditor.class );
                startActivity( intent );
            }
        } );

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        dbHelper = new GameInventoryDbHelper( this );
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM games"
        // to get a Cursor that contains all rows from the books table.
        String[] projection = {
                GameInventoryEntry._ID,
                GameInventoryEntry.COLUMN_GAME_NAME,
                GameInventoryEntry.COLUMN_GAME_PRICE,
                GameInventoryEntry.COLUMN_GAME_QUANTITY,
                GameInventoryEntry.COLUMN_GAME_SUPPLIER_NAME,
                GameInventoryEntry.COLUMN_GAME_SUPPLIER_PHONE_NUMBER
        };
        // Perform a query on the games table
        Cursor cursor = db.query(
                GameInventoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null,
                null );

        TextView displayView = (TextView) findViewById( R.id.text_view_games );

        try {
            displayView.setText( "Inventory contains : " + cursor.getCount() + " products.\n\n" );

            displayView.append(
                    GameInventoryEntry._ID + " | " +
                            GameInventoryEntry.COLUMN_GAME_NAME + " | " +
                            GameInventoryEntry.COLUMN_GAME_PRICE + " | " +
                            GameInventoryEntry.COLUMN_GAME_QUANTITY + " | " +
                            GameInventoryEntry.COLUMN_GAME_SUPPLIER_NAME + " | " +
                            GameInventoryEntry.COLUMN_GAME_SUPPLIER_PHONE_NUMBER + "\n" );

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex( GameInventoryEntry._ID );
            int nameColumnIndex = cursor.getColumnIndex( GameInventoryEntry.COLUMN_GAME_NAME );
            int priceColumnIndex = cursor.getColumnIndex( GameInventoryEntry.COLUMN_GAME_PRICE );
            int quantityColumnIndex = cursor.getColumnIndex( GameInventoryEntry.COLUMN_GAME_QUANTITY );
            int supplierNameColumnIndex = cursor.getColumnIndex( GameInventoryEntry.COLUMN_GAME_SUPPLIER_NAME );
            int supplierPhoneColumnIndex = cursor.getColumnIndex( GameInventoryEntry.COLUMN_GAME_SUPPLIER_PHONE_NUMBER );

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                int currentID = cursor.getInt( idColumnIndex );
                String currentName = cursor.getString( nameColumnIndex );
                int currentPrice = cursor.getInt( priceColumnIndex );
                int currentQuantity = cursor.getInt( quantityColumnIndex );
                int currentSupplierName = cursor.getInt( supplierNameColumnIndex );
                int currentSupplierPhone = cursor.getInt( supplierPhoneColumnIndex );

                // Display the values
                displayView.append( ("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhone) );
            }

        } finally {
            // Close the cursor
            cursor.close();
        }
    }
}
