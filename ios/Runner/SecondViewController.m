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
    
    UIButton * button = [UIButton buttonWithType:UIButtonTypeSystem];
    button.frame=CGRectMake(40, 100, 240, 30);
    button.backgroundColor=[UIColor blueColor];
    [button setTitle:@"点我一下退出当前页面"
            forState:UIControlStateNormal];
    [button addTarget:self
               action:@selector(quitPage)
     forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:button];

}

-(void) quitPage{
    NSLog(@"退出点击");
    [self.navigationController popViewControllerAnimated:YES];
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
