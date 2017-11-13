# ElasticSearch demo
我打算花这个周末(2016-09-24)的两天时间熟悉下ElasticSearch
从demo到源码分析
这样可以锻炼自己在短时间内快速深入获取知识的能力。

-----
1. 安装marvel插件
plugin.bat install elasticsearch/marvel/1.3.1
// 一般文档建议是 plugin.bat install elasticsearch/marvel/latest  
但是在windows的命令行下latest转向最新版本会不生效，那么久就在浏览器中访问下，看看最新版本是多少

即使按上述办法安装
仍然不能生效:

D:\600.self\05.code\04.java\12. my_demo_github\demo\07.ElasticSearch_demo\elasti
csearch-2.4.0\bin>plugin.bat install elasticsearch/marvel/1.3.1
-> Installing elasticsearch/marvel/1.3.1...
Trying https://download.elastic.co/elasticsearch/marvel/marvel-1.3.1.zip ...
Downloading ....................................................................
.......................................................DONE
Verifying https://download.elastic.co/elasticsearch/marvel/marvel-1.3.1.zip chec
ksums if available ...
NOTE: Unable to verify checksum for downloaded plugin (unable to find .sha1 or .
md5 file to verify)
ERROR: Could not find plugin descriptor 'plugin-descriptor.properties' in plugin
 zip
 
 有空细看下为什么
 
 --
 抓trace日志
 -javaagent:D:\600.self\05.code\04.java\TProfiler\tprofiler-0.2.jar  -Dorg.simonme.tracer.enableinit=true
 
 
 ## 2017 11再次拿起来继续学习
 这个放置一年多了 都没怎么继续分析下去，今天拿起来继续分析。  
 
 