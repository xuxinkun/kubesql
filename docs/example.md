# example

Query the CPU resources of each pod (requests and limits).


```
presto:kubesql> select pods.namespace,pods.name,sum("requests.cpu") as "requests.cpu" ,sum("limits.cpu") as "limits.cpu" from pods,containers where pods.uid = containers.uid group by pods.namespace,pods.name
     namespace     |                 name                 | requests.cpu | limits.cpu 
-------------------+--------------------------------------+--------------+------------
 rrrrqq-test-01    | rrrrqq-test-01-202005151652391759    |          0.8 |        8.0 
 lll-nopassword-18 | lll-nopassword-18-202005211645264618 |          0.1 |        1.0 
```

Query the remaining CPUs on each node.

```
presto:kubesql> select nodes.name, nodes."allocatable.cpu" - podnodecpu."requests.cpu" from nodes, (select pods.nodename,sum("requests.cpu") as "requests.cpu" from pods,containers where pods.uid = containers.uid group by pods.nodename) as podnodecpu where nodes.name = podnodecpu.nodename;
    name     |       _col1        
-------------+--------------------
 10.11.12.29 | 50.918000000000006 
 10.11.12.30 |             58.788 
 10.11.12.32 | 57.303000000000004 
 10.11.12.34 |  33.33799999999999 
 10.11.12.33 | 43.022999999999996 
```

Query all pods created after 2020-05-12.

```
presto:kube> select name, namespace,creationTimestamp from pods where creationTimestamp > date('2020-05-12') order by creationTimestamp desc;
                         name                         |        namespace        |    creationTimestamp    
------------------------------------------------------+-------------------------+-------------------------
 kube-api-webhook-controller-manager-7fd78ddd75-sf5j6 | kube-api-webhook-system | 2020-05-13 07:56:27.000 
```

Query pods with the appid label of `springboot`, and have not been scheduled successfully.

```
presto:kubesql> select namespace,name,phase from pods where phase = 'Pending' and "labels.appid" = 'springboot';
     namespace      |     name     |  phase  
--------------------+--------------+---------
 springboot-test-xx | v6ynsy3f73jn | Pending 
 springboot-test-xx | mu4zktenmttp | Pending 
 springboot-test-xx | n0yvpxxyvk4u | Pending 
 springboot-test-xx | dd2mh6ovkjll | Pending 
 springboot-test-xx | hd7b0ffuqrjo | Pending
 
 presto:kubesql> select count(*) from pods where phase = 'Pending' and "labels.appid" = 'springboot';
  _col0 
 -------
      5 
```