package com.maxiee.libandblue;

import java.lang.reflect.Method;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ArrayAdapter;

public class libAndBlue {
	private Activity context;
	private BluetoothAdapter btAdapter;
	private ArrayAdapter<String> foundDevices;

	public libAndBlue(Activity context) {
		this.context = context;
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		//搜到的蓝牙设备
		foundDevices = new ArrayAdapter<String>(context,android.R.layout.simple_expandable_list_item_1);
	}

	public boolean enable() {
		return btAdapter.enable();
	}

	public boolean disable() {
		return btAdapter.disable();
	}

	public boolean discoverable(int duration) {
		Intent discoverableIntent = new Intent(
				BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,duration);
		context.startActivity(discoverableIntent);
		return true;
	}

	public ArrayAdapter<String> getBondDevices() {
		Set<BluetoothDevice> pairedDevices  = btAdapter.getBondedDevices();
		ArrayAdapter<String> bondDevices = new ArrayAdapter<String>(context,android.R.layout.simple_expandable_list_item_1);
		if (pairedDevices.size() > 0) {
		    for (BluetoothDevice device : pairedDevices) {
		    	bondDevices.add(device.getName() + '\n' + device.getAddress());
		    }
		}
		return bondDevices;
	}
	
	public boolean discovery() {
		foundDevices.clear();
		return btAdapter.startDiscovery();
	}
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            	foundDevices.add(device.getName()+'\n'+ device.getAddress());
	        }
	    }
	};
	
	public void registerBroadCast() {
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		context.registerReceiver(mReceiver, filter);
	}
	
	public ArrayAdapter<String> discoveryResult(){
		return foundDevices;
	}
	
	public boolean setBond(BluetoothDevice remoteDevice) throws Exception {
		if (remoteDevice.getBondState() == BluetoothDevice.BOND_NONE) {
			Method createBond = BluetoothDevice.class.getMethod("createBond");
			return (Boolean) createBond.invoke(remoteDevice);
		}
		return false;
	}
	
	public boolean removeBond(BluetoothDevice remoteDevice) throws Exception {
		if (remoteDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
			Method createBond = BluetoothDevice.class.getMethod("removeBond");
			return (Boolean) createBond.invoke(remoteDevice);
		}
		return false;
	}
	
	public boolean isEnable() {
		if(btAdapter.isEnabled())
			return true;
		else
			return false;
	}
}
