package com.xxx.mxh.emulatordemo;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.*;

/*
判断时候是模拟器
 */
public class Emulator {
    public static void GotoNotification(Activity mainActivity) {
        Intent intent = new Intent();

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", mainActivity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);

            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");

            intent.putExtra("com.android.settings.ApplicationPkgName", mainActivity.getPackageName());
        }
        mainActivity.startActivity(intent);

    }
    /*
    跳转通知设置界面
     */
    public static void GotoNoti(Activity mainActivity) {
        ApplicationInfo appInfo = mainActivity.getApplicationInfo();
        String pkg = mainActivity.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Intent intent = new Intent();
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Log( "GotoNotify: "+Build.VERSION.SDK_INT);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
                }
                else {
                    //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                    intent.putExtra("app_package", pkg);
                    intent.putExtra("app_uid", uid);
                }
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                //intent.setData(Uri.parse("package:" + mainActivity.getPackageName()));
                intent.setData(Uri.fromParts("package",mainActivity.getPackageName(),null));
            } else {
                intent.setAction(Settings.ACTION_SETTINGS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainActivity.startActivityForResult(intent, 0);

    }

    public static final String TAG = "-----------tools";

    public static Integer GetIsEmulator(Activity mAct){
    	mLog = "";

        Activity mMainActivity = mAct;
        if (mMainActivity == null)
		{
			Log.e(TAG, "GetIsEmulator: mAct is null");
			return 0;
		}

        Context context = mMainActivity.getApplicationContext();

		Log( "GetIsEmulator: Build.FINGERPRINT:"+Build.FINGERPRINT);
        Log( "GetIsEmulator: Build.SERIAL:"+Build.SERIAL);
        Log( "GetIsEmulator: Build.MANUFACTURER:"+Build.MANUFACTURER);
		Log( "GetIsEmulator: Build.HARDWARE:"+Build.HARDWARE);
		Log( "GetIsEmulator: Build.MODEL:"+Build.MODEL);
        Integer ret = 0;
        try {
            if (Build.FINGERPRINT.toLowerCase().contains("generic")) {
                ret = 1;
            } else if (Build.FINGERPRINT.toLowerCase().contains("vbox")) {
                ret = 2;
            } else if (Build.FINGERPRINT.toLowerCase().contains("test-keys")) {
                ret = 3;
            } else if (Build.MODEL.contains("google_sdk")) {
                ret = 4;
            } else if (Build.MODEL.toLowerCase().contains("emulator")) {
                ret = 5;
            } else if (Build.SERIAL.equalsIgnoreCase("unknown")) {
                ret = 6;
            } else if (Build.SERIAL.equalsIgnoreCase("android")) {
                ret = 7;
            } else if (Build.HARDWARE.toLowerCase().contains("goldfish")) {
                ret = 8;
            } else if (Build.MODEL.contains("Android SDK built for x86")) {
                ret = 9;
            } else if (Build.MANUFACTURER.toLowerCase().contains("genymotion")) {
                ret = 10;
            } else if ((Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) || "google_sdk".equals(Build.PRODUCT)) {
                ret = 11;
            } else if (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName().toLowerCase().equals("android")) {
                ret = 12;
            }

            //if(0 == ret)
            {
                String cpuInfo = "";
                try {
                    String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
                    ProcessBuilder cmd = new ProcessBuilder(args);

                    Process process = cmd.start();
                    StringBuffer sb = new StringBuffer();
                    String readLine = "";
                    BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
                    while ((readLine = responseReader.readLine()) != null) {
                        sb.append(readLine);
                    }
                    responseReader.close();
                    cpuInfo = sb.toString().toLowerCase();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if ((cpuInfo.contains("intel") || cpuInfo.contains("amd")))
                {
                    ret = 13;
                }

				Log( "GetIsEmulator: cpu:"+cpuInfo);
			}

            if(0 == ret)
            {
            	/*
                String[] known_files = {"/data/app/com.bluestacks.appmart-1.apk", "/data/app/com.bluestacks.BstCommandProcessor-1.apk",
                        "/data/app/com.bluestacks.help-1.apk", "/data/app/com.bluestacks.home-1.apk", "/data/app/com.bluestacks.s2p-1.apk",
                        "/data/app/com.bluestacks.searchapp-1.apk", "/data/bluestacks.prop", "/data/data/com.androVM.vmconfig",
                        "/data/data/com.bluestacks.accelerometerui", "/data/data/com.bluestacks.appfinder", "/data/data/com.bluestacks.appmart",
                        "/data/data/com.bluestacks.appsettings", "/data/data/com.bluestacks.BstCommandProcessor", "/data/data/com.bluestacks.bstfolder",
                        "/data/data/com.bluestacks.help", "/data/data/com.bluestacks.home", "/data/data/com.bluestacks.s2p", "/data/data/com.bluestacks.searchapp",
                        "/data/data/com.bluestacks.settings", "/data/data/com.bluestacks.setup", "/data/data/com.bluestacks.spotlight", "/mnt/prebundledapps/bluestacks.prop.orig"
                };
                */

				String[] known_files = {
						"/system/lib/libc_malloc_debug_qemu.s",
						"/sys/qemu_trace",
						"/system/bin/qemu-props"
				};

                for (int i = 0; i < known_files.length; i++) {
                    String file_name = known_files[i];
                    File qemu_file = new File(file_name);
                    if (qemu_file.exists()) {
                        ret = 14;
                        Log( "GetIsEmulator: file_name"+file_name);
                    }
                }
            }

            if(0 == ret)
			{
				//网易mumu
				if (Build.MANUFACTURER.toLowerCase().contains("netease"))
				{
					ret = 15;
				}
			}
			
			//if(0 == ret)
			{
				BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

				if(null == bluetoothAdapter || TextUtils.isEmpty(bluetoothAdapter.getName()))
				{
					ret = 16;
					Log( "GetIsEmulator: bluetoothAdapter is null");
				}
				else
				{
					Log( "GetIsEmulator: bluetoothAdapter is:" + bluetoothAdapter.getName());
				}
				
			}


        }
        catch (Exception e){
            e.printStackTrace();
        }

        mLog = 0 == ret ? "不是模拟器" : "是模拟器:"+ret + "\n\n" + mLog;


        return ret;
    }

    private static String mLog = "";
	private static void Log(String log)
	{
		Log.w(TAG, "Log: "+log);

		mLog = mLog + "\n\n" + log;
	}

	public static String GetLog()
	{
		return mLog;
	}
}
