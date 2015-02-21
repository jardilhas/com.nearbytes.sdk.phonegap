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

#import <UIKit/UIKit.h>
#import <Cordova/CDVPlugin.h>
#import "NearBytes.h"
#import "NearBytesDelegate.h"

@interface NearBytesPhonegap : CDVPlugin <NearBytesDelegate>{
    UIView * view;
}
@property (nonatomic,retain) UIView * view;
    
+ (NSString*)cordovaVersion;

//- (void)getDeviceInfo:(CDVInvokedUrlCommand*)command;
    
    
-(BOOL)create:(CDVInvokedUrlCommand*)command;
    
-(void)send:(CDVInvokedUrlCommand*)command;
-(void)ask:(CDVInvokedUrlCommand*)command;
-(void)answer:(CDVInvokedUrlCommand*)command;
/// TO BYTES
-(void)stringToBytes:(CDVInvokedUrlCommand*)command;
-(void)longToBytes:(CDVInvokedUrlCommand*)command;
-(void)doubleToBytes:(CDVInvokedUrlCommand*)command;
-(void)intToBytes:(CDVInvokedUrlCommand*)command;
-(void)floatToBytes:(CDVInvokedUrlCommand*)command;
-(void)shortToBytes:(CDVInvokedUrlCommand*)command;
-(void)charToBytes:(CDVInvokedUrlCommand*)command;
/// TO VALUE
-(void)bytesToString:(CDVInvokedUrlCommand*)command;
-(void)bytesToLong:(CDVInvokedUrlCommand*)command;
-(void)bytesToDouble:(CDVInvokedUrlCommand*)command;
-(void)bytesToInt:(CDVInvokedUrlCommand*)command;
-(void)bytesToFloat:(CDVInvokedUrlCommand*)command;
-(void)bytesToShort:(CDVInvokedUrlCommand*)command;
-(void)bytesToChar:(CDVInvokedUrlCommand*)command;

@end
