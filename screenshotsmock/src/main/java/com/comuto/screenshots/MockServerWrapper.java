package com.comuto.screenshots;

import android.util.Log;

import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import java.io.IOException;

/**
 * A wrapper for MockWebServer.
 */
public class MockServerWrapper {
    private static final String TAG = "MockServerWrapper";
    public static final int MOCK_SERVER_PORT = 54828;

    private MockWebServer mServer;
    private String mUrl;

    public void start(final ParameterizedCallback changeUrlCallback, final MockResponse... mockResponses) {
        new MockServerTask().run(new Callback() {
            @Override
            public void execute() {
                mServer = new MockWebServer();
                for (MockResponse mockResponse : mockResponses) {
                    mServer.enqueue(mockResponse);
                }
                try {
                    mServer.start(MOCK_SERVER_PORT); 
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mServer != null) {
                    mUrl = mServer.url("").url().toString().replaceAll("/$", "");
                    Log.i(TAG, "MockServer URL: " + mUrl);
                    changeUrlCallback.execute(mUrl);
                }
            }
        });
    }

    public void start(final ParameterizedCallback changeUrlCallback, final Dispatcher dispatcher) {
        new MockServerTask().run(new Callback() {
            @Override
            public void execute() {
                mServer = new MockWebServer();
                mServer.setDispatcher(dispatcher);
                try {
                    mServer.start(MOCK_SERVER_PORT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mServer != null) {
                    Log.e("LoginTest", "port = " + mServer.getPort());
                    mUrl = mServer.url("").url().toString().replaceAll("/$", "");
                    Log.i(TAG, "MockServer URL: " + mUrl);
                    changeUrlCallback.execute(mUrl);
                }
            }
        });
    }

    public void start(ParameterizedCallback changeUrlCallback, final String... mockResponsesBodies) {
        int amount = mockResponsesBodies.length;
        MockResponse mockResponses[] = new MockResponse[amount];
        for (int i = 0; i < amount; i++) {
            mockResponses[i] = new MockResponse().setBody(mockResponsesBodies[i]);
        }
        start(changeUrlCallback, mockResponses);
    }

    public void stop() {
        if (mServer != null) {
            new MockServerTask().run(new Callback() {
                @Override
                public void execute() {
                    try {
                        mServer.shutdown();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
