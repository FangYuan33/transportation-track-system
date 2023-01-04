## TTS

## 1. TTS组件MAVEN依赖图

![](images/新tts依赖关系.jpg)

- `tts-start`: 轨迹服务启动模块，包含启动类和一些必要的ApplicationRunner
- `tts-base`: 轨迹服务基础功能、组件模块，包含节点实现类和一些基础服务
- `tts-framework`: 框架支持，包含一些配置
- `tts-common`: 通用的枚举和工具类等...

其中图示上方为轨迹业务不同的实现，分别放在了不同的module里

- `tcsp-component-tts-remote`: 这个主要是开放一些通用的方法，达成jar包后供其他系统依赖使用
- `tcsp-component-tts-iov`: 车联网通用接口层，定义了三个通用的方法：
  **初始化属性**，**车辆最新位置**、**车辆轨迹信息** 以供具体业务做不同的实现；
  另外还使用了**工厂模式**，不同的轨迹业务服务（g7、中交兴路）没有被Spring管理起来，
  而是每次这些接口被调用时用工厂模式创建对象
- `tcsp-component-tts-iov-sinoiov`、`tcsp-component-tts-iov-g7`:
  分别为中交兴路，和g7轨迹业务的具体方法实现

问题

## 2. TTS服务
### 2.1 TTS节点
#### 2.1.1 实现原理
![](images/TTS组件实现原理类图.jpg)

- **LeaderSelectorListenerAdapter**: 实现节点的选举，分出Leader和Follower节点负责不同的职责
- **InitializingBean**: 初始化节点中的必要信息
- **Closeable**: 为了调用`close()`方法优雅的释放资源

`TtsNodeRunner`实现`ApplicationRunner`随服务启动，调用节点的启动方法，让zookeeper帮忙分配角色

- **BaseNodeHeartbeatService**: 服务节点心跳服务，随服务启动，每隔N秒更新服务节点心跳并记录心跳流水

#### 2.1.2 类型及其职责

- **Leader**: 负责分配任务和检查从节点任务的心跳状态
- **Follower**: 执行任务和更新任务的状态

### 2.2 集群服务高可用

使用zookeeper来实现对集群中节点的管理，并记录服务节点心跳来判断服务的可用性

### 2.3 任务的高可用
心跳机制

## 3. TTS开放接口
### 3.1 外部轨迹服务接入（G7）

![](images/ttsG7业务图.jpg)

1. `SystemRemoteService`开放通用接口方法供其他服务调用，
   将`tcsp-component-tts-remote`打成jar包，其他项目进行依赖即可
2. `SystemRemoteServiceImpl`实现`SystemRemoteService`做具体方法实现，
   依赖TTS轨迹服务
3. 在TTS轨迹服务`TtsIovSubscribeTaskService`中，调用`IovFactory`的工厂方法，
   根据**配置的轨迹业务不同创建不同的轨迹业务服务**。
   但是轨迹服务调用查询轨迹、车辆位置的方法时，每次都会调用一次工厂方法，创建对象。

### 3.2 APP轨迹服务接入
