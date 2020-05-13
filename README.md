# kubesql

kubesql is a tool to use sql to query the resources of kubernetes.

The resources of kubernetes such as nodes, pods and so on are handled as tables.

For example, all pods are easily to list from apiserver. But the number of pods on each node is not easy to caculate.

```
presto:kubesql> select nodename, count(*) as pod_count from pods group by nodename;
   nodename    | pod_count 
---------------+-----------
 10.111.11.118 |         8 
(1 row)

Query 20200513_094558_00006_7cycm, FINISHED, 1 node
Splits: 49 total, 49 done (100.00%)
0:00 [8 rows, 0B] [48 rows/s, 0B/s]
```

# architecture

kubesql makes use of **presto** to execute the sql. 

kubesql is a connector plugin of presto. It watches the node and pod event from kube-apiserver and update the cache.
And presto read cursor from the cache.

# install

## docker

In case the kubeconfig file is located at `/root/.kube/config`. Just run and enjoy.

```
[root@localhost kubesql]# docker run -it -d --name kubesql -v /root/.kube/config:/home/presto/config xuxinkub/kubesql:latest
[root@localhost kubesql]# docker exec -it kubesql presto --server localhost:8080 --catalog kubesql --schema kubesql
presto:kubesql> show tables;
   Table    
------------
 containers 
 nodes      
 pods       
(3 rows)

Query 20200513_094219_00003_7cycm, FINISHED, 1 node
Splits: 19 total, 19 done (100.00%)
0:01 [3 rows, 70B] [2 rows/s, 54B/s]
```

