package io.kubesql.presto.kube.table;

import com.facebook.airlift.log.Logger;
import com.facebook.presto.spi.ColumnMetadata;
import com.facebook.presto.spi.SchemaTableName;
import com.facebook.presto.spi.type.BigintType;
import com.facebook.presto.spi.type.DoubleType;
import com.facebook.presto.spi.type.VarcharType;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.models.V1Node;
import io.kubesql.presto.kube.KubeColumn;
import io.kubesql.presto.kube.KubeTableHandle;
import io.kubesql.presto.kube.KubeTables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.kubesql.presto.kube.KubeMetadata.PRESTO_KUBESQL_SCHEMA;

public class KubeResTable {
    private static final Logger log = Logger.get(KubeResTable.class);

    public void registerTableCache(Map<SchemaTableName, Map<String, Map<String, Object>>> kubeResourceCache) {
        kubeResourceCache.put(getSchemaTableName(), getCache());
    }

    public Map<String, Map<String, Object>> getCache() {
        return null;
    }

    public KubeTables getKubeTables() {
        return null;
    }

    public String getTableName() {
        return null;
    }

    public Map<String, KubeColumn> getKubeColumn() {
        return null;
    }

    public void UpdateColumns(String columnName, KubeColumn column) {
        if (!getKubeColumn().containsKey(columnName)) {
            getKubeColumn().put(columnName, column);
            getKubeTables().tableColumns.put(getSchemaTableName(), getColumns());
        }
    }

    public void registerTable() {
        SchemaTableName schemaTableName = getSchemaTableName();
        KubeTableHandle tableHandle = new KubeTableHandle(schemaTableName);
        getKubeTables().tables.put(schemaTableName, tableHandle);
        getKubeTables().tableColumns.put(schemaTableName, getColumns());
    }

    public SchemaTableName getSchemaTableName() {
        return new SchemaTableName(PRESTO_KUBESQL_SCHEMA, getTableName());
    }

    public List<ColumnMetadata> getColumns() {
        List<ColumnMetadata> columnMetadataList = new ArrayList<>();
        for (String key : getKubeColumn().keySet()) {
            columnMetadataList.add(getKubeColumn().get(key).getColumnMetadata());
        }
        return columnMetadataList;
    }

    public void handleQuantity(Map<String, Quantity>  quantityMap, String prefix, Map<String, Object> data) {
        if (quantityMap != null){
            for (String key : quantityMap.keySet()) {
                String columnName = (prefix + key).toLowerCase();
                if (key.endsWith("cpu")) {
                    data.put(columnName, quantityMap.get(key).getNumber().doubleValue());
                    if (!getKubeColumn().containsKey(columnName)) {
                        UpdateColumns(columnName, new KubeColumn<Object>(columnName, DoubleType.DOUBLE, key));
                    }
                } else {
                    data.put(columnName, quantityMap.get(key).getNumber().longValue());
                    if (!getKubeColumn().containsKey(columnName)) {
                        UpdateColumns(columnName, new KubeColumn<Object>(columnName, BigintType.BIGINT, key));
                    }
                }

            }
        }
    }

    public void handleStringMap(Map<String, String>  map, String prefix, Map<String, Object> data) {
        if (map != null) {
            for (String key : map.keySet()) {
                String columnName = (prefix + key).toLowerCase();
                data.put(columnName, map.get(key));
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, VarcharType.createUnboundedVarcharType(), key));
                }
            }
        }
    }
}
