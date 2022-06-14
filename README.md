# Flutter 引擎demo

混合开发 单引擎

## Getting Started

在平时Flutter开发中，有时需要用到原生页面。本文针对flutter项目中，插入原生开发进行介绍。

####选择单引擎和双引擎？
 单引擎，不用处理很多关于入口的逻辑。多引擎，每次每个引擎初始化时（在默认不指定入口的情况下），都会执行main方法。且每个引擎之间，内存是独立的，这样就会带来很多问题。比如登录状态需要同步。由于FlutterEngine只能绑定一个Activity页面（IOS VC），双引擎原生页面跳转更流畅些（个人感觉）,单引擎处理路由管理有点复杂。本文针对单引擎进行混合开发研究。

###纯flutter项目引入原生页面
####安卓
####1.引擎初始化
引擎初始化可以在Application中进行，也可以在MainActivity中进行。但是在前者中进行有时会报错。多是一些第三方库的报错，小编猜想，在前者中进行，页面没有呈现，会致使一些代码报错。而后者则不会。
MainActivity继承FlutterActivity，会在onCreate方法创建引擎。
```
 @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    switchLaunchThemeForNormalTheme();
    super.onCreate(savedInstanceState);
   ·····
   //关键代码
    delegate.onAttach(this);
   ·····
  }
```
再看onAttach方法
```
  void onAttach(@NonNull Context context) {    
   //关键代码
    if (flutterEngine == null) {
      setupFlutterEngine();
    }
  }
```

继续看 setupFlutterEngine

```
void setupFlutterEngine() {

    // 检查是否有缓存引擎
    String cachedEngineId = host.getCachedEngineId();
    if (cachedEngineId != null) {
      flutterEngine = FlutterEngineCache.getInstance().get(cachedEngineId);
      isFlutterEngineFromHost = true;
      if (flutterEngine == null) {
        throw new IllegalStateException(）
      }
      return;
    }

    // 判断子类的引擎是否存在，如果存在就不再创建 
    flutterEngine = host.provideFlutterEngine(host.getContext());
    if (flutterEngine != null) {
      isFlutterEngineFromHost = true;
      return;
    }

    // 创建引擎
 
    flutterEngine =
        new FlutterEngine(
            host.getContext(),
            host.getFlutterShellArgs().toArray(),
            /*automaticallyRegisterPlugins=*/ false,
            /*willProvideRestorationData=*/ host.shouldRestoreAndSaveState());
    isFlutterEngineFromHost = false;
  }
```
从源码中可以看到，MainActivity创建的时候，会判断缓存中，有没有引擎。所以我们可如下处理：
```

@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //需要在页面中初始化，这样flutter层调用比方一些和页面相关的东西不会报错
        initFlutterEngine();
        mSavedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
    }

    private void initFlutterEngine() {
        //flutter 引擎 new的时候会自动注册插件
        FlutterEngine flutterEngine = new FlutterEngine(this);
        // 初始化路由名字，abc。可在 flutter 端用 window.defaultRouteName
        flutterEngine.getNavigationChannel().setInitialRoute("abc");
        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );

        channel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL_NAME);
        channel.setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
                switch (call.method) {
                    //这里去原生页面
                    case "goToFirstAty":
                        Intent intent = new Intent(MainActivity.this, FirstAty.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        // Cache the FlutterEngine to be used by FlutterActivity or FlutterFragment.
        // 将当前引擎缓存下来
        FlutterEngineCache
                .getInstance()
                .put(Engine_ID, flutterEngine);

        //将cached_engine_id 设置成Engine_ID ，以便让当前activity 使用缓存引擎
        getIntent().putExtra("cached_engine_id", Engine_ID);
    }
```

