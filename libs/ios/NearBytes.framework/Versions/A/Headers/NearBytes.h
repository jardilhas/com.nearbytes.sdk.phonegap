/****************************************************************
 Copyright (c) 2011 - NEARBYTES® / TOTMOB® / KINETICS®
 
 http://www.nearbytes.com
 http://www.totmob.com
 http://www.kinetics.com.br
 
 ****************************************************************/


#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import "NearBytesDelegate.h"

#define NB_VERSION (char*)"1.7.3"

enum {
    NB_ERROR_UNDEFINED=0,
    NB_ERROR_EXCEEDED_RETRIES=1,
    NB_ERROR_ASK_TIMEOUT=2,
    NB_ERROR_INVALID_ID=3,
    NB_ERROR_READ_ERROR=4,
    NB_DEFAULT_APPID_TEST=0,
    NB_DEFAULT_TIMEOUT=1000,
    NB_DEFAULT_ASK_TIMEOUT=5000,
    NB_DEFAULT_RETRIES=4,
    NB_DEFAULT_METHOD_FLAG=1,
    NB_SOUND_METHOD_FLAG=1,
    NB_NFC_METHOD_FLAG=2,
    NB_PLAY_MODE_FLAG_AUTO=0,
    NB_PLAY_MODE_FLAG_SPEAKER=1
};


/**
 * @author Ícaro F. Martins <icaro@kinetics.com.br>
 * @package com.nearbytes.sdk
 * @version 1.7
 * @see http://www.nearbytes.com */
@interface NearBytes : NSObject<NSURLConnectionDataDelegate> {}


/** This function isn't used to initialize the nearbytes instance.
 * @since 1.0 */
-(id)init;
+(NearBytes*)shared;
/** This function is used to initialize the nearbytes instance.
 * @param controller Your UIViewController with the NearBytesDelegate
 * @since 1.0 */
-(id)init:(UIViewController<NearBytesDelegate>*)controller;
/** This function is used to initialize the nearbytes instance,
 * if you want to define the method of transmission use the other function.
 * @param controller Your UIViewController with the NearBytesDelegate
 * @param appid Your appid.
 * @param appkey Your appKey.
 * @since 1.6 */
-(id)init:(UIViewController<NearBytesDelegate>*)controller appid:(int)appid appkey:(NSString*)appkey;
/** This function is used to initialize the nearbytes instance.
 * @param controller Your UIViewController with the NearBytesDelegate
 * @param appid Your appid
 * @param appkey Your appKey.
 * @param methodFlags Methods.
 * @since 1.6 */
-(id)init:(UIViewController<NearBytesDelegate>*)controller appid:(int)appid appkey:(NSString*)appkey methodFlags:(int)method;

/** This function return the NearBytes' version */
+(char*)version;
+(void)applicationWillResignActive;
+(void)applicationDidBecomeActive;
+(void)applicationWillTerminate;
/** This function indicates if the debug mode is active */
+(bool)isDebug;

/** This function is used to set the delegate who acts as
 * a listener of NearBytes' events.
 * @param delegate NearBytesDelegate instance.
 * @since 1.5 */
-(void)setDelegate:(UIViewController<NearBytesDelegate>*)delegate;
/** This function is used to change the default timeout.
 * @param timeout Timeout. (Default value:1000)
 * @since 1.6 */
-(void)setTimeout:(int)timeout;
/** This function is used to change the default play mode.
 * @param playMode Play mode. (Default value is auto: 0
 * @since 1.6 */
-(void)setPlayMode:(int)playMode;
/** This function is used to change the default ask's timeout
 * This timeout works as the time who the app wait another ask,
 * if it already received one.
 * @param timeout Timeout. (Default value:5000)
 * @since 1.6 */
-(void)setAskTimeout:(int)timeout;
/** This function is used to change the number of retries.
 * @param number Number of Retries. (Default value:4)
 * @since 1.6 */
-(void)setRetries:(int)retries;
/** This function actives the debug mode */
-(void)debugMode;
/** This function makes the nearbytes starts to listen
 * @since 1.4 */
-(void)startListening;
/** This function makes the nearbytes stop to listen.
 * @since 1.4 */
-(void)stopListening;
/** This function indicates if nearbytes is listening.
 * @since 1.6.02 */
-(bool)isListening;
/** @since 1.4 */
-(void)reset;
/** This function transmit the message.
 * @param bytes Send's data.
 * @since 1.4 */
-(void)send:(NSData*)bytes;
/** This function ask an answer for other device.
 * @param bytes Ask's data.
 * @since 1.6 */
-(void)ask:(NSData*)bytes;
/** This function is used to answer an ask.
 * @param bytes Answer's data.
 * @since 1.6 */
-(void)answer:(NSData*)bytes;
/** This function is used to answer an ask.
 * @param bytes Answer's data
 * @param hangup This flag indicates if is the last answer.
 * @since 1.6 */
-(void)answer:(NSData*)bytes hangup:(bool)hangup;


#pragma mark - EVENTS' FUNCTIONS

/** This function call the listener of action receive data if it exists
 * @since 1.2 */
-(void)onReceiveData:(NSMutableData*)data;
/** This function call the listener of action receive error if it exists
 * @since 1.2 */
-(void)onReceiveError:(int)code :(NSString*)msg;


#pragma mark - CONVERTIONS' FUNCTIONS

/** Convert a NSString value into a byte array.
 * @param value String value.
 * @return byte array */
+(NSMutableData*)stringToBytes:(NSString*)value;
/** Convert a long value into a byte array.
 * @param value long value.
 * @return byte array */
+(NSMutableData*)longToBytes:(long)value;
/** Convert a double value into a byte array.
 * @param value double value.
 * @return byte array */
+(NSMutableData*)doubleToBytes:(double)value;
/** Convert a int value into a byte array.
 * @param value int value.
 * @return byte array */
+(NSMutableData*)intToBytes:(int)value;
/** Convert a float value into a byte array.
 * @param value float value.
 * @return byte array */
+(NSMutableData*)floatToBytes:(float)value;
/** Convert a short value into a byte array.
 * @param value short value.
 * @return byte array */
+(NSMutableData*)shortToBytes:(short)value;
/** Convert a char value into a byte array.
 * @param value char value.
 * @return byte array */
+(NSMutableData*)charToBytes:(char)value;

/** Convert a byte array into a NSString value.
 * @param bytes array of byte values.
 * @return NSString */
+(NSString*)bytesToString:(NSMutableData*)bytes;
/** Convert a byte array into a long value.
 * @param bytes array of byte values.
 * @return long */
+(long)bytesToLong:(NSMutableData*)bytes;
/** Get a long value from a byte array, starting by the specific index.
 * @param bytes array of byte values.
 * @param index this indicates where starts the long on array.
 * @return long */
+(long)bytesToLong:(NSMutableData *)bytes atStartPoint:(int)index;
/** Convert a byte array into a double value.
 * @param bytes array of byte values.
 * @return double */
+(double)bytesToDouble:(NSMutableData*)bytes;
/** Get a double value from a byte array, starting by the specific index.
 * @param bytes array of byte values.
 * @param index this indicates where starts the double on array.
 * @return double */
+(double)bytesToDouble:(NSMutableData *)bytes atStartPoint:(int)index;
/** Convert a byte array into a int value.
 * @param bytes array of byte values.
 * @return int */
+(int)bytesToInt:(NSMutableData*)bytes;
/** Get a int value from a byte array, starting by the specific index.
 * @param bytes array of byte values.
 * @param index this indicates where starts the int on array.
 * @return int */
+(int)bytesToInt:(NSMutableData *)bytes atStartPoint:(int)index;
/** Convert a byte array into a float value.
 * @param bytes array of byte values.
 * @return float */
+(float)bytesToFloat:(NSMutableData*)bytes;
/** Get a float value from a byte array, starting by the specific index.
 * @param bytes array of byte values.
 * @param index this indicates where starts the float on array.
 * @return float */
+(float)bytesToFloat:(NSMutableData *)bytes atStartPoint:(int)index;
/** Convert a byte array into a short value.
 * @param bytes array of byte values.
 * @return short */
+(short)bytesToShort:(NSMutableData*)bytes;
/** Get a short value from a byte array, starting by the specific index.
 * @param bytes array of byte values.
 * @param index this indicates where starts the short on array.
 * @return short */
+(short)bytesToShort:(NSMutableData *)bytes atStartPoint:(int)index;
/** Convert a byte array into a char value.
 * @param bytes array of byte values.
 * @return char */
+(char)bytesToChar:(NSMutableData*)bytes;
/** Get a char value from a byte array, starting by the specific index.
 * @param bytes array of byte values.
 * @param index this indicates where starts the char on array.
 * @return char */
+(char)bytesToChar:(NSMutableData *)bytes atStartPoint:(int)index;


@end
