**RxJava Hello World**<br>
_一定要敲代码、一定要敲代码、一定要敲代码，才能深刻理解_
1. pom引入：
    ```$xslt
        <dependencies>
            <dependency>
                <groupId>io.reactivex</groupId>
                <artifactId>rxjava</artifactId>
                <version>1.3.8</version>
            </dependency>
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>4.12</version>
            </dependency>
        </dependencies>
    ```
2. demo代码
    ```$xslt
    
        /**
         * rxJava hello world <br>
         * 生产-消费(订阅) <br>
         * Observable - Observer(Subscriber)  <br>
         * event(事件)：<br>
         * onNext() <br>
         * onCompleted() 事件队列完结<br>
         * onError() 事件队列异常<br>
         */
        @Test
        public void helloWorld()
        {
            Observable<String> producer = Observable.create(new Observable.OnSubscribe<String>()
            {
                @Override
                public void call(Subscriber<? super String> subscriber)
                {
                    subscriber.onNext("hello world!");
                    subscriber.onNext("apple!");
                    subscriber.onNext("orange!");
                    subscriber.onNext("exit");
                    subscriber.onCompleted();
                }
            });
    
            Subscriber<String> consumer = new Subscriber<String>()
            {
                @Override
                public void onCompleted()
                {
                    System.out.println("发送完成!");
                }
    
                @Override
                public void onError(Throwable e)
                {
                    System.out.println("接受消息错误：" + e);
                }
    
                @Override
                public void onNext(String s)
                {
                    System.out.println("收到消息：" + s);
                }
            };
            producer.subscribe(consumer);
        }
    
        /**
         * rxJava from 操作符<br>
         * 生产-消费(订阅)
         */
        @Test
        public void from()
        {
            List<String> source = Arrays.asList("rxJava", "from", "exit");
            // 简写用action 代替 subscription
            final Subscription subscribe = Observable.from(source).subscribe(new Action1<String>()
            {
                @Override
                public void call(String s)
                {
                    System.out.println("From: - " + s);
                }
            });
        }
    
        /**
         * [简要描述]:defer<br/>
         * [详细描述]:只有当订阅者订阅才创建Observable<br/>
         * 每个订阅创建一个新的Observable
         *
         * @return void
         * llxiao  2019/12/13 - 15:02
         **/
        @Test
        public void defer()
        {
            List<String> source = Arrays.asList("rxJava", "defer", "exit");
            final Observable<String> observable = Observable.defer(new Func0<Observable<String>>()
            {
                @Override
                public Observable<String> call()
                {
                    System.out.println("新建一个Observale生产者");
                    return Observable.from(source);
                }
            });
            observable.subscribe(new Action1<String>()
            {
                @Override
                public void call(String s)
                {
                    System.out.println("Defer1: - " + s);
                }
            });
    
            observable.subscribe(new Action1<String>()
            {
                @Override
                public void call(String s)
                {
                    System.out.println("Defer2: - " + s);
                }
            });
        }
    
        /**
         * [简要描述]:在执行onNext之前添加数据<br/>
         * [详细描述]:<br/>
         *
         * @return void
         * llxiao  2019/12/13 - 15:10
         **/
        @Test
        public void startWith()
        {
            List<String> sources = Arrays.asList("rxJava", "startWith", "exit");
            Observable.from(sources).startWith("Hello", "World").subscribe(new Action1<String>()
            {
                @Override
                public void call(String s)
                {
                    System.out.println("Start with: - " + s);
                }
            });
        }
    
        /**
         * [简要描述]:筛选数据<br/>
         * [详细描述]:jdk 8 stream 里面的filter类似<br/>
         *
         * @return void
         * llxiao  2019/12/13 - 15:15
         **/
        @Test
        public void filter()
        {
            List<Integer> numbers = Arrays.asList(10, 2, 6, 3, 7, 5, 9, 8, 1);
            Observable.from(numbers).filter(new Func1<Integer, Boolean>()
            {
                @Override
                public Boolean call(Integer integer)
                {
                    return integer > 4;
                }
            }).subscribe(new Action1<Integer>()
            {
                @Override
                public void call(Integer integer)
                {
                    System.out.println("Filter : " + integer);
                }
            });
        }
    
        /**
         * [简要描述]:数据经过转换后发送给消费者<br/>
         * [详细描述]:可以转换成一个Object对象<br/>
         * 转成字符串输出
         *
         * @return void
         * llxiao  2019/12/13 - 15:18
         **/
        @Test
        public void map()
        {
            List<Integer> numbers = Arrays.asList(10, 2, 6, 3, 7, 5, 9, 8, 1);
            Observable.from(numbers).map((Func1<Integer, Object>) integer -> "[map:" + integer + ']')
                    .subscribe(o -> System.out.println("Map - > " + o.toString()));
        }
    
        /**
         * [简要描述]:map是一个对象变成另外一个对象<br/>
         * [详细描述]:一对多<br/>
         * 如果 i*5,i*10,i*20
         *
         * @return void
         * llxiao  2019/12/13 - 15:24
         **/
        @Test
        public void flatMap()
        {
            List<Integer> numbers = Arrays.asList(10, 2, 6, 3, 7, 5, 9, 8, 1);
            Observable.from(numbers).flatMap(new Func1<Integer, Observable<?>>()
            {
                @Override
                public Observable<?> call(Integer integer)
                {
                    List<Integer> integers = Arrays.asList(integer, integer * 5, integer * 10, integer * 20);
                    return Observable.from(integers);
                }
            }).subscribe(obj -> System.out.println("FlatMap: " + obj.toString()));
        }
    
        /**
         * [简要描述]:接收上一次运算结果，参与下一次的计算，只输出最后一次计算结果<br/>
         * [详细描述]:类似递归<br/>
         * 如 1~9相乘= ?
         *
         * @return void
         * llxiao  2019/12/13 - 15:32
         **/
        @Test
        public void reduce()
        {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
            Observable.from(numbers).reduce(new Func2<Integer, Integer, Integer>()
            {
                @Override
                public Integer call(Integer integer, Integer integer2)
                {
                    return integer * integer2;
                }
            }).subscribe(integer -> System.out.println("1-9相乘结果：" + integer));
        }
    
        /**
         * [简要描述]:聚合函数，与reduce类似，只不过会输出每一次的计算结果<br/>
         * [详细描述]:<br/>
         * 如：1~9 相加 = ?
         *
         * @return void
         * llxiao  2019/12/13 - 15:40
         **/
        @Test
        public void scan()
        {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
            Observable.from(numbers).scan((integer, integer2) -> integer + integer2)
                    .subscribe(integer -> System.out.println("Scan : " + integer));
        }
    
        /**
         * [简要描述]:滑动窗口<br/>
         * [详细描述]:<br/>
         * 计算 两两数据之间的值，组合成一个新的数组<br>
         * (1,2)(3,4)(5,6)(7,8)(9,10)->[3,7,11,15,19]<br>
         *
         * @return void
         * llxiao  2019/12/13 - 15:46
         **/
        @Test
        public void windowCount()
        {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
            // 取相邻的2个数计算，然后跳跃2个事件
            Observable.from(numbers).window(2, 2).subscribe(new Action1<Observable<Integer>>()
            {
                @Override
                public void call(Observable<Integer> integerObservable)
                {
                    integerObservable.reduce(new Func2<Integer, Integer, Integer>()
                    {
                        @Override
                        public Integer call(Integer integer, Integer integer2)
                        {
                            return integer + integer2;
                        }
                    }).subscribe(integer -> System.out.println("Window ->" + integer));
                }
            });
        }
    
        /**
         * [简要描述]:使用windows 一定时间内收集数据再发送出去<br/>
         * [详细描述]:<br/>
         * 如：将1S内收集到的数据发送出去，然后滑到第二个窗口
         *
         * @return void
         * llxiao  2019/12/13 - 15:58
         **/
        @Test
        public void windowTime() throws InterruptedException
        {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            Observable inputEventStream = Observable.create(subscriber -> subscriber.onNext("我是生产者.........."));
            // 每个1s中输出一次
            inputEventStream.window(1000, TimeUnit.MILLISECONDS).subscribe(new Action1()
            {
                @Override
                public void call(Object o)
                {
                    Calendar calendar = Calendar.getInstance();
                    int i = calendar.get(Calendar.SECOND);
                    System.out.println(i + " -->执行");
                }
            });
    
            //        inputEventStream.window(1000, TimeUnit.MILLISECONDS).subscribe(new Subscriber()
            //        {
            //            @Override
            //            public void onCompleted()
            //            {
            //
            //            }
            //
            //            @Override
            //            public void onError(Throwable e)
            //            {
            //
            //            }
            //
            //            @Override
            //            public void onNext(Object o)
            //            {
            //                Calendar calendar = Calendar.getInstance();
            //                int i = calendar.get(Calendar.SECOND);
            //                System.out.println(i + " -->执行");
            //            }
            //        });
            countDownLatch.await();
        }
    
        /**
         * [简要描述]:持续输入一组数字，按一定规则持续输出结果<br/>
         * [详细描述]:滑动窗口的监控过程<br/>
         * 按时间窗口移动输出相邻的两个数的值，以S为单位计数输出
         *
         * @return void
         * llxiao  2019/12/13 - 16:27
         **/
        @Test
        public void windowUltimateDemo() throws InterruptedException
        {
            // reduce 计算两个数字
            Func2<Integer, Integer, Integer> publicSum = (integer, integer2) -> integer + integer2;
    
            // 第一次统计，结合window(2,1)将输入的数字按 1:2,3:3,6:4,10:5 规则计算
            Func1<Observable<Integer>, Observable<Integer>> reduceFunc = integerObservable -> integerObservable
                    .reduce(0, publicSum);
    
            // 第二次攻击，将1s内生成的数据进行递归结算，可使用scan函数观测每次叠加的数据
            Func1<Observable<Integer>, Observable<Integer>> scanFunc = integerObservable -> integerObservable
                    .reduce(0, publicSum);
    
            // 程序计数等待器，防止主线程完成
            CountDownLatch countDownLatch = new CountDownLatch(1);
    
            // Subject可以实现一个消息向多个订阅者推送消息
            // BehaviorSubject将数据流中的最新值推送给接受者,每次接受者只会接受最新最送的那个消息
            // BehaviorSubject 跟 Subject 最大的不同就是 BehaviorSubject 是用来呈现当前的值，而不是单纯的发送事件。
            // BehaviorSubject 会记住最新一次发送的元素，并把该元素当作目前的值，在使用上 BehaviorSubject 建构式需要传入一个参数来代表起始的状态
            BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.create();
    
            behaviorSubject
                    // 1s作为一个基本块，进行横向滑动
                    .window(1000, TimeUnit.MILLISECONDS)
                    // 将flatMap汇总平铺成一个事件,然后累加成一个Observable<Integer>对象，比如说1s内有10个对象，被累加起来
                    .flatMap(reduceFunc)
                    //对这个对象2个发送，步长为1
                    .window(2, 1)
                    //对窗口里面的进行求和,用的scan, 每次累加都会打印出来
                    .flatMap(scanFunc)
                    .subscribe(integer -> System.out.println(Thread.currentThread().getName() + "-每秒统计输出结果：-> " + integer));
    
            for (int i = 0; i < 10000; i++)
            {
                behaviorSubject.onNext(i);
                //200ms产生一个数据
                Thread.sleep(200);
            }
            countDownLatch.await();
        }
    
    ```
 3. 参考文档<br>
 [Hystrix核心基础 - 滑动窗口创建过程及demo](https://www.cnblogs.com/itar/p/9013697.html)<br>
 [30 天精通 RxJS(23): Subject, BehaviorSubject, ReplaySubject, AsyncSubject](https://www.jianshu.com/p/982ea76a6bac)<br>