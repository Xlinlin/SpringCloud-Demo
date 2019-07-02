设计到主要知识点:
 1. Zookeeper 临时节点创建
 2. CAS 操作
 3. MDC 线程安全的存放诊断日志的容器

```html
public ServerRunningMonitor(){
        // 创建父节点
        IZkDataListener dataListener = new IZkDataListener() {

            // ZK节点数据出现变更，比如：主动释放、激活、新增等
            public void handleDataChange(String dataPath, Object data) throws Exception {
                // 线程安全的存放诊断日志的容器,其实是放到当前线程的ThreadLocalMap中，不明所以？
                MDC.put("destination", destination);
                ServerRunningData runningData = JsonUtils.unmarshalFromByte((byte[]) data, ServerRunningData.class);

                // 如果不是本机节点
                if (!isMine(runningData.getAddress())) {
                    // 互斥设置false，内部使用CAS操作
                    mutex.set(false);
                }

                // zk上现有节点不活跃，且zk的节点是本机节点，说明是主动释放
                if (!runningData.isActive() && isMine(runningData.getAddress())) { // 说明出现了主动释放的操作，并且本机之前是active
                    release = true;
                    // 从zk上删除节点
                    releaseRunning();// 彻底释放mainstem
                }

                // 设置当前活动节点为zk上正在运行的节点
                activeData = (ServerRunningData) runningData;
            }

            // 节点删除
            public void handleDataDeleted(String dataPath) throws Exception {
                MDC.put("destination", destination);
                // 互斥设置false，内部使用CAS操作
                mutex.set(false);

                //  不为释放状态、活动节点不会为空且为本机节点，则即时触发一下active抢占
                if (!release && activeData != null && isMine(activeData.getAddress())) {
                    initRunning();
                } else {
                    // 否则就是等待delayTime，避免因网络瞬端或者zk异常，导致出现频繁的切换操作
                    delayExector.schedule(new Runnable() {
                        public void run() {
                            initRunning();
                        }
                    }, delayTime, TimeUnit.SECONDS);
                }
            }

        };

    }
    
    private void initRunning() {
    
            // 不是运行中直接返回
            if (!isStart()) {
                return;
            }
    
            // 生产当前运行的实例的节点信息：/otter/canal/destinations/{0} 实例信息如example
            String path = ZookeeperPathUtils.getDestinationServerRunning(destination);
            // 序列化 当前运行的节点，此处应该是本机的节点
            byte[] bytes = JsonUtils.marshalToByte(serverData);
            try {
                // 互斥false CAS操作  阻塞?
                mutex.set(false);
                // ZK上创建临时节点(带open ACL)并设置数据
                zkClient.create(path, bytes, CreateMode.EPHEMERAL);
                activeData = serverData;
                // 触发激活事件
                processActiveEnter();
                // 设置互斥  CAS操作，释放一下锁对象，唤醒一下阻塞的Thread
                mutex.set(true);
            } catch (ZkNodeExistsException e) {
                // 节点已经存在，或去数据
                bytes = zkClient.readData(path, true);
                if (bytes == null) {
                    // 节点中的数据为空，则立即出发抢占
                    initRunning();
                } else {
                    // 反之设置当前可用节点为ZK上的节点
                    activeData = JsonUtils.unmarshalFromByte(bytes, ServerRunningData.class);
                }
            } catch (ZkNoNodeException e) {
                // 不存在则创建父节点，尝试抢占动作
                zkClient.createPersistent(ZookeeperPathUtils.getDestinationPath(destination), true);
                initRunning();
            }
        }

```