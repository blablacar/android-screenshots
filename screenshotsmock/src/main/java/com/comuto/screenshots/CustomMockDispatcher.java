package com.comuto.screenshots;

import android.content.Context;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.cheikhna on 15/02/2016.
 */
public class CustomMockDispatcher extends Dispatcher {

    private final String jsonProperties;
    private final String dataPropertiesFile;
    private final AssetsProperties assetsProperties;
    private final AssetsProperties dataProperties;

    private final List<String> mocks;

    public CustomMockDispatcher(Context context, String locale, String jsonPropertiesFile, String dataPropertiesFile) {
        this.jsonProperties = jsonPropertiesFile;
        this.dataPropertiesFile = dataPropertiesFile;
        this.assetsProperties = new AssetsProperties(context, jsonPropertiesFile);
        this.dataProperties = new AssetsProperties(context, dataPropertiesFile);

        this.mocks = new ArrayList<>();

    }

    @Override
    public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        final String path = request.getPath();


        return null;
    }
}
