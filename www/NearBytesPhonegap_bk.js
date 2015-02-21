	/** This class helps you to use the NearBytes SDK on Phonegap.
	 * @author Icaro F. Martins <icaro@kinetics.com.br>
	 * @version 1.0 */
	function NearBytes() {
		var exec = require('cordova/exec');
		var PLUGIN_NAME = "NearBytes";
		
		///////////////////////////////////
		/// [INIT] RECEIVE ERROR CODES
		///////////////////////////////////
		this.NB_ERROR_UNDEFINED=0;
		this.NB_ERROR_EXCEEDED_RETRIES=1;
		this.NB_ERROR_ASK_TIMEOUT=2;
		this.NB_ERROR_INVALID_ID=3;
		this.NB_ERROR_READ_ERROR=4;
		///////////////////////////////////
		/// [INIT] PLAY MODE FLAGS
		///////////////////////////////////
		/** This flag indicates that the sound will be playing in the out available. */
		this.PLAY_MODE_FLAG_AUTO=0;
		/** This flag indicates that the sound will be playing by the speaker. */
		this.PLAY_MODE_FLAG_SPEAKER=1;
		///////////////////////////////////
		/// [INIT] Constants Variables
		///////////////////////////////////
		/** This constant contains the default test appid. */
		this.DEFAULT_APPID_TEST=0;
		/** This Constant contains the default send timeout. */
		this.DEFAULT_TIMEOUT=1000;
		/** This Constant contains the default ask's timeout.
		* This is used as a time who the app wait another ask. */
		this.DEFAULT_ASK_TIMEOUT=5000;
		/** This Constant contains the default retries' numbers. */
		this.DEFAULT_RETRIES=4;
		
		var mNearBytesListener={};
		var listenerAlreadCalled=false;
		var DEV_MODE=false;
	
		this.create=function(appid,appkey){
			console.log("NearBytesPhonegap(JS)::create");
			var params=[];
			if( typeof(appid)=="number" && typeof(appkey)=="string" )
				params=[appid,appkey];
			var ret=false;
			exec(function(){ret=true;},null,PLUGIN_NAME,"create",params);
			return ret;
		};
		this.playDemo=function(){
			debug("playDemo");
			exec(null,null,PLUGIN_NAME,"playDemo",[]);
		};
		this.debugMode=function(){
			//DEV_MODE=true;
			exec(null,null,PLUGIN_NAME,"debugMode",[]);
		};
		var onReceiveData=function(e){
			if( typeof(mNearBytesListener)=="object" && typeof(mNearBytesListener["onReceiveData"])=="function"){
				setTimeout( function(){
					mNearBytesListener.onReceiveData(convBufferToByteArray(e));
				},10);
			}
			setNativeListener();
		};
		var onReceiveError=function(e){
			if( typeof(mNearBytesListener)=="object" && typeof(mNearBytesListener["onReceiveError"])=="function"){
				setTimeout( function(){
					mNearBytesListener.onReceiveError(e);
				},10);
			}
			setNativeListener();
		};
		var setNativeListener=function(){
			exec(onReceiveData,onReceiveError,PLUGIN_NAME,"setNearBytesListener",[]);
		};
		this.setNearBytesListener=function(f){
			debug("setNearBytesListener");
			mNearBytesListener={onReceiveData:f.onReceiveData,onReceiveError:f.onReceiveError};
			if( listenerAlreadCalled != true )
				setNativeListener();
			listenerAlreadCalled=true;
		};
		this.startListening=function(){
			debug("startListening");
			exec(null,null,PLUGIN_NAME,"startListening",[]);
		};
		this.stopListening=function(){
			debug("stopListening");
			exec(null,null,PLUGIN_NAME,"stopListening",[]);
		};
		this.setTimeout=function(v){
			debug("setTimeout");
			exec(null,null,PLUGIN_NAME,"setTimeout",[v]);
		};
		this.setAskTimeout=function(v){
			debug("setAskTimeout");
			exec(null,null,PLUGIN_NAME,"setAskTimeout",[v]);
		};
		this.setRetries = function(v){
			debug("setRetries");
			exec(null,null,PLUGIN_NAME,"setRetries",[v]);
    	};
    	this.setPlayMode = function(v){
			debug("setPlayMode");
			exec(null,null,PLUGIN_NAME,"setPlayMode",[v]);
    	};
		this.send=function(b){
			debug("send");
			if( b==null ) return;
			exec(null,null,PLUGIN_NAME,"send",[b]);
		};
		this.ask=function(b){
			debug("ask");
			if( b==null ) return;
			exec(null,null,PLUGIN_NAME,"ask",[b]);
		};
		this.answer=function(b,h){
			debug("answer");
			if( b==null ) return;
			exec(null,null,PLUGIN_NAME,"answer",[b,(h?true:false)]);
		};
		var bytesAction=function(o,type,act){
			if( o == null ) return;
			var ret=null;
			var funcName=( (!act) ? (type+"ToBytes") : ("bytesTo"+type) );
			exec( function(b){ret=b;} , null , PLUGIN_NAME , funcName , [o] );
			if( !act )
				ret=convBufferToByteArray(ret);
			return ret;
		};
		var convBufferToByteArray=function(buffer){
			buffer = new Uint8Array( buffer );
			var tmp=[];
			for( var i=0;i<buffer.length;i++ )
				tmp[i]=buffer[i];
			buffer=tmp;
			return buffer;
		};
		///////////////////////////////////
		/// CONVERT TO BYTES
		/// <byte[]> <type>ToBytes( <type> );
		///////////////////////////////////
		this.stringToBytes=function(o){return bytesAction(o,"string");};
		this.longToBytes=function(o){return bytesAction(o,"long");};
		this.doubleToBytes=function(o){return bytesAction(o,"double");};
		this.intToBytes=function(o){return bytesAction(o,"int");};
		this.floatToBytes=function(o){return bytesAction(o,"float");};
		this.shortToBytes=function(o){return bytesAction(o,"short");};
		this.charToBytes=function(o){return bytesAction(o,"char");};
		///////////////////////////////////
		/// CONVERT TO TYPE
		/// <type> bytesTo<Type>( <bytes[]> );
		///////////////////////////////////
		this.bytesToString=function(o){return bytesAction(o,"String",1);};
		this.bytesToLong=function(o){return bytesAction(o,"Long",1)*1;};
		this.bytesToDouble=function(o){return bytesAction(o,"Double",1)*1.0;};
		this.bytesToInt=function(o){return bytesAction(o,"Int",1);};
		this.bytesToFloat=function(o){return bytesAction(o,"Float",1)*1.0;};
		this.bytesToShort=function(o){return bytesAction(o,"Short",1);};
		this.bytesToChar=function(o){return bytesAction(o,"Char",1);};
		
		var debug=function(){if(DEV_MODE)console.log("NearBytesPhonegap(JS)::"+arguments[0]);};
		return this;
	};
	///////////////////////////////////
	/// NATIVE FUNCTIONS REQUIRED
	/// Required native functions (Uint8Array),
	/// if them not exists we try to create here
	///////////////////////////////////
	(function() {
		/// Check if the Function Uint8Array exists
		try {var a = new Uint8Array(1);return;} catch(e) { }
		function subarray(start, end) {return this.slice(start, end);};
		function set_(array, offset) {
			if (arguments.length < 2) offset = 0;
			for (var i = 0, n = array.length; i < n; ++i, ++offset)
			this[offset] = array[i] & 0xFF;
		};
		function TypedArray(arg1) {
			var result;
			if (typeof arg1 === "number") {
				result = new Array(arg1);
				for (var i = 0; i < arg1; ++i) result[i] = 0;
			} else
				result = arg1.slice(0);
			result.subarray = subarray;
			result.buffer = result;
			result.byteLength = result.length;
			result.set = set_;
			if (typeof arg1 === "object" && arg1.buffer)
				result.buffer = arg1.buffer;
			return result;
		};
		window.Uint8Array = TypedArray;
		window.Uint32Array = TypedArray;
		window.Int32Array = TypedArray;
	})();
	(function() {
		if ("response" in XMLHttpRequest.prototype || "mozResponseArrayBuffer" in XMLHttpRequest.prototype || "mozResponse" in XMLHttpRequest.prototype || "responseArrayBuffer" in XMLHttpRequest.prototype) return;
		Object.defineProperty(XMLHttpRequest.prototype, "response", {
			get: function() {return new Uint8Array( new VBArray(this.responseBody).toArray() );}
		});
	})();
	(function() {
		/// Check if the Function Uint8Array exists
		if ("btoa" in window) return;
		var digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
		window.btoa = function(chars) {
			var buffer = "";
			var i, n;
			for (i = 0, n = chars.length; i < n; i += 3) {
				var b1 = chars.charCodeAt(i) & 0xFF;
				var b2 = chars.charCodeAt(i + 1) & 0xFF;
				var b3 = chars.charCodeAt(i + 2) & 0xFF;
				var d1 = b1 >> 2, d2 = ((b1 & 3) << 4) | (b2 >> 4);
				var d3 = i + 1 < n ? ((b2 & 0xF) << 2) | (b3 >> 6) : 64;
				var d4 = i + 2 < n ? (b3 & 0x3F) : 64;
				buffer += digits.charAt(d1) + digits.charAt(d2) + digits.charAt(d3) +      digits.charAt(d4);
			}
		return buffer;
		}; 
	})();
	
	module.exports = new NearBytes();
	