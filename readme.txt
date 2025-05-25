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
整个过程收获颇丰，从模型环境的不断调试（有时候一个小小的问题会被卡住几个小时之久），到各种数据维度细节的对接，再到java api的编写和调试
总会有层出不穷的新奇的无法想象的Bug等待着自己去解决，很麻烦但当自己尝试了几个小时终于解决了这个无法想象的bug之后的喜悦又是铺天盖地的
学习了很多新的技术，也把自己的耐心锻炼到了一个新的高度。
