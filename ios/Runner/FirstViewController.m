//
//  FirstViewController.m
//  Runner
//
//  Created by linzupeng on 2022/3/23.
//

#import "FirstViewController.h"
#import "SecondViewController.h"
#import "AppDelegate.h"


@interface FirstViewController ()

@end

@implementation FirstViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor=UIColor.whiteColor;
    
    UIButton * button = [UIButton buttonWithType:UIButtonTypeSystem];
    button.frame=CGRectMake(40, 100, 240, 30);
    button.backgroundColor=[UIColor blueColor];
    [button setTitle:@"点我一下退出当前页面"
            forState:UIControlStateNormal];
    [button addTarget:self
               action:@selector(quitPage)
     forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:button];
    
    UIButton * button2 = [UIButton buttonWithType:UIButtonTypeSystem];
    button2.frame=CGRectMake(40, 180, 240, 30);
    button2.backgroundColor=[UIColor blueColor];
    [button2 setTitle:@"去第二页"
             forState:UIControlStateNormal];
    [button2 addTarget:self
                action:@selector(goToSecondPage)
      forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:button2];
}

-(void) quitPage{
    NSLog(@"退出点击");
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)goToSecondPage{
    NSLog(@"去第二页点击");
    AppDelegate *myDelegate = (AppDelegate*)[[UIApplication sharedApplication]delegate];

    SecondViewController *secondController =[[SecondViewController alloc] initWithEngine:myDelegate.flutterEngine nibName:nil bundle:nil];
    [self.navigationController pushViewController:secondController animated:YES];
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
