/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
*/
package com.nearbytes.sdk.phonegap;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nearbytes.sdk.NearBytes;
import com.nearbytes.sdk.NearBytes.NBException;

import android.content.BroadcastReceiver;
import android.util.Log;

/** This class helps you to use the NearBytes SDK on Phonegap.
 * @author Icaro F. Martins <icaro@kinetics.com.br>
 * @version 1.0 */
public class NearBytesPhonegap extends CordovaPlugin implements NearBytes.NearBytesListener {
    public static final String TAG = "NearBytesPhonegap";

    public static String cordovaVersion = "dev";              // Cordova version
    public static String platform = "android";                  // Device OS
    private static NearBytes NBY = null;
    private CallbackContext mNBListiner=null;
    private static boolean DEV_MODE = false;
        
    BroadcastReceiver telephonyReceiver = null;

    /** Constructor. */
    public NearBytesPhonegap() {}

    /** Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in. */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        //this.initTelephonyReceiver();
    }

    /** Executes the request and returns PluginResult.
     * @param action            The action to execute.
     * @param args              JSONArry of arguments for the plugin.
     * @param callbackContext   The callback id used when calling back into JavaScript.
     * @return                  True if the action was valid, false if not. */
    public boolean execute(String action,final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    	if (action.equals("create") ){
    		if( (NBY=NearBytes.shared())!=null ){
    			debug("already exists an instance!");
    			callbackContext.success();
    			return true;
    		}
		    debug("create");
		    int appid=NearBytes.DEFAULT_APPID_TEST;
		    String appkey="";
		    if( args!=null && args.length() >= 1 ){
		    	try{
		    		JSONObject obj=args.getJSONObject(0);
		    		appid=obj.getInt("appid");
		    		appkey=obj.getString("appkey");
		    	}catch (Exception e) {e.printStackTrace();}
		    }
		    try{
		    	NBY = new NearBytes(NearBytesPhonegap.this.cordova.getActivity(),appid,appkey);
		    	NBY.setNearBytesListener(NearBytesPhonegap.this);
		    	callbackContext.success();
		    }catch(NBException e){
		    	Log.e(TAG,"NearBytes Exception, "+e.getMessage());
		    	callbackContext.error("NearBytes Exception, "+e.getMessage());
		    }
    	} else if(action.equals("debugMode")){
			if(NearBytes.shared()!=null)
				RunOnMainThread(new Runnable() {
        			@Override
        			public void run() {
        				NearBytes.shared().debugMode();
        			}});
    	} else if(action.equals("playDemo")){
    		if(NearBytes.shared()!=null)
    			RunOnMainThread(new Runnable() {
        			@Override
        			public void run() {
        				NearBytes.shared().send(NearBytes.stringToBytes("demonstration text!"));
        			}});
    	} else if(action.equals("setNearBytesListener")){
    		mNBListiner=callbackContext;
    	} else if(action.equals("startListening") ){
    		debug("startListening");
    		if(NearBytes.shared()!=null)
    			RunOnMainThread(new Runnable() {
        			@Override
        			public void run() {
        				NearBytes.shared().startListening();
    			}});
    	} else if(action.equals("stopListening") ){
    		debug("stopListening");
    		if(NearBytes.shared()!=null)
    			RunOnMainThread(new Runnable() {
        			@Override
        			public void run() {
        				NearBytes.shared().stopListening();
        			}});
    	} else if(action.equals("setAskTimeout")){
    		debug("setAskTimeout");
    		if(NearBytes.shared()!=null){
    			final int value = args.getInt(0);
    			RunOnMainThread(new Runnable() {
        			@Override
        			public void run() {
        				NearBytes.shared().setAskTimeout(value);
        			}});
    		}
    	} else if(action.equals("setTimeout")){
    		debug("setTimeout");
    		if(NearBytes.shared()!=null){
    			final int value = args.getInt(0);
    			RunOnMainThread(new Runnable() {
        			@Override
        			public void run() {
        				NearBytes.shared().setTimeout(value);
        			}});
    		}
    	} else if(action.equals("setRetries")){
    		debug("setRetries");
    		if(NearBytes.shared()!=null){
    			final int value = args.getInt(0);
    			RunOnMainThread(new Runnable() {
        			@Override
        			public void run() {
        				NearBytes.shared().setRetries(value);
        			}});
    		}
    	} else if(action.equals("setPlayMode")){
    		debug("setPlayMode");
    		if(NearBytes.shared()!=null){
    			final int value = args.getInt(0);
    			RunOnMainThread(new Runnable() {
        			@Override
        			public void run() {
        				NearBytes.shared().setPlayMode(value);
        			}});
    		}
    	} else if(action.equals("send")){
			if(NearBytes.shared()!=null)
				RunOnMainThread(new Runnable() {
	    			@Override
	    			public void run() {
	    				NearBytes.shared().send( getBytesFromArray(args,0) );
	    			}});
    	} else if(action.equals("ask")){
    		if(NearBytes.shared()!=null)
    			RunOnMainThread(new Runnable() {
        			@Override
        			public void run() {
        				NearBytes.shared().ask( getBytesFromArray(args,0) );
        			}});
    	} else if(action.equals("answer")){
    		//debug("answer:"+getBoolFromArray(args,1));
    		if(NearBytes.shared()!=null)
    			RunOnMainThread(new Runnable() {
        			@Override
        			public void run() {
        				NearBytes.shared().answer( getBytesFromArray(args,0), getBoolFromArray(args,1) );
        			}});
    	}
    	
    	// conv
    	else if(action.equals("stringToBytes") ){
    		final String value = args.getString(0);
    		RunOnMainThread(new Runnable() {
    			@Override
    			public void run() {
    				byte[] a = NearBytes.stringToBytes(value);
    				callbackContext.success(a);
    			}});
    	} else if(action.equals("longToBytes") ){
    		final long value = args.getLong(0);
    		RunOnMainThread(new Runnable() {
    			@Override
    			public void run() {
    				byte[] a = NearBytes.longToBytes(value);
    				callbackContext.success(a);
    			}});
    	} else if(action.equals("doubleToBytes") ){
    		final double value = args.getDouble(0);
    		RunOnMainThread(new Runnable() {
    			@Override
    			public void run() {
    				byte[] a = NearBytes.doubleToBytes(value);
    				callbackContext.success(a);
    			}});
    	} else if(action.equals("intToBytes") ){
    		final int value = args.getInt(0);
    		RunOnMainThread(new Runnable() {
    			@Override
    			public void run() {
    				byte[] a =  NearBytes.intToBytes(value);
    				callbackContext.success(a);
    			}});
    	} else if(action.equals("floatToBytes") ){
    		final float value = (float)args.getDouble(0);
    		RunOnMainThread(new Runnable() {
    			@Override
    			public void run() {
    				byte[] a =  NearBytes.floatToBytes(value);
    				callbackContext.success(a);
    			}});
    	} else if(action.equals("shortToBytes") ){
    		final short value = (short)args.getInt(0);
    		RunOnMainThread(new Runnable() {
    			@Override
    			public void run() {
    				byte[] a =  NearBytes.shortToBytes(value);
    				callbackContext.success(a);
    			}});
    	} else if(action.equals("charToBytes") ){
    		final String value = args.getString(0).substring(0,1);
    		RunOnMainThread(new Runnable() {
    			@Override
    			public void run() {
    				byte[] a =  NearBytes.stringToBytes(value);
    				callbackContext.success(a);
    			}});
    	}
    	// unconv
    	else if(action.equals("bytesToString") ){
    		RunOnMainThread(new Runnable() {
    			@Override
    			public void run() {
     				final String a = NearBytes.bytesToString( getBytesFromArray(args,0) );
     				debug("RET:"+a);
    				callbackContext.success(a);
    			}});
     	} else if(action.equals("bytesToLong") ){
     		RunOnMainThread(new Runnable() {
    			@Override
    			public void run() {
    				String a = ""+NearBytes.bytesToLong( getBytesFromArray(args,0) );
    				callbackContext.success(a);
    			}});
     	} else if(action.equals("bytesToDouble") ){
     		RunOnMainThread(new Runnable() {
    			@Override
    			public void run() {
    				String a = ""+NearBytes.bytesToDouble( getBytesFromArray(args,0) );
    				callbackContext.success(a);
    			}});
     	} else if(action.equals("bytesToInt") ){
     		RunOnMainThread(new Runnable() {
    			@Override
    			public void run() {
    				int a = NearBytes.bytesToInt( getBytesFromArray(args,0) );
    				callbackContext.success(a);
    			}});
     	} else if(action.equals("bytesToFloat") ){
     		RunOnMainThread(new Runnable() {
    			@Override
    			public void run() {
    				String a = ""+NearBytes.bytesToFloat( getBytesFromArray(args,0) );
    				callbackContext.success(a);
    			}});
     	} else if(action.equals("bytesToShort") ){
     		RunOnMainThread(new Runnable() {
    			@Override
    			public void run() {
    				short a = NearBytes.bytesToShort( getBytesFromArray(args,0) );
     				callbackContext.success(a);
    			}});
     	} else if(action.equals("bytesToChar") ){
     		RunOnMainThread(new Runnable() {
    			@Override
    			public void run() {
    				String a = NearBytes.bytesToString( getBytesFromArray(args,0) );
    				callbackContext.success(a);
    			}});
     	}
    	else if (action.equals("getDeviceInfo")) {
            /*JSONObject r = new JSONObject();
            r.put("uuid", Device.uuid);
            r.put("version", this.getOSVersion());
            r.put("platform", Device.platform);
            r.put("cordova", Device.cordovaVersion);
            r.put("model", this.getModel());
            callbackContext.success(r);*/
        }
        else {
            return false;
        }
        return true;
    }

    
    private boolean getBoolFromArray(JSONArray array,int index){
    	if(array.length()>index)
			try {
				return array.getBoolean(index);
			} catch (JSONException e) {
				e.printStackTrace();
			}
    	return false;
    }
    private byte[] getBytesFromArray(JSONArray array,int index){
    	if( array.length() > index )
	    	try{
	    		return getBytesFromArray(array.getJSONArray(0));
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
		return null;
    }
    
    private byte[] getBytesFromArray(JSONArray a){
    	int s=a.length();
    	debug( "size of array: "+s);
    	byte[] b=new byte[s];
    	for(int i=0;i<s;i++)
			try {
				b[i]=(byte)a.getInt(i);
			} catch (JSONException e) {
				e.printStackTrace();
			}
    	return b;
    }
    
    private void RunOnMainThread(final Runnable r){
    	Future<Boolean>threadObj = this.cordova.getThreadPool().submit(new Callable<Boolean>() {
    		@Override
    		public Boolean call() throws Exception {
    			r.run();
    			return true;
    		}
		});
    	
    	try{
    		threadObj.get();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    //--------------------------------------------------------------------------
    // LOCAL METHODS
    //--------------------------------------------------------------------------

    /**
     * Listen for telephony events: RINGING, OFFHOOK and IDLE
     * Send these events to all plugins using
     *      CordovaActivity.onMessage("telephone", "ringing" | "offhook" | "idle")
     */
    /*private void initTelephonyReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        //final CordovaInterface mycordova = this.cordova;
        this.telephonyReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // If state has changed
                if ((intent != null) && intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
                    if (intent.hasExtra(TelephonyManager.EXTRA_STATE)) {
                        String extraData = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                        if (extraData.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                            LOG.i(TAG, "Telephone RINGING");
                            webView.postMessage("telephone", "ringing");
                        }
                        else if (extraData.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                            LOG.i(TAG, "Telephone OFFHOOK");
                            webView.postMessage("telephone", "offhook");
                        }
                        else if (extraData.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                            LOG.i(TAG, "Telephone IDLE");
                            webView.postMessage("telephone", "idle");
                        }
                    }
                }
            }
        };

        // Register the receiver
        this.cordova.getActivity().registerReceiver(this.telephonyReceiver, intentFilter);
    }*/
        
    public void onPause(boolean multitasking) {
		debug("onPause");
		if(NearBytes.shared()!=null)
			NearBytes.shared().onPause();
    };
    @Override
    public void onResume(boolean multitasking) {
		debug("onResume");
    	super.onResume(multitasking);
    	if(NearBytes.shared()!=null)
			NearBytes.shared().onResume();
    }
    /** Unregister receiver.*/
    public void onDestroy() {
		debug("onDestroy");
        if(NearBytes.shared()!=null)
        	NearBytes.shared().onDestroy();
    }
    
    /** Get the OS name.
     * @return*/
    public String getPlatform() {
        return NearBytesPhonegap.platform;
    }
    
    private static void debug(String msg){
    	if( DEV_MODE==true )
    		Log.d(TAG, msg);
    }

    @Override
	public void OnReceiveData(final byte[] bytes) {
		if(mNBListiner!=null){
			RunOnMainThread(new Runnable() {
    			@Override
    			public void run() {
					NearBytesPhonegap.this.mNBListiner.success(bytes);
			}});
		}
	}

	@Override
	public void OnReceiveError(final int code, final String msg) {
		if(mNBListiner==null) return;
			RunOnMainThread(new Runnable() {
				@Override
				public void run() {
					JSONObject mData=null;
					try{
						mData = new JSONObject();
						mData.put("code", code);
						mData.put("msg",msg);
					}catch(Exception e){e.printStackTrace();}
					NearBytesPhonegap.this.mNBListiner.error(mData);
				}
			});
	}
}
