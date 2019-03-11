import prettytable as pt
import argparse
import httplib
import json
from kubesql import utils

cfg = utils.load_config()

def get_kube_sql(sql):
    conn = httplib.HTTPConnection("%s:%s" % (cfg.get("ip"), cfg.get("port")))
    params = {'sql': sql}
    headers = {'Content-type': 'application/json'}
    conn.request("POST", "/sql", headers=headers, body=json.dumps(params))
    try:
        response = conn.getresponse()
        # print params, response.status, response.reason
        data = response.read()
        return json.loads(data)
    except Exception, e:
        print e, params
    conn.close()


def print_json_as_table(result):
    if result:
        tb = pt.PrettyTable()
        row = result[0]
        tb.field_names = row.keys()
        for row in result:
            row_value = []
            for field_name in tb.field_names:
                row_value.append(row.get(field_name))
            tb.add_row(row_value)
        tb.align = "l"
        print(tb)


parser = argparse.ArgumentParser()
parser.add_argument("sql", nargs="?", type=str, help="execte the sql.")
parser.add_argument("-t", "--table", help="increase output verbosity")
parser.add_argument("-a", "--all", action='store_true', help="show all tables")
args = parser.parse_args()


def main():
    if args.sql:
        result = get_kube_sql(args.sql)
    elif args.table:
        result = get_kube_sql('PRAGMA table_info(%s)' % args.table)
    elif args.all:
        result = get_kube_sql('SELECT name as table_name FROM sqlite_master WHERE type="table"')
    print_json_as_table(result)


if __name__ == '__main__':
    main()
