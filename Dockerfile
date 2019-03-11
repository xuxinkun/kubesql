FROM centos:7
RUN yum install -y epel-release && yum install -y python2-pip && pip install kubernetes==8.0.1 bottle==0.12.16 prettytable==0.7.2 eventlet==0.24.1
WORKDIR /home/kubesql/
ADD https://github.com/xuxinkun/kubesql/archive/master.zip /home/kubesql/
RUN yum install -y unzip && unzip master.zip && cd kubesql-master && python setup.py install && cp -r etc/kubesql /etc