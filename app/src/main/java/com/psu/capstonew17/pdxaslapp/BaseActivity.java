//MIT License Copyright 2017 PSU ASL Capstone Team
package com.psu.capstonew17.pdxaslapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This activity implements behavior of drop down menu
 * and actions of item selected
 */
public class BaseActivity extends AppCompatActivity {
    private static final int REQ_EXT_STORAGE_PERMS   = 1;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        permChck();
        return true;
    }

    //external storage perm check
    protected void permChck() {
        if (!(PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQ_EXT_STORAGE_PERMS);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        permChck();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] perms, @NonNull int[] grantResults){
        switch(requestCode) {
            //check for external storage perms
            case REQ_EXT_STORAGE_PERMS:
                if (grantResults.length > 0 && !(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //if the user refuses external storage perms then trap them! muahahahaha!
                    //(okay, not really, but don't let them continue to try to use the app)
                    Intent intent = new Intent(this, NoExtrnlStrgPrmsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.item_sub_menu_choose_quiz:
                intent = new Intent(this, TakeQuizSubMenuActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.item_create_card:
                intent = new Intent(this, CreateCardActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.item_delete_card:
                intent = new Intent(this, DeleteCardActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.item_ced_decks:
                intent = new Intent(this, CreateEditDeleteDeckActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.item_receive:
                intent = new Intent(this, ReceiveDeckActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.item_share:
                intent = new Intent(this, ShareDeckActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
