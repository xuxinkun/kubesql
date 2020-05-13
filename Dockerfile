FROM centos:7
RUN yum install -y java-1.8.0-openjdk wget curl
RUN mkdir -p /home/presto && cd /home/presto &&\
    wget https://repo1.maven.org/maven2/com/facebook/presto/presto-server/0.234.2/presto-server-0.234.2.tar.gz &&\
    tar xzvf presto-server-0.234.2.tar.gz &&\
    rm -rf presto-server-0.234.2.tar.gz &&\
    mv presto-server-0.234.2 presto-server &&\
    wget https://repo1.maven.org/maven2/com/facebook/presto/presto-cli/0.234.2/presto-cli-0.234.2-executable.jar &&\
    chmod +x presto-cli-0.234.2-executable.jar &&\
    mv presto-cli-0.234.2-executable.jar /usr/local/bin/presto
WORKDIR /home/presto/presto-server
CMD bin/launcher start && presto --server localhost:8080 --catalog kubesql --schema kubesql
COPY etc/ etc/
ADD target/kubesql.tar.gz plugin/kubesql
