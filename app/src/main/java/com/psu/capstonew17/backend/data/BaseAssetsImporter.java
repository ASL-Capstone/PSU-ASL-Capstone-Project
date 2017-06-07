package com.psu.capstonew17.backend.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.CardManager;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.ObjectAlreadyExistsException;
import com.psu.capstonew17.backend.api.Video;
import com.psu.capstonew17.backend.db.AslDbContract;
import com.psu.capstonew17.backend.db.AslDbHelper;
import com.psu.capstonew17.pdxaslapp.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class BaseAssetsImporter {

    /**
     * the string prefix that denotes a raw file is a video to create a card from
     */
    private static final String RAW_PREFIX = "_card_";

    /**
     * the string that separates card names and deck names in raw files
     */
    private static final String RAW_DELIMITER = "_indeck_";

    /**
     * the character to replace in raw file names with a space
     */
    private static final char RAW_SPACER = '_';

    /**
     * string pattern to replace with a question mark
     */
    private static final String RAW_QUESTION_PATTERN = "__question__";

    public static void importAssets(Context context){
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA256");
        } catch(NoSuchAlgorithmException e) {
            Log.d("BaseAssetsImporter", "System does not support SHA256 hash", e);
            return;
        }
        CardManager cardManager = ExternalCardManager.getInstance(context);
        ExternalDeckManager deckManager = (ExternalDeckManager) ExternalDeckManager.getInstance(context);
        AslDbHelper dbHelper = AslDbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Field[] fields = R.raw.class.getDeclaredFields();
        for(Field f : fields){
            String rName = f.getName();
            if(rName.startsWith(RAW_PREFIX)) {
                File rFile = new File("android.resource://" + context.getPackageName() + "/raw/" + rName);
                String parsedName = rName.replaceAll(RAW_QUESTION_PATTERN, "?");
                String cardAndDeckNames = parsedName.substring(RAW_PREFIX.length(), parsedName.length());
                String cardName = cardAndDeckNames.split(RAW_DELIMITER, 2)[0];
                cardName = cardName.replace(RAW_SPACER, ' ');
                String deckName = cardAndDeckNames.split(RAW_DELIMITER, 2)[1];
                deckName = deckName.replace(RAW_SPACER, ' ');
                InputStream input = context.getResources().openRawResource(
                        context.getResources().getIdentifier(rName, "raw", context.getPackageName()));
                byte [] data = new byte[8192];
                int len;
                try {
                    while((len = input.read(data)) != -1){
                        digest.update(data, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                byte [] sha = digest.digest();
                // check if video already exists
                String query = dbHelper.buildSelectQuery(
                        AslDbContract.VideoEntry.TABLE_NAME,
                        Arrays.asList(AslDbContract.VideoEntry.COLUMN_SHA + "='" + Base64.encodeToString(sha, Base64.DEFAULT) + "'")
                );
                Video video;
                Cursor cursor = db.rawQuery(query, null);
                if(cursor.moveToFirst()){
                    int videoId = cursor.getInt(cursor.getColumnIndex(AslDbContract.VideoEntry.COLUMN_ID));
                    String videoPath = cursor.getString(cursor.getColumnIndex(AslDbContract.VideoEntry.COLUMN_PATH));
                    File existingVideo = new File(videoPath);
                    video = new ExternalVideo(videoId, existingVideo.getAbsoluteFile());
                }
                else{
                    // create the new video
                    ContentValues values = new ContentValues();
                    values.put(AslDbContract.VideoEntry.COLUMN_PATH, rFile.getAbsolutePath());
                    values.put(AslDbContract.VideoEntry.COLUMN_SHA, Base64.encodeToString(sha, Base64.DEFAULT));
                    int videoId = (int) db.insert(AslDbContract.VideoEntry.TABLE_NAME, null, values);
                    video = new ExternalVideo(videoId, rFile);
                }
                try{
                    Card card = cardManager.buildCard(video, cardName);
                    if(deckManager.deckExists(deckName)){
                        Deck deck = deckManager.getDeck(deckName);
                        List<Card> cards = deck.getCards();
                        cards.add(card);
                        deck.commit();
                    }
                    else{
                        List<Card> cards = Arrays.asList(card);
                        deckManager.buildDeck(deckName, cards);
                    }
                } catch (ObjectAlreadyExistsException e) {
                    Log.d("BaseAssetsImporter", "Card '" + cardName + "' already exists. Skipping import.");
                }
            }
        }

    }
}
