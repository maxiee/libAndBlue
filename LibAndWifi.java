package com.maxiee.libAndWifi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class LibAndWifi {
	//定义WifiManager对象
	private WifiManager wifiManager;
	//定义WifiInfo对象
	private WifiInfo wifiInfo;
	//wifi扫描结果保存
	private List<ScanResult> wifiList;
	
	public LibAndWifi(Context context){
		wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		wifiInfo = wifiManager.getConnectionInfo();
	}
	
	//打开WIFI
	public void OpenWifi()
	{
		if (!wifiManager.isWifiEnabled())
		{
			wifiManager.setWifiEnabled(true);
			
		}
	}
	
	//关闭WIFI
	public void CloseWifi()
	{
		if (wifiManager.isWifiEnabled())
		{
			wifiManager.setWifiEnabled(false);	
		}
	}
	
	//搜索WIFI
	public List<String> StartScan()
	{
		if(!(wifiList==null))
			wifiList.clear();
		wifiManager.startScan();
		//得到扫描结果
		wifiList = wifiManager.getScanResults();
		List<String> result = new ArrayList<String>();
		for(ScanResult sr: wifiList){
			result.add("\nSSID：" + sr.SSID + "\n" 
					+ "信号强度：" + sr.level + "\n" 
					+ "加密方式：" + sr.capabilities +"\n");
		}
		return result;
	}
	
	public Map<String, String> getSelectedWifiInfo(int which){
		return null;
	}
}
