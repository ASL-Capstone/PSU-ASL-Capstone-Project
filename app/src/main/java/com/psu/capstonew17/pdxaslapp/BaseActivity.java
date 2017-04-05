package com.psu.capstonew17.pdxaslapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by thanhhoang on 4/4/17.
 */

/**
 * This activity implements behavior of drop down menu
 * and actions of item selected
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_multiple_choice:

                Intent intent = new Intent(this, TakeQuizSubMenuActivity.class);
                startActivity(intent);

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
