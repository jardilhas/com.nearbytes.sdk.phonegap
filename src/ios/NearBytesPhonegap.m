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
 under the License. */

#include <sys/types.h>
#include <sys/sysctl.h>
#import <Cordova/CDV.h>
#import "NearBytesPhonegap.h"
#import <objc/message.h>

@implementation NearBytesPhonegap (ModelVersion)

- (NSString*)modelVersion{
    size_t size;
    sysctlbyname("hw.machine", NULL, &size, NULL, 0);
    char* machine = malloc(size);
    sysctlbyname("hw.machine", machine, &size, NULL, 0);
    NSString* platform = [NSString stringWithUTF8String:machine];
    free(machine);

    return platform;
}

@end

@interface NearBytesPhonegap () {
    CDVInvokedUrlCommand * mListener;
};
    
@end

@implementation NearBytesPhonegap

@synthesize view;
+ (NSString*)cordovaVersion{
    return CDV_VERSION;
}

    
-(BOOL)create:(CDVInvokedUrlCommand*)command{
    if([NearBytes shared]!=NULL){
        NSLog(@"NearBytes already exists!");
        return false;
    }
    int appid=0;
    NSString * appkey=NULL;
    if ( command.arguments != NULL && [command.arguments count]>=1 && [command.arguments objectAtIndex:0]  ) {
        appid=[[[command.arguments objectAtIndex:0] objectForKey:@"appid"] intValue];
        appkey=[[command.arguments objectAtIndex:0] objectForKey:@"appkey"];
    }
    @try {
        NearBytes * NBY=NULL;
        if (appid>0 && appkey!=NULL)
            NBY = [[NearBytes alloc] init:[self getActivity] appid:appid appkey:appkey];
        else
            NBY = [[NearBytes alloc] init:[self getActivity]];
    }
    @catch (NSException *exception) {
        NSLog(@"NearBytes Exception, %@",[exception reason]);
        CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:[NSString stringWithFormat:@"NearBytes Exception, %@",[exception reason]]];
        [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
        return true;
    }
    CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
    return true;
}

#pragma mark -- DEBUG FUNCTIONS
-(void)debugMode:(CDVInvokedUrlCommand*)command{
    if([NearBytes shared]!=NULL)
        [[NearBytes shared] debugMode];
}

-(void)playDemo:(CDVInvokedUrlCommand*)command{
    if ([NearBytes shared]!=NULL )
        [[NearBytes shared] send:[NearBytes stringToBytes:@"demonstration text!"]];
}

#pragma mark -- CONFIG FUNCTIONS
-(void)setTimeout:(CDVInvokedUrlCommand*)command{
    if ([NearBytes shared]!=NULL)
        [[NearBytes shared] setTimeout:[[command.arguments objectAtIndex:0] intValue]];
}
-(void)setAskTimeout:(CDVInvokedUrlCommand*)command{
    if ([NearBytes shared]!=NULL)
        [[NearBytes shared] setAskTimeout:[[command.arguments objectAtIndex:0] intValue]];
}
-(void)setRetries:(CDVInvokedUrlCommand*)command{
    if ([NearBytes shared]!=NULL)
        [[NearBytes shared] setRetries:[[command.arguments objectAtIndex:0] intValue]];
}
-(void)setPlayMode:(CDVInvokedUrlCommand*)command{
    if ([NearBytes shared]!=NULL)
        [[NearBytes shared] setPlayMode:[[command.arguments objectAtIndex:0] intValue]];
}

#pragma mark -- SEND FUNCTIONS
-(void)send:(CDVInvokedUrlCommand*)command{
    NSMutableData * bytes=[self getBytesFromArray:[command.arguments objectAtIndex:0]];
    if( [NearBytes shared] != NULL )
        [[NearBytes shared] send:bytes];
}

-(void)ask:(CDVInvokedUrlCommand*)command{
    NSMutableData * bytes=[self getBytesFromArray:[command.arguments objectAtIndex:0]];
    if( [NearBytes shared] != NULL )
        [[NearBytes shared] ask:bytes];
}
    
-(void)answer:(CDVInvokedUrlCommand*)command{
    NSMutableData * bytes=[self getBytesFromArray:[command.arguments objectAtIndex:0]];
    Boolean hangup=[[command.arguments objectAtIndex:1] boolValue]==true? true : false;
    if( [NearBytes shared] != NULL )
        [[NearBytes shared] answer:bytes hangup:hangup];
}

#pragma mark -- CONV FUNCTIONS, valueToBytes
-(void)stringToBytes:(CDVInvokedUrlCommand*)command{
    NSString * value=[command.arguments objectAtIndex:0];
    CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArrayBuffer:[NearBytes stringToBytes:value]];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(void)longToBytes:(CDVInvokedUrlCommand*)command{
    long value=[[command.arguments objectAtIndex:0] longValue];
    CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArrayBuffer:[NearBytes longToBytes:value]];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(void)doubleToBytes:(CDVInvokedUrlCommand*)command{
    double value=[[command.arguments objectAtIndex:0] doubleValue];
    CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArrayBuffer:[NearBytes doubleToBytes:value]];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(void)intToBytes:(CDVInvokedUrlCommand*)command{
    int value=[[command.arguments objectAtIndex:0] intValue];
    CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArrayBuffer:[NearBytes intToBytes:value]];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(void)floatToBytes:(CDVInvokedUrlCommand*)command{
    float value=[[command.arguments objectAtIndex:0] floatValue];
    CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArrayBuffer:[NearBytes floatToBytes:value]];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(void)shortToBytes:(CDVInvokedUrlCommand*)command{
    short value=[[command.arguments objectAtIndex:0] shortValue];
    CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArrayBuffer:[NearBytes shortToBytes:value]];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(void)charToBytes:(CDVInvokedUrlCommand*)command{
    NSString * value=[command.arguments objectAtIndex:0];
    CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArrayBuffer:[NearBytes stringToBytes:[value substringToIndex:1]]];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}
