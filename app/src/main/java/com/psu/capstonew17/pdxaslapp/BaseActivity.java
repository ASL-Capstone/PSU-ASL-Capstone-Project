package com.psu.capstonew17.pdxaslapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
        Intent intent;
        switch (item.getItemId()) {
            case R.id.item_multiple_choice:
                intent = new Intent(this, MultipleChoiceActivity.class);
                startActivity(intent);
                break;

            case R.id.item_flash_cards:
                intent = new Intent(this, FlashCardActivity.class);
                startActivity(intent);
                break;

            case R.id.item_write_up:
                intent = new Intent(this, WriteUpActivity.class);
                startActivity(intent);
                break;

            case R.id.item_create_card:
                intent = new Intent(this, CreateCardActivity.class);
                startActivity(intent);
                break;

            case R.id.item_delete_card:
                intent = new Intent(this, DeleteCardActivity.class);
                startActivity(intent);
                break;

            case R.id.item_ced_decks:
                intent = new Intent(this, CreateEditDeleteDeckActivity.class);
                startActivity(intent);
                break;

            case R.id.item_share:
                intent = new Intent(this, ReceiveDeckActivity.class);
                startActivity(intent);
                break;

            case R.id.item_receive:
                intent = new Intent(this, ShareDeckActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
