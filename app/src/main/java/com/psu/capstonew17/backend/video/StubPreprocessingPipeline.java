package com.psu.capstonew17.backend.video;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Pair;

import com.psu.capstonew17.backend.api.VideoManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/** Dumb stub preprocessor that just copies data to a local file
 *
 * Created by noahz on 6/6/17.
 */
public class StubPreprocessingPipeline extends PreprocessingPipeline {
    public StubPreprocessingPipeline(Context ctx, File outFile, Uri in, VideoManager.ImportOptions opts) throws IOException {
        super(ctx, outFile, in, opts);
    }

    public void start() throws IOException {
        PreprocessingOperation op = new PreprocessingOperation();
        op.execute(Pair.create(input, output));
    }

    private class PreprocessingOperation extends AsyncTask<Pair<Uri, File>, Pair<Integer, Integer>, Void> {
        @Override
        protected Void doInBackground(Pair<Uri, File>... inputs) {
            Uri in = inputs[0].first;
            File out = inputs[0].second;

            InputStream is;
            FileOutputStream fos;
            try {
                is = ctx.getContentResolver().openInputStream(in);
                fos = new FileOutputStream(out);
            } catch(FileNotFoundException e) {
                cancel(false);
                return null;
            }

            try {
                // copy data
                byte[] buf = new byte[8192*1024];
                int length;
                while((length = is.read(buf)) > 0) {
                    fos.write(buf, 0, length);
                }

                // close the files
                is.close();
                fos.close();
            } catch(IOException e) {
                cancel(false);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Pair<Integer, Integer>... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(listener != null) {
                listener.onCompleted();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if(listener != null) {
                listener.onFailed();
            }
        }
    }
}
