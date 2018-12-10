package com.example.android.inventoryappstage1udacity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.android.inventoryappstage1udacity.data.GameInventoryContract.GameInventoryEntry;
import com.example.android.inventoryappstage1udacity.data.GameInventoryDbHelper;

/**
 * Allows user to create a new game or edit an existing one.
 */
public class ActivityEditor extends AppCompatActivity {

    public GameInventoryDbHelper dbHelper;

    // EditText field to enter the games's name
    private EditText gameNameEditText;

    // EditText field to enter the game's price
    private EditText gamePriceEditText;

    // EditText field to enter the game's quantity
    private EditText gameQuantityEditText;

    // EditText field to enter the game's supplier name
    private Spinner supplierNameSpinner;

    // EditText field to enter the game's supplier contact
    private EditText supplierContactEditText;

    // Supplier of the game
    private int supplierName = GameInventoryEntry.SUPPLIER_UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_editor );

        // Find all relevant views that we will need to read user input from
        gameNameEditText = findViewById( R.id.product_name );
        gamePriceEditText = findViewById( R.id.poduct_price );
        gameQuantityEditText = findViewById( R.id.product_quantity );
        supplierNameSpinner = findViewById( R.id.product_supplier_name_spinner );
        supplierContactEditText = findViewById( R.id.supplier_contact );
        dbHelper = new GameInventoryDbHelper( this );
        setupSpinner();
    }

    // Setup the dropdown spinner that allows the user to select the supplier's name of the game.
    private void setupSpinner() {
        // Create adapter for spinner.
        ArrayAdapter gameSupplieNameSpinnerAdapter = ArrayAdapter.createFromResource( this,
                R.array.array_supplier_options, android.R.layout.simple_spinner_item );

        gameSupplieNameSpinnerAdapter.setDropDownViewResource( android.R.layout.simple_dropdown_item_1line );

        supplierNameSpinner.setAdapter( gameSupplieNameSpinnerAdapter );

        supplierNameSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition( position );
                if (!TextUtils.isEmpty( selection )) {
                    if (selection.equals( getString( R.string.supplier_sony ) )) {
                        supplierName = GameInventoryEntry.SUPPLIER_SONY_GAME_ELECTRONICS;
                    } else if (selection.equals( getString( R.string.supplier_gamers ) )) {
                        supplierName = GameInventoryEntry.SUPPLIER_GAMERS_OFFLINE;
                    } else if (selection.equals( getString( R.string.supplier_players ) )) {
                        supplierName = GameInventoryEntry.SUPPLIER_PLAYERS_UNKNOWN_SELLER;
                    } else {
                        supplierName = GameInventoryEntry.SUPPLIER_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                supplierName = GameInventoryEntry.SUPPLIER_UNKNOWN;
            }
        } );

    }

    //Get user input from editor and save new game info into database.
    private void insertGame() {
        String gameName;
        if (TextUtils.isEmpty( gameNameEditText.getText() )) {
            gameNameEditText.setError( getString( R.string.required_field ) );
            return;
        } else {
            gameName = gameNameEditText.getText().toString().trim();
        }

        String gamePrice;
        if (TextUtils.isEmpty( gamePriceEditText.getText() )) {
            gamePriceEditText.setError( getString( R.string.required_field ) );
            return;
        } else {
            gamePrice = gamePriceEditText.getText().toString().trim();
        }

        String gameQuantity;
        if (TextUtils.isEmpty( gameQuantityEditText.getText() )) {
            gameQuantityEditText.setError( getString( R.string.required_field ) );
            return;
        } else {
            gameQuantity = gameQuantityEditText.getText().toString().trim();
        }

        String supplierContact;
        if (TextUtils.isEmpty( supplierContactEditText.getText() )) {
            supplierContactEditText.setError( getString( R.string.required_field ) );
            return;
        } else {
            supplierContact = supplierContactEditText.getText().toString().trim();
        }

        int gamePriceInt = Integer.parseInt( gamePrice );
        if (gamePriceInt < 0) {
            gamePriceEditText.setError( getString( R.string.price_cannot_be_negative ) );
            Toast.makeText( this, getString( R.string.price_cannot_be_negative ), Toast.LENGTH_SHORT ).show();
            return;
        }
        int gameQuantityInt = Integer.parseInt( gameQuantity );
        if (gameQuantityInt < 0) {
            gameQuantityEditText.setError( getString( R.string.quantity_cannot_be_negative ) );
            Toast.makeText( this, getString( R.string.quantity_cannot_be_negative ), Toast.LENGTH_SHORT ).show();
            return;
        }

        // Gets the database in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and game attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put( GameInventoryEntry.COLUMN_GAME_NAME, gameName );
        values.put( GameInventoryEntry.COLUMN_GAME_PRICE, gamePriceInt );
        values.put( GameInventoryEntry.COLUMN_GAME_QUANTITY, gameQuantityInt );
        values.put( GameInventoryEntry.COLUMN_GAME_SUPPLIER_NAME, supplierName );
        values.put( GameInventoryEntry.COLUMN_GAME_SUPPLIER_PHONE_NUMBER, supplierContact );

        // Insert a new row for game in the database.
        long newRowId = db.insert( GameInventoryEntry.TABLE_NAME, null, values );

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            Toast.makeText( this, getString( R.string.error_saving_games ), Toast.LENGTH_SHORT ).show();
        } else {
            Toast.makeText( this, getString( R.string.game_saved ) + " " + newRowId, Toast.LENGTH_SHORT ).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate( R.menu.menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                insertGame();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask( this );
                return true;
        }
        return super.onOptionsItemSelected( item );
    }
}

