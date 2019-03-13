# examples

Query the name and namespace of pods on the node.

```
[root@localhost kubesql]# kubesql "select name, namespace from pods where hostIp = '172.22.160.107'"
+-----------+-----------------------------------------------------------+
| namespace | name                                                      |
+-----------+-----------------------------------------------------------+
| default   | imagetest1                                                |
| xutest    | dftest-b16f1ac7-0b56c2b9-v2bw2                            |
| default   | imagetest                                                 |
| xutest    | dftest-9da529db                                           |
+-----------+-----------------------------------------------------------+
```

Query the pod info created after 2019-03-12.

```
[root@localhost kubesql]# kubesql "select name, namespace,creationTimestamp from pods where creationTimestamp > datetime('2019-03-12') order by creationTimestamp desc"
+-----------+---------------------------+------------------+
| namespace | creationTimestamp         | name             |
+-----------+---------------------------+------------------+
| huck-test | 2019-03-12 07:59:36+00:00 | xxxxlog-v4-hvmsd |
| xutt      | 2019-03-12 02:45:40+00:00 | soxcs-a03b8302   |
+-----------+---------------------------+------------------+
```

Query the nodes with docker version `docker://1.10.3`.

```
[root@localhost kubesql]# kubesql "select name,containerRuntimeVersion from nodes where containerRuntimeVersion = 'docker://1.10.3'"
+----------------+-------------------------+
| name           | containerRuntimeVersion |
+----------------+-------------------------+
| 111.22.111.31  | docker://1.10.3         |
| 11.3.22.201    | docker://1.10.3         |
+----------------+-------------------------+
```