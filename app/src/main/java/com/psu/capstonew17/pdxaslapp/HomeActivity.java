package com.psu.capstonew17.pdxaslapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_multiple_choice:
                break;

            case R.id.item_flash_cards:
                break;

            case R.id.item_write_up:
                break;

            case R.id.item_create_card:
                break;

            case R.id.item_delete_card:
                break;

            case R.id.item_ced_decks:
                break;

            case R.id.item_share:
                break;

            case R.id.item_receive:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
