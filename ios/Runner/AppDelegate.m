#import "AppDelegate.h"
#import "GeneratedPluginRegistrant.h"

#import "FirstViewController.h"
#import "SecondViewController.h"
#import "MainViewController.h"



@interface AppDelegate()
@property (nonatomic, strong) UINavigationController *navigationController;

@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application
didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    // Override point for customization after application launch.
    //初始化引擎和通道。所有的VC都会使用这个引擎
    self.flutterEngine=[[FlutterEngine alloc] initWithName:@"lin-flutter-engine"];
    
    [self.flutterEngine run];
    
    //[self.flutterEngine runWithEntrypoint:@"main" libraryURI:nil initialRoute:@"main-flutter"];
    
    [GeneratedPluginRegistrant registerWithRegistry:self.flutterEngine];
    
    //ios原生通信
    self.flutterMethodChannel = [FlutterMethodChannel
                                 methodChannelWithName:@"app.lin.test/main"
                                 binaryMessenger:self.flutterEngine.binaryMessenger];
    [self.flutterMethodChannel setMethodCallHandler:^(FlutterMethodCall* call, FlutterResult result) {

        if ([call.method isEqualToString:@"goToFirstAty"]){
            NSLog(@"去第一页");
            FirstViewController *firstController = [FirstViewController alloc];
            [self.navigationController pushViewController:firstController animated:YES];
            
        }
        
    }];
    

    //初始化入口改成这个了,解决引擎与mainVC重新绑定的问题
    MainViewController *vc = [[MainViewController alloc] initWithEngine:self.flutterEngine nibName:@"main-flutter" bundle:nil];
    //解决App打开白屏问题
    [vc loadDefaultSplashScreenView];
    
    self.navigationController = [[UINavigationController alloc] initWithRootViewController:vc];
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    
    self.window.rootViewController = self.navigationController;
    
    [self.window makeKeyAndVisible];
    

    
    
    return [super application:application didFinishLaunchingWithOptions:launchOptions];
}

@end
