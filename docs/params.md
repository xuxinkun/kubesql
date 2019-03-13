# params

`params` is placed at `/etc/kubesql/params`.

The kubesql-watch uses `param_path` to handle the yaml from kube-apiserver and stores it to the sqlite3 db.

`params` has several parts. This means it can handle many kinds of resources from kubernetes.
 
Each part is divided by `---`. 

## part

part starts with the resource name from kubernetes in lower case, such as `node`, `pod`, `configmap`.

Then the real params follows. Each param is written in a line.

For example, a pod yaml is like this.

```
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: 2018-09-28T07:42:35Z
  name: watchtest
  namespace: wanwwiwu
  uid: 10b60b8e-c2f2-11e8-8cfe-e0db55138e34
spec:
  nodeName: 10.8.64.179
  schedulerName: default-scheduler
status:
  phase: Pending
```

In `param`, `metadata.name`, means that kubesql-watch will get the name of metadata,  `watchtest` and then write it to the `name` column of the row.

In default, kubesql-watch will use the string after the last **.** as the column name. Also, users can redefine the column name.
This coulnd be achieved by `{param} {redefine column name}`.
 
For example, `status.allocatable.cpu allocatable_cpu` means that `status.allocatable.cpu` will use `allocatable_cpu` as column name.

**Column names cannot be repeated.**