/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.nsdchatclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    public static final String TAG = "NsdChat";

    NsdHelper mNsdHelper;
    List<NsdServiceInfo> mDeviceList = new ArrayList<>();
    private DeviceListAdapter mDeviceListAdapter = null;
    ProgressDialog mProgressDialog;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListView deviceListView = (ListView) findViewById(R.id.deviceList);

        mDeviceListAdapter = new DeviceListAdapter(this, mDeviceList);
        deviceListView.setAdapter(mDeviceListAdapter);
        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v(TAG, "position: " + position);
                NsdServiceInfo serviceInfo = mDeviceListAdapter.getItem(position);
                Log.v(TAG, "Selected service: " + serviceInfo.getServiceName());
                mNsdHelper.deviceSelected(serviceInfo);
                mProgressDialog = new ProgressDialog(MainActivity.this);
                mProgressDialog.setCancelable(true);
                mProgressDialog.setMessage("Loading ....");
                mProgressDialog.show();
            }
        });

        mNsdHelper = new NsdHelper(this, this);
        mNsdHelper.initializeNsd();
        mNsdHelper.discoverServices();
    }

    public void onResolved(final NsdServiceInfo service) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
                Intent intent = new Intent(MainActivity.this, ControlActivity.class);
                intent.putExtra("host", service.getHost().getHostAddress());
                intent.putExtra("port", service.getPort());
                startActivity(intent);
            }
        });
    }

    public void onDeviceDiscovered(final NsdServiceInfo serviceInfo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!hasService(serviceInfo)) {
                    mDeviceList.add(serviceInfo);
                    mDeviceListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
//        mNsdHelper.tearDown();
        super.onDestroy();
    }

    private boolean hasService(NsdServiceInfo serviceInfo) {
        for (NsdServiceInfo nsdServiceInfo : mDeviceList) {
            if (nsdServiceInfo.getServiceName().equals(serviceInfo.getServiceName())) {
                return true;
            }
        }
        return false;
    }
}