#pragma mark -- CONV FUNCTIONS, bytesToValue
-(void)bytesToString:(CDVInvokedUrlCommand*)command{
    NSMutableData * bytes = [self getBytesFromArray:[command.arguments objectAtIndex:0]];
    CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:[NearBytes bytesToString:bytes]];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(void)bytesToLong:(CDVInvokedUrlCommand*)command{
    NSMutableData * bytes = [self getBytesFromArray:[command.arguments objectAtIndex:0]];
    CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:[NSString stringWithFormat:@"%li",[NearBytes bytesToLong:bytes]]];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(void)bytesToInt:(CDVInvokedUrlCommand*)command{
    NSMutableData * bytes = [self getBytesFromArray:[command.arguments objectAtIndex:0]];
    CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:[NearBytes bytesToInt:bytes]];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(void)bytesToDouble:(CDVInvokedUrlCommand*)command{
    NSMutableData * bytes = [self getBytesFromArray:[command.arguments objectAtIndex:0]];
    CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:[NSString stringWithFormat:@"%f",[NearBytes bytesToDouble:bytes]]];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(void)bytesToFloat:(CDVInvokedUrlCommand*)command{
    NSMutableData * bytes = [self getBytesFromArray:[command.arguments objectAtIndex:0]];
    CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:[NSString stringWithFormat:@"%f",[NearBytes bytesToFloat:bytes]]];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(void)bytesToShort:(CDVInvokedUrlCommand*)command{
    NSMutableData * bytes = [self getBytesFromArray:[command.arguments objectAtIndex:0]];
    CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:[NSString stringWithFormat:@"%i",[NearBytes bytesToShort:bytes]]];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}
    
-(void)bytesToChar:(CDVInvokedUrlCommand*)command{
    NSMutableData * bytes = [self getBytesFromArray:[command.arguments objectAtIndex:0]];
    CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:[[NearBytes bytesToString:bytes] substringToIndex:1]];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

#pragma mark -- LISTENING FUNCTIONS
-(void)startListening:(CDVInvokedUrlCommand*)command{
    if ([NearBytes shared]!=NULL)
        [[NearBytes shared] startListening];
}

-(void)stopListening:(CDVInvokedUrlCommand*)command{
    if([NearBytes shared]!=NULL)
        [[NearBytes shared] stopListening];
}
-(void)setNearBytesListener:(CDVInvokedUrlCommand*)command{
    mListener=command;
}

-(void)OnReceiveDataListener:(NSMutableData *)bytes{
    if( mListener != NULL ){
        CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArrayBuffer:bytes];
        [self.commandDelegate sendPluginResult:result callbackId:mListener.callbackId];
    }
}
    
-(void)OnReceiveErrorListener:(int)code :(NSString *)msg{
    if( mListener != NULL ){
        NSMutableDictionary * data = [[NSMutableDictionary alloc] init];
        [data setObject:msg forKey:@"msg"];
        [data setObject:[NSNumber numberWithInt:code] forKey:@"code"];
        CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:data];
        [self.commandDelegate sendPluginResult:result callbackId:mListener.callbackId];
    }
}
#pragma mark -- TOOLS
-(NSMutableData*)getBytesFromArray:(NSArray*)arr{
    char bytes[arr.count];
    for (int i=0; i<arr.count; i++)
        bytes[i]=[[arr objectAtIndex:i] intValue];
    return [[NSMutableData alloc] initWithBytes:bytes length:arr.count];
}
-(UIViewController<NearBytesDelegate>*)getActivity{
    self.view = self.viewController.view;
    return (UIViewController<NearBytesDelegate>*)self;
}
#pragma -- mark DEFAULT FUNCTIONS
-(void)pluginInitialize{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onPause) name:UIApplicationDidEnterBackgroundNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onResume) name:UIApplicationWillEnterForegroundNotification object:nil];
}

-(void)onPause{
    NSLog(@"NearBytes.PhoneGap onPause");
    if ([NearBytes shared]!=NULL)
        [NearBytes applicationWillResignActive];
}
-(void)onResume{
    NSLog(@"NearBytes.PhoneGap onResume");
    if ([NearBytes shared]!=NULL)
        [NearBytes applicationDidBecomeActive];
}
-(void)onAppTerminate{
    NSLog(@"NearBytes.PhoneGap onAppTerminate");
    if ([NearBytes shared]!=NULL)
        [NearBytes applicationWillTerminate];
}
-(void)dispose{
    NSLog(@"NearBytes.PhoneGap dispose");
    if ([NearBytes shared]!=NULL)
        [NearBytes applicationWillTerminate];
}
@end
