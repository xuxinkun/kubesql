import ConfigParser
from kubernetes import client


def load_config():
    config = ConfigParser.ConfigParser()
    with open('/etc/kubesql/config', 'r') as cfgfile:
        config.readfp(cfgfile)
        port = config.getint('kubesql', 'port')
        ip = config.get('kubesql', 'ip')
        db_path = config.get('kubesql', 'db_path')
        param_path = config.get('kubesql', 'param_path')
        kubeconfig_path = config.get('kubesql', 'kubeconfig_path')
        return {"port": port,
                "ip": ip,
                "db_path": db_path,
                "param_path": param_path,
                "kubeconfig_path": kubeconfig_path}


def load_params(param_path="/etc/kubesql/params"):
    with open(param_path, 'r') as param_file:
        contents = param_file.read()
        parts = contents.strip().split("---")
        param_lists = []
        for part in parts:
            lines = part.strip().split("\n")
            if len(lines) > 2:
                resource = lines[0]
                param_lists.append({
                    "resource": resource,
                    "param_list": lines[1:]
                })
        return param_lists


def get_watch_args(resource):
    v1 = client.CoreV1Api()
    if resource == "pod":
        list_func = v1.list_pod_for_all_namespaces
        base_class_name = "V1Pod"
        table_name = "pods"
    elif resource == "node":
        list_func = v1.list_node
        base_class_name = "V1Node"
        table_name = "nodes"
    elif resource == "configmap":
        list_func = v1.list_config_map_for_all_namespaces
        base_class_name = "V1Pod"
        table_name = "configmaps"
    return {"list_func": list_func,
            "base_class_name": base_class_name,
            "table_name": table_name}
