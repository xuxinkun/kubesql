import sys
from bottle import run, request, route
import json
import sqlite3
from kubesql import utils

cfg = utils.load_config()

conn = sqlite3.connect(cfg.get("db_path"))


@route('/sql', method='POST')
def do_sql():
    data = request.json
    sql = data.get('sql')
    if not (sql.lower().find("select") > -1  or sql.lower().find("pragma") > -1):
        return json.dumps([{"error":"sql must contains `select`."}])
    cursor = conn.cursor()
    cursor.execute(sql)
    values = cursor.fetchall()
    result = []
    for v in values:
        v_dict = {}
        i = 0
        for tuple in cursor.description:
            col_nam = tuple[0]
            v_dict[col_nam] = v[i]
            i += 1
        result.append(v_dict)
    cursor.close()
    return json.dumps(result)


def main():
    run(host='0.0.0.0', port=cfg.get("port"))


if __name__ == "__main__":
    sys.exit(main())
