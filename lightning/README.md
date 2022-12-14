# lightning

#### 介绍
Gitee:https://gitee.com/achengovo/lightning.git

Github:https://github.com/achengovo/lightning.git

技术栈： netty、nacos、反射、动态代理、juc、序列化与反序列化

实现功能：服务注册与发现、负载均衡、Filter、优雅关闭

技术亮点：
1. 客户端使用动态代理技术使得调用远程方法和调用本地方法相同的体验；
2. 使用nacos作为注册中心，客户端可及时发现服务端节点的变更；
3. 实现了随机与加权随机负载均衡；
4. 实现了Filter功能，可使用Filter实现aop功能，例如打印调用日志等；
5. 实现了优雅关闭功能，服务方关机、重启不会造成调用失败；
   收获总结：通过写底层框架的经历，对动态代理、反射、多线程、线程安全、网络编程等技术的理解更加深刻。不仅加深技术能力、更提升思维高度，能全面地认识、思考框架的原理与架构。
