# RxJava2 + Retrofit2网络框架
[![](https://jitpack.io/v/PingerOne/RxNetGo.svg)](https://jitpack.io/#PingerOne/RxNetGo)

支持常用的GET和POST请求，支持同步和异步，使用简单方便快捷。

提供默认的Service，可以快速构建请求，支持手动构建Service，支持多个BaseUrl。

拥有四种强大的缓存功能：仅使用缓存，仅使用网络，先缓存后网络，先网络或缓存。

支持异步请求生命周期管理，管理好每一个请求，可以防止内存泄漏。

提供各种订阅者进行回调，空订阅者，Json订阅者，String订阅者，文件订阅者，图片订阅者和下载上传订阅者。

支持添加公共请求头和公共参数，支持每个请求动态添加请求头和参数。

支持在订阅者中手动解析数据，想要什么数据结构都能得到。

支持本地保存Cookie，支持保存在内存中或者保存Sp中。

支持下载多文件和断点续传，支持上传文件。

支持Https请求，可以设置安全证书。


## 添加依赖
1. 在项目根目录的build.gradle文件中添加jitpack仓库路径。

        allprojects {
            repositories {
                maven { url 'https://jitpack.io' }
            }
        }

2. 在app的build.gradle文件中引入仓库依赖。


       implementation 'com.github.PingerOne:RxNetGo:1.0.5'





