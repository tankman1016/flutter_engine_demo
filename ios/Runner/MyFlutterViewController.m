//
//  MyFlutterViewController.m
//  Runner
//
//  Created by linzupeng on 2023/4/3.
//
#import "AppDelegate.h"
#import "MyFlutterViewController.h"

@interface MyFlutterViewController ()

@end

@implementation MyFlutterViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

-(void)viewWillAppear:(BOOL)animated{
    
    //当引擎绑定其他页面时，以下操作是和当前页面重新绑定
    //绑定前需要 myDelegate.flutterEngine.viewController=nil;不然会报错
    AppDelegate *myDelegate = (AppDelegate*)[[UIApplication sharedApplication]delegate];
    if(myDelegate.flutterEngine.viewController!=self){
        myDelegate.flutterEngine.viewController=nil;
        myDelegate.flutterEngine.viewController=self;
    }
    
    [super viewWillAppear:animated];
    
}


- (void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    AppDelegate *myDelegate = (AppDelegate*)[[UIApplication sharedApplication]delegate];
    myDelegate.flutterEngine.viewController=nil;
    
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
