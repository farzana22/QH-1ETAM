# QH-1ETAM
## Prepared by Farzana Alam

### Branches: master & develop

#### Requirements: 
##### * Tomcat v8.0, * jre1.8

#### 1. Deploy from .war
* Istall Tomcat v8.0
* Copy HyperQueue.war to <Tomcat v8.0 install dir>/webapps
  * .war is attached in the email
* Start Tomcat v8.0 from <Tomcat v8.0 install dir>/bin/startup.bat (or startup.sh)
* Use project links from 2. Links section and follow steps in Test section
 
#### 2. Links
2.1 Producer role to register events in newUser queue: http://localhost:8080/HyperQueue/producer_newUser  
2.2 Producer role to register events in sysLog queue: http://localhost:8080/HyperQueue/producer_sysLog  
2.3 Consumer role to consume events in newUser queue: http://localhost:8080/HyperQueue/consumer_newUser  
2.4 Consumer role to consume events in sysLog queue: http://localhost:8080/HyperQueue/consumer_sysLog  

_P.S. change the port# from Tomcat's default 8080 to the one your server uses_

#### 3. Test
3.1 Producer:  
* Open any link for the producers (2.1 or 2.2)
* Enter Number in the TextField
* Click Register
* TextArea will be display POST response
3.2 Consumer:
* Open any link for the consumers (2.3 or 2.4)
* Click Consume
* TextArea will be display GET response
