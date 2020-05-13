/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.kubesql.presto.kube;

import com.facebook.airlift.log.Logger;
import com.facebook.presto.spi.predicate.TupleDomain;
import com.facebook.presto.spi.type.Type;
import com.facebook.presto.spi.HostAddress;
import com.facebook.presto.spi.RecordCursor;
import com.facebook.presto.spi.SchemaTableName;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import io.airlift.slice.Slice;
import io.airlift.slice.Slices;
import org.joda.time.DateTime;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.facebook.presto.spi.type.BigintType.BIGINT;
import static com.facebook.presto.spi.type.BooleanType.BOOLEAN;
import static com.facebook.presto.spi.type.DoubleType.DOUBLE;
import static com.facebook.presto.spi.type.IntegerType.INTEGER;
import static com.facebook.presto.spi.type.TimestampType.TIMESTAMP;
import static com.facebook.presto.spi.type.VarcharType.createUnboundedVarcharType;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.util.Objects.requireNonNull;

public class KubeRecordCursor
        implements RecordCursor {
    private static final Logger log = Logger.get(KubeRecordCursor.class);
    private static final Splitter LINE_SPLITTER = Splitter.on("\t").trimResults();
    public static final DateTimeFormatter ISO_FORMATTER = ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault());

    private final int[] fieldToColumnIndex;
    private final String[] fieldToColumnName;
    private final HostAddress address;
    private final List<KubeColumnHandle> columns;
    private final Iterator<Map<String, Object>> resources;
    private Map<String, Object> resourceData;


    public KubeRecordCursor(KubeTables kubeTables, List<KubeColumnHandle> columns, SchemaTableName tableName, HostAddress address, TupleDomain<KubeColumnHandle> predicate) {
        this.columns = requireNonNull(columns, "columns is null");
        this.address = requireNonNull(address, "address is null");

        fieldToColumnName = new String[columns.size()];
        fieldToColumnIndex = new int[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            KubeColumnHandle columnHandle = columns.get(i);
            fieldToColumnIndex[i] = columnHandle.getOrdinalPosition();
            fieldToColumnName[i] = columnHandle.getColumnName();
        }
        resources = kubeTables.getKubeCache().getCache(tableName).values().iterator();
    }


    @Override
    public long getCompletedBytes() {
        return 0;
    }

    @Override
    public long getReadTimeNanos() {
        return 0;
    }

    @Override
    public Type getType(int field) {
        checkArgument(field < columns.size(), "Invalid field index");
        return columns.get(field).getColumnType();
    }

    @Override
    public boolean advanceNextPosition() {
        if (resources.hasNext()) {
            resourceData = resources.next();
            return true;
        }
        return false;
    }

    private Object getFieldValue(int field) {
        String columnName = fieldToColumnName[field];
        return resourceData.get(columnName);
    }

    @Override
    public boolean getBoolean(int field) {
        checkFieldType(field, BOOLEAN);
        return (Boolean) (getFieldValue(field));
    }

    @Override
    public long getLong(int field) {
        if (getType(field).equals(TIMESTAMP)) {
            DateTime dt = (DateTime) getFieldValue(field);
            return dt.toInstant().getMillis();
        } else if (getType(field).equals(INTEGER)) {
            Integer value = (Integer) (getFieldValue(field));
            if (value != null) {
                return value.longValue();
            }
            return (Long) null;
        } else {
            checkFieldType(field, BIGINT);
            return (Long) (getFieldValue(field));
        }
    }

    @Override
    public double getDouble(int field) {
        checkFieldType(field, DOUBLE);
        return (Double) (getFieldValue(field));
    }

    @Override
    public Slice getSlice(int field) {
        checkFieldType(field, createUnboundedVarcharType());
        return Slices.utf8Slice((String) getFieldValue(field));
    }

    @Override
    public Object getObject(int field) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isNull(int field) {
        checkArgument(field < columns.size(), "Invalid field index");
        Object fieldValue = getFieldValue(field);
        return "null".equals(fieldValue) || fieldValue == null;
    }

    private void checkFieldType(int field, Type... expected) {
        Type actual = getType(field);
        for (Type type : expected) {
            if (actual.equals(type)) {
                return;
            }
        }
        String expectedTypes = Joiner.on(", ").join(expected);
        throw new IllegalArgumentException(format("Expected field %s to be type %s but is %s", field, expectedTypes, actual));
    }

    @Override
    public void close() {

    }

}
