import sqlite3
from kubernetes import client, config, watch
from multiprocessing import Process
from kubesql import utils
import re

cfg = utils.load_config()

conn = sqlite3.connect(cfg.get("db_path"))


def get_db_type(col_type):
    col_type = col_type.strip()
    if col_type == "str":
        return "char(200)"
    elif col_type == "int":
        return "int"
    elif col_type == "datetime":
        return "datetime"
    else:
        print "%s can not be handled." % col_type
        return None

dict_reg = re.compile("dict\((.*),(.*)\)")
base_type_set = set(["str","int","float"])


def get_col_lists(param_list, base_class_name):
    col_lists = []
    for item in param_list:
        class_type = base_class_name
        property_name_list = []
        item_list = item.split(" ")
        if item_list:
            param = item_list[0]
            col_name = None
            if len(item_list) > 1:
                col_name = item_list[1]
            for s in param.split("."):
                if s == "labels" or s == "annotations":
                    property_name_list.append({"type_name": s, "type": "property"})
                    property_name_list.append({"type_name": param.split(s+".")[1], "type": "dict"})
                    class_type = "str"
                    if col_name is None:
                        col_name = param.split(s+".")[1]
                    break
                base_class = getattr(client, class_type, None)
                if base_class:
                    for k, v in base_class.attribute_map.iteritems():
                        if v == s:
                            class_property_name = k
                            property_name_list.append({"type_name":class_property_name,"type":"property"})
                            class_type = base_class.swagger_types.get(class_property_name)
                elif class_type in base_type_set:
                    pass
                else:
                    group = dict_reg.match(class_type)
                    if group:
                        property_name_list.append({"type_name":s,"type":"dict"})
                        class_type = group.groups()[1]
                    else:
                        print "error", param, class_type
            if col_name is None:
                col_name = s
            col_lists.append({
                "col_path": property_name_list,
                "col_type": class_type,
                "col_dbtype": get_db_type(class_type),
                "col_name": col_name,
                "col_param": param
            })
    return col_lists


def create_sql(table_name, col_lists):
    col_sql = []
    for col in col_lists:
        col_sql.append("%s %s" % (col.get("col_name"), col.get("col_dbtype")))
    return "create table %s (" % table_name + ",".join(col_sql) + ");"


def transfer_object_to_db(object, col_lists):
    db_object = {}
    for col in col_lists:
        col_path = col.get("col_path")
        col_name = col.get("col_name")
        value = object
        for p in col_path:
            if value:
                path_type = p.get("type")
                type_name = p.get("type_name")
                if path_type == "property":
                    value = getattr(value, type_name)
                elif path_type == "dict":
                    value = value.get(type_name)
        db_object[col_name] = value
    return db_object


def insert_object_to_db(table_name, object, col_lists):
    db_object = transfer_object_to_db(object, col_lists)
    cols = []
    values = []
    mark = []
    for col, v in db_object.iteritems():
        cols.append(col)
        mark.append("?")
        values.append(v)
    sql = "insert into %s (" % table_name + ",".join(cols) + ") values (" + ",".join(mark) + ")"
    return sql, tuple(values)


def update_object_to_db(table_name, object, col_lists):
    db_object = transfer_object_to_db(object, col_lists)
    cols = []
    values = []
    for col, v in db_object.iteritems():
        cols.append("%s = ?" % col)
        values.append(v)
    values.append(object.metadata.uid)
    sql = "update %s set " % table_name+ ",".join(cols) + " where uid = ?"
    return sql, tuple(values)


def delete_object_in_db(table_name, object, col_lists):
    sql = "delete from %s where uid = ?" % table_name
    return sql, tuple([object.metadata.uid])


def wait_for_change(list_func, col_lists, table_name):
    cursor = conn.cursor()
    cursor.execute("DROP TABLE IF EXISTS %s" % table_name)
    cursor.execute(create_sql(table_name, col_lists))
    w = watch.Watch()
    stream = w.stream(list_func)
    for event in stream:
        eventType = event['type']
        pod = event['object']
        if eventType == "ADDED":
            sql = insert_object_to_db(table_name, pod, col_lists)
        elif eventType == "MODIFIED":
            sql = update_object_to_db(table_name, pod, col_lists)
        elif eventType == "DELETED":
            sql = delete_object_in_db(table_name, pod, col_lists)
        # print sql
        try:
            cursor.execute(*sql)
            conn.commit()
        except Exception, e:
            print sql
            print e
    cursor.close()


def main():
    config.load_kube_config(config_file=cfg.get("kubeconfig_path"))
    param_lists = utils.load_params(cfg.get("param_path"))
    process_list = []
    for p in param_lists:
        resource = p.get("resource")
        param_list = p.get("param_list")
        watch_args = utils.get_watch_args(resource)
        resource_col_lists = get_col_lists(param_list, watch_args.get("base_class_name"))
        resource_process = Process(target=wait_for_change, args=(watch_args.get("list_func"), resource_col_lists, watch_args.get("table_name")))
        resource_process.start()
        process_list.append(resource_process)
    for p in process_list:
        p.join()


if __name__ == '__main__':
    main()
