# infos
installation steps:
1. clone git repository to local
2. import project to eclipse
3. set project properties, encode to utf-8, jdk 
4. update configuration files, mvn clean build
5. copy and deploy infos.war to a web server

visit: http://localhost:$port?id=XXX&n=YYYY&d=ZZZZ&t=aaaa

id: id string from portal application, not use
n: name of user
d: department of user
t: timestamp

for example:http://localhost:8080/?id=210|240|214|218&n=%E5%82%85%E6%B5%A9%E6%9D%B0&d=%E7%AC%AC%E4%B8%89%E4%BA%8B%E4%B8%9A%E9%83%A8&t=1421805165842
