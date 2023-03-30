
import 'package:flutter/material.dart';

import 'channel_init.dart';

class BPage extends StatefulWidget {
  const BPage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  State<BPage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<BPage> {
  int _counter = 0;

  void _incrementCounter() {
    ChannelTool.platformMethodChannel.invokeMethod("goToFirstAty");
  }

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            const Text(
              '我是Flutter2页面',
            ),
            Text(
              '$_counter',
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }

}