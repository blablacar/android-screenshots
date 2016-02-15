package com.comuto.screenshots;

import android.os.AsyncTask;

import com.squareup.okhttp.mockwebserver.MockWebServer;

/**
 * An {@link AsyncTask} to start {@link MockWebServer}.
 */
public class MockServerTask extends AsyncTask<Void, Void, Void> {

    private Callback mCallback;

    @Override
    protected Void doInBackground(Void... voids) {
        mCallback.execute();
        return null;
    }

    public void run(Callback callback) {
        mCallback = callback;
        this.execute();
    }
}
