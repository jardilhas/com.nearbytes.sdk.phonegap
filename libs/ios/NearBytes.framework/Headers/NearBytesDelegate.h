//
//  TTMCommunicatorDelegate.h
//  NSC
//
//  Created by Hangar 4 Projetos LTDA on 5/8/12.
//  Copyright (c) 2012 Kinetics. All rights reserved.
//

#import <Foundation/Foundation.h>

/**
 *
 * @since 1.2
 */
@protocol NearBytesDelegate

//-(void)onEndPlaySound;
-(void)OnReceiveDataListener:(NSMutableData*)bytes;
-(void)OnReceiveErrorListener:(int)code :(NSString*)msg;

@end
