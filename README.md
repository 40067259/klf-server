# klf-server
It is a springboot project

BackEnd technologies: Java + springboot + REST + MVC + Jersy + XML + Jason + Maven + Token

DataBase: JDBC + JPA + Mysql

Deployment: Azure

3 ways to access it:

1.download it and find ServerKlfApplication class then run it

2.I deploy it on Azure cloud, https://klfserver.azurewebsites.net/. You can use postman to test it

3.watch my video demo:https://www.youtube.com/watch?v=xgaKXZqYN5k

Explain:

This app can divide 4 layers:

(1)Controller  handle the clients' requests

(2)Service  this layer provide services to controller and process the basic transactions

(3)DOA   Use mapper to communicate with data base, it provide services to service layer

(4)PoJo  They provides simple objects for all others layers
