一、专案介绍
本项目主要分为三个部分：模型搭建和训练、Java api编写和SSL加密
模型搭建和训练部分我主要使用tensorflow完成，Java api采用springboot进行编写，模型和相关参数放置于resources目录下，HTTPS采用Java SSL
二、环境建置与启动指令
（1）模型在jupyter notebook的环境搭建命令如下：
（！！！一定要先安装Visual C++再进行pip安装，如果先安装tensorflow后安装vs C++会导致失败）
首先使用jupyter notebook命令进入jupyter
pip install pandas
pip install numpy
pip install scikit-learn
pip install tensorflow
pip install pyarrow
pip install tf2onnx onnxruntime
pip install tqdm
（2）Java api环境：
使用maven对项目进行管理
jdk环境为java17
呼叫指令为http://localhost:443/predict_behavior（加密文件keystore.p12)
三、api测试样例：
输入：
{
  "events": [
    {"type": "view", "timestamp": "2023-07-20T10:00:00Z"},
    {"type": "view", "timestamp": "2023-07-20T10:01:00Z"},
    {"type": "addtocart", "timestamp": "2023-07-20T10:02:00Z"},
    {"type": "addtocart", "timestamp": "2023-07-20T10:03:00Z"},
    {"type": "addtocart", "timestamp": "2023-07-20T10:04:00Z"},
    {"type": "addtocart", "timestamp": "2023-07-20T10:05:00Z"},
    {"type": "addtocart", "timestamp": "2023-07-20T10:06:00Z"},
    {"type": "addtocart", "timestamp": "2023-07-20T10:07:00Z"},
    {"type": "addtocart", "timestamp": "2023-07-20T10:08:00Z"},
    {"type": "addtocart", "timestamp": "2023-07-20T10:09:00Z"},
    {"type": "addtocart", "timestamp": "2023-07-20T10:10:00Z"},
    {"type": "addtocart", "timestamp": "2023-07-20T10:11:00Z"}
  ]
}
api输出：
{
    "predictedAction": "addtocart",
    "probabilities": [
        0.9441185,
        0.010685589,
        0.045195907
    ]
}
四、作者简介与心得
作者简介：
心得：
整个过程收获颇丰，从模型环境的不断调试（有时候一个小小的问题会被卡住几个小时之久），到各种数据维度细节的对接，再到java api的编写和调试。
首先就是关于库的安装问题，看起来好像只需要pip几条命令就ok了
但实际运行的时候就会发现总是莫名其妙的出问题，明明前一天已经装好了VS C++
而且已经可以成功运行，第二天开启电脑再次运行就又不通了
重新配置环境变量、重新卸载掉VS C++再重装、重新建立新的环境
所有的Pip依赖全部重新装好依然不行，可以说几乎所有的办法都尝试了，然而报错始终都是那个报错。
经过作者反反复复的尝试，终于发现一定要先安装VS C++，再安装tensorflow才能成功
如果在没有VS C++的情况下安装了tensorflow，那么即使后面再重装无数个VS C++都是不行的
其次就是模型的保存代码调试问题，虽然之前的成果可以提前保存好，但模型每跑一次都要花费大量的时间
而很不巧的是作者在模型的保存代码中又碰到了几个需要调试很多次的Bug，修改bug并不困难，但每一次调试都需要重新花费大量时间将模型跑出来，真的很让人崩溃TXT
另外就是https的实现部分，原本作者使用NGINX已经基本实现
然而在用docker进行部署时nginx的镜像时却总是拉取不成功，各种国内的镜像库链接都尝试配置了却还是不行，只好重新改成使用SSL。
诸如此类的层出不穷的新奇的无法想象的Bug还有很多，很麻烦但当自己尝试了几个小时终于解决了这个无法想象的bug之后的喜悦又是铺天盖地的
学习了很多新的技术，也把自己的耐心锻炼到了一个新的高度。


