package com.example.android.inventoryappstage1udacity.data;

import android.provider.BaseColumns;

public class GameInventoryContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private GameInventoryContract() {
    }

    /**
     * Inner class that defines constant values for the games database table.
     */
    public static abstract class GameInventoryEntry implements BaseColumns {
        /**
         * Name of database table for games
         */
        public final static String TABLE_NAME = "games";
        /**
         * Unique ID number for the game (only for use in the database table).
         */
        public final static String _ID = BaseColumns._ID;
        /**
         * Name of the Game.
         */
        public final static String COLUMN_GAME_NAME = "product_name";
        /**
         * Price of the Game.
         */
        public final static String COLUMN_GAME_PRICE = "price";
        /**
         * Quantity of the Game.
         */
        public final static String COLUMN_GAME_QUANTITY = "quantity";
        /**
         * Game supplier name of the Game.
         */
        public final static String COLUMN_GAME_SUPPLIER_NAME = "supplier_name";
        /**
         * Game supplier phone number of the Game.
         */
        public final static String COLUMN_GAME_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";

        // Supplier name list values
        public final static int SUPPLIER_UNKNOWN = 0;
        public final static int SUPPLIER_SONY_GAME_ELECTRONICS = 1;
        public final static int SUPPLIER_GAMERS_OFFLINE = 2;
        public final static int SUPPLIER_PLAYERS_UNKNOWN_SELLER = 3;

        private GameInventoryEntry() {

        }
    }
}