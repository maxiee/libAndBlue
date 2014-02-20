package com.maxiee.libandblue;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.ArrayAdapter;

public class libAndBlue {
	private Activity context;
	private BluetoothAdapter btAdapter;
	private ArrayAdapter<String> foundDevices;
	public ServerThread serverThread = null;

	public libAndBlue(Activity context) {
		this.context = context;
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		//搜到的蓝牙设备
		foundDevices = new ArrayAdapter<String>(context,android.R.layout.simple_expandable_list_item_1);
		serverThread = new ServerThread();
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
	//服务器端线程类
	public class ServerThread extends Thread {
		private final BluetoothServerSocket mmServerSocket;
		public ServerThread() {
			BluetoothServerSocket tmp = null;
			try {
				tmp = btAdapter.listenUsingRfcommWithServiceRecord("maxiee",
						UUID.fromString("C72E6D27-6AFC-075C-BC4E-01A4328059F4"));
			} catch (IOException e) { }
			mmServerSocket = tmp;
		}
		public void run() {
			BluetoothSocket socket = null;
			InputStream inputStream = null;
			final byte[] buffer = new byte[1024];
			// Keep listening until exception occurs or a socket is returned
			while (true) {
				try {
					Log.d("maxiee", "11111111");
					socket = mmServerSocket.accept();
					Log.d("maxiee", "222222222");
				} catch (IOException e) {
					break;
				}
				if (socket != null) {
					// Do work to manage the connection (in a separate thread)
					//测试功能
					try {
						inputStream = socket.getInputStream();
						inputStream.read(buffer);
						mmServerSocket.close();
					} catch (IOException e1) {}
					Thread serverDataHandle = new ServerDataHandle(buffer);
					serverDataHandle.run();
					break;
				}
			}
		}
		public void cancel() {
			try {
				mmServerSocket.close();
			} catch (IOException e) { }
		}
	}
	//服务器处理数据线程
	private class ServerDataHandle extends Thread {
		private String str = null;
		public ServerDataHandle(byte[] buffer){
			try {
				str = new String(buffer,"UTF-8");
			} catch (UnsupportedEncodingException e) {}
		}
		public void run() {
			Log.d("maxiee", str);
		}
	}
}
