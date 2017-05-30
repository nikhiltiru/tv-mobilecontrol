package com.example.android.nsdchatclient;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class DeviceListAdapter.
 */
public class DeviceListAdapter extends ArrayAdapter<NsdServiceInfo> {

    // Declare Variables
    /** The context. */
    private Context mContext;

    /** The device list. */
    private ArrayList<NsdServiceInfo> mDeviceList = new ArrayList<>();

    /** The inflater. */
    private LayoutInflater mInflater;

    /** The selected position. */
    private int mSelectedPosition = -1;

    /** The selection color. */
//    private int selectionColor = 0;

    /**
     * Instantiates a new device list adapter.
     *
     * @param context the context
     * @param deviceList the connected devices list
     */
    public DeviceListAdapter(Context context, List<NsdServiceInfo> deviceList) {
        super(context, 0, deviceList);
        mContext = context;
        mDeviceList.addAll(deviceList);
        mSelectedPosition = -1;
//        selectionColor = context.getResources().getColor(R.color.selected_bg_color, null);
    }

    /**
     * Gets the view.
     *
     * @param position the position
     * @param convertView the convert view
     * @param parent the parent
     * @return the view
     * @see android.widget.Adapter#getView(int, View, ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View localView = convertView;
        if (localView == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            localView = mInflater.inflate(R.layout.device_list_item, parent, false);
            holder = new ViewHolder();
            holder.text1 = (TextView) localView.findViewById(R.id.device_name);
            localView.setTag(holder);
        } else {
            holder = (ViewHolder) localView.getTag();
        }

        NsdServiceInfo deviceName = getItem(position);
        String displayName = deviceName.getServiceName();
        displayName = displayName.replace("Nsd-DPAD-", "");
        holder.text1.setText(displayName);

//        if (position == mSelectedPosition) {
//            localView.setBackgroundColor(selectionColor);
//        } else {
//            localView.setBackgroundColor(Color.TRANSPARENT);
//        }

        return localView;
    }

    /**
     * The Class ViewHolder.
     */
    static class ViewHolder {

        /** The Device name. */
        private TextView text1;

    }
}
