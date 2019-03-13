# config

config is placed at `/etc/kubesql/config`.

Default config is like this.

```
[kubesql]
port=415
ip=127.0.0.1
db_path=/dev/shm/kubesql.db
param_path=/etc/kubesql/params
kubeconfig_path=/etc/kubeconfig
```

- port: The kubesql-server listens on the `port` and the kubesql-client uses the `port` to connect to the kubesql-server.
- ip: The kubesql-client uses the `ip` and `port` to connect to the kubesql-server.
- db_path: The sqilite3 db is placed at `db_path`. `/dev/shm` is recommended. The `db_path` on the kubsql-server and the kubesql-watch must point to the same path.
- param_path: The kubesql-watch uses `param_path` to handle the yaml from kube-apiserver and stores it to the sqlite3 db.
- kubeconfig_path: The kubesql-watch uses `kubeconfig_path` to connect to the kube-apiserver.