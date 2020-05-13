# example

Query the pod info created after 2020-05-12.

```
presto:kube> select name, namespace,creationTimestamp from pods where creationTimestamp > date('2020-05-12') order by creationTimestamp desc;
                         name                         |        namespace        |    creationTimestamp    
------------------------------------------------------+-------------------------+-------------------------
 kube-api-webhook-controller-manager-7fd78ddd75-sf5j6 | kube-api-webhook-system | 2020-05-13 07:56:27.000 
(1 row)

Query 20200513_083356_00024_6xwbr, FINISHED, 1 node
Splits: 19 total, 19 done (100.00%)
0:00 [8 rows, 0B] [71 rows/s, 0B/s]
```