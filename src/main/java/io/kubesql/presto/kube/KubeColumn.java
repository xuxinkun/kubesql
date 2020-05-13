package io.kubesql.presto.kube;

import com.facebook.presto.spi.type.Type;
import com.facebook.presto.spi.ColumnMetadata;
import io.kubesql.presto.kube.table.KubeDataGetInterface;

public class KubeColumn<T> implements KubeDataGetInterface<T> {
    public String columnName;
    public Type type;
    public String dataSrc;
    public String getDataType;

    @Override
    public Object getData(T t) {
        return null;
    }

    @Override
    public String getDataSrc() {
        return dataSrc;
    }

    public KubeColumn(String columnName, Type type) {
        this.columnName = columnName;
        this.type = type;
    }

    public KubeColumn(String columnName, Type type, String dataSrc) {
        this.columnName = columnName;
        this.type = type;
        this.dataSrc = dataSrc;
    }

    public ColumnMetadata getColumnMetadata() {
        return new ColumnMetadata(columnName, type);
    }
}