####2.多个Activity共用同一个引擎
如何实现原生页面FirstAty跳转Flutter页面SecondAty，同时保证MainActivity存活？
在MainAty中，我们可以通道跳转到原生界面FirstAty。那FirstAty如何跳转新的Flutter页面呢？当然你可以通过逻辑实现跳回MainAty。但我们这里讨论多个Activity共用引擎。
具体代码如下：
FirstAty
```
public class FirstAty extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(NATIVE_TAG, "FirstAty onCreate");
        setContentView(R.layout.aty_play);
        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(NATIVE_TAG, "去第二页");
                startActivity(SecondAty.withCachedEngine(MainActivity.Engine_ID).build(FirstAty.this));
            }
        });
    }
}
```
SecondAty:
```
public class SecondAty extends FlutterActivity {

    public static FlutterActivity.CachedEngineIntentBuilder withCachedEngine(@NonNull String cachedEngineId) {
        return new FlutterActivity.CachedEngineIntentBuilder(SecondAty.class, cachedEngineId)
                .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
                .destroyEngineWithActivity(false);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(MainActivity.NATIVE_TAG, "SecondAty:onCreate");

    }
}

```

####3.MainActivty重连引擎
当SecondAty页面展示的时候，FlutterEngine会和MainAty解绑，并和SecondAty绑定。当一层层返回到MainAty的时候，由于之前解绑，本页面会静止，有时会报错。那怎么重新连上呢？？?
返回该页面时，onResume会被执行，代码如下:
```
 @Override
    protected void onResume() {
        super.onResume();
        //由于一个flutter引擎，只能和一个页面aty绑定
        //当引擎与其他页面绑定时，当前页面会卡住不动
        //这块代码处理，引擎与当前aty的重新绑定
        try {
            FlutterEngine engine = getFlutterEngine();
            if (engine != null) {
                Log.v(MainActivity.NATIVE_TAG, "当前引擎不为空");
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            Log.v(MainActivity.NATIVE_TAG, "当前引擎为空");
            super.onCreate(mSavedInstanceState);
            FlutterEngine engineCache = FlutterEngineCache.getInstance().get(Engine_ID);
            if (engineCache != null) {
                Log.v(MainActivity.NATIVE_TAG, "缓存引擎不为空");
                engineCache.getLifecycleChannel().appIsResumed();
            } else {
                Log.v(MainActivity.NATIVE_TAG, "缓存引擎为空");
            }
        }
    }
```

####IOS

与安卓不同的是，IOS却可以在AppDelegate初始化。由于小编是Android出身，其中具体的原因也是无从探知。直接看代码，代码中很多注释，小编就不多啰嗦了。
```
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
    //解决App启动白屏问题
    [vc loadDefaultSplashScreenView];
    
    self.navigationController = [[UINavigationController alloc] initWithRootViewController:vc];
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    
    self.window.rootViewController = self.navigationController;
    
    [self.window makeKeyAndVisible];
   
    return [super application:application didFinishLaunchingWithOptions:launchOptions];
}

@end

```

同安卓，FirstVC，跳转SecondVC（Flutter承载）

```
-(void)goToSecondPage{
    NSLog(@"去第二页点击");
    AppDelegate *myDelegate = (AppDelegate*)[[UIApplication sharedApplication]delegate];

    SecondViewController *secondController =[[SecondViewController alloc] initWithEngine:myDelegate.flutterEngine nibName:nil bundle:nil];
    [self.navigationController pushViewController:secondController animated:YES];
}
```
解决MainVC重新绑定问题：

```
-(void)viewWillAppear:(BOOL)animated{
    
    NSLog(@"Main viewWillAppear");
    //当引擎绑定其他页面时，以下操作是和当前页面重新绑定
    //绑定前需要 myDelegate.flutterEngine.viewController=nil;不然会报错
    AppDelegate *myDelegate = (AppDelegate*)[[UIApplication sharedApplication]delegate];
    if(myDelegate.flutterEngine.viewController!=self){
        myDelegate.flutterEngine.viewController=nil;
        myDelegate.flutterEngine.viewController=self;
    }
    
    [super viewWillAppear:animated];
    
}
```

参考资料：
https://developpaper.com/how-to-run-flutter-on-ios/
https://api.flutter.dev/objcdoc/Classes/FlutterViewController.html
https://www.notion.so/Displaying-Flutter-modules-with-multiple-views-in-iOS-0e199842629b4cc9a44abba7b0c3e56f

Demo：
https://github.com/tankman1016/flutter_engine_demo/








