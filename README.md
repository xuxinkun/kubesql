# kubesql

kubesql is a tool to use sql to query the resources of kubernetes.

The resources of kubernetes such as nodes, pods and so on are handled as the 

For example, all pods are easily to list from apiserver. But the number of pods on each node is not easy to caculate. 

With kubesql, a sql statement can achieve it like this.

```
[root@localhost kubesql]# kubesql "select hostIp, count(*) from pods group by hostIp"
+----------+----------------+
| count(*) | hostIP         |
+----------+----------------+
| 9        | None           |
| 4        | 22.2.22.222    |
| 14       | 11.1.111.11    |
+----------+----------------+
```

How many pod are pending

```
[root@localhost kubesql]# kubesql "select count(*) from pods where phase = 'Pending'"
+----------+
| count(*) |
+----------+
| 29       |
+----------+
```


# compoments

kubesql has three compoments.

- kubesql-watch: Watch the events from kube-apiserver, and write it to sqlite3.
- kubesql-server: Provide a http api for query. Accepts the sql query , execute the query in sqlite3 and return the query result.
- kubesql-client: Send the query sql to kubesql-server and get the result, then print the result in table format.

```
+----------------+  watch   +---------------+     +---------+
| kube-apiserver | -------> | kubesql-watch | --> | sqlite3 |
+----------------+          +---------------+     +---------+
                                                    ^
                                                    |
                                                    |
+----------------+  http    +---------------+       |
| kubesql-client | -------> | kubsql-server | ------+
+----------------+          +---------------+
```

# install and deploy 

## manualy install and deploy

install

```
//check out the code
pip install requirements.txt
python setup.py install
cp -r etc/kubesql /etc
```

check the config of `/etc/kubesql/config`, and modify the kubeconfig. kubeconfig is for kubesql-watch to connect to the apiserver.

```
nohup kubesql-watch &
nohup kubesql-server &
```


# Usage

kubesql command is short for kubesql-client. It is used to send the query and show the result in table. 

```
[root@localhost kubesql]# kubesql -h
usage: kubesql [-h] [-t TABLE] [-a] [sql]

positional arguments:
  sql                   execte the sql.

optional arguments:
  -h, --help            show this help message and exit
  -t TABLE, --table TABLE
                        increase output verbosity
  -a, --all             show all tables
```

`kubesql -a` can list the tables currently supported.

```
[root@localhost kubesql]# kubesql -a
+------------+
| table_name |
+------------+
| pods       |
| nodes      |
+------------+
```

And `kubesql -t {table_name}` can list the columns for `table_name` currently supported.

```
[root@localhost kubesql]# kubesql -t nodes
+-------------------------+-----+------------+---------+----+-----------+
| name                    | cid | dflt_value | notnull | pk | type      |
+-------------------------+-----+------------+---------+----+-----------+
| name                    | 0   | None       | 0       | 0  | char(200) |
| uid                     | 1   | None       | 0       | 0  | char(200) |
| creationTimestamp       | 2   | None       | 0       | 0  | datetime  |
| deletionTimestamp       | 3   | None       | 0       | 0  | datetime  |
| zone                    | 4   | None       | 0       | 0  | char(200) |
| allocatable_cpu         | 5   | None       | 0       | 0  | char(200) |
| allocatable_memory      | 6   | None       | 0       | 0  | char(200) |
| allocatable_pods        | 7   | None       | 0       | 0  | char(200) |
| capacity_cpu            | 8   | None       | 0       | 0  | char(200) |
| capacity_memory         | 9   | None       | 0       | 0  | char(200) |
| capacity_pods           | 10  | None       | 0       | 0  | char(200) |
| architecture            | 11  | None       | 0       | 0  | char(200) |
| containerRuntimeVersion | 12  | None       | 0       | 0  | char(200) |
| kubeProxyVersion        | 13  | None       | 0       | 0  | char(200) |
| kubeletVersion          | 14  | None       | 0       | 0  | char(200) |
| operatingSystem         | 15  | None       | 0       | 0  | char(200) |
| osImage                 | 16  | None       | 0       | 0  | char(200) |
+-------------------------+-----+------------+---------+----+-----------+
```
