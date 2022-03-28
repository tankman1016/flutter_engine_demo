//
//  SecondViewController.m
//  Runner
//
//  Created by linzupeng on 2022/3/23.
//

#import "SecondViewController.h"
#import "AppDelegate.h"


@interface SecondViewController ()

@end

@implementation SecondViewController



- (void)viewDidLoad {
    [super viewDidLoad];
    
    //self.splashScreenView;
    //self.view.backgroundColor=[UIColor redColor];
    
//    UIButton * button = [UIButton buttonWithType:UIButtonTypeSystem];
//    button.frame=CGRectMake(40, 100, 240, 30);
//    button.backgroundColor=[UIColor blueColor];
//    [button setTitle:@"点我一下退出当前页面"
//            forState:UIControlStateNormal];
//    [button addTarget:self
//               action:@selector(quitPage)
//     forControlEvents:UIControlEventTouchUpInside];
//    [self.view addSubview:button];


    // Do any additional setup after loading the view.
}

-(void) quitPage{
    NSLog(@"退出点击");
    [self.navigationController popViewControllerAnimated:YES];
}

-(BOOL)loadDefaultSplashScreenView{
    return YES;
}


-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
    [self setViewOpaque:YES];
    
    
    if (self.navigationController) {
        [self.navigationController setNavigationBarHidden:YES animated:YES];
    }
    NSLog(@"viewWillAppear33333");
    if(self.engine==NULL){
               NSLog(@"总引擎为空");
           }else{
               NSLog(@"总引擎不为空");
           }
        
//    AppDelegate *myDelegate = (AppDelegate*)[[UIApplication sharedApplication]delegate];
//    NSLog(@"%@", myDelegate.flutterEngine.viewController);
//    if(myDelegate.flutterEngine.viewController!=self){
//        NSLog(@"赋值为空");
//        if(myDelegate.flutterEngine==NULL){
//            NSLog(@"总引擎为空");
//        }else{
//            NSLog(@"总引擎不为空");
//        }
//
//    }
    
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
