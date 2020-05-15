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
import com.facebook.presto.spi.ColumnMetadata;
import com.facebook.presto.spi.SchemaTableName;
import com.google.common.collect.ImmutableList;
import io.kubesql.presto.kube.table.KubeNodeTable;
import io.kubesql.presto.kube.table.KubePodTable;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public class KubeTables
{
    private static final Logger log = Logger.get(KubeTables.class);
    public Map<SchemaTableName, KubeTableHandle> tables;
    public Map<SchemaTableName, List<ColumnMetadata>> tableColumns;

    public KubeCache getKubeCache() {
        return kubeCache;
    }

    public void setKubeCache(KubeCache kubeCache) {
        this.kubeCache = kubeCache;
    }

    private KubeCache kubeCache;

    public KubeConfig getConfig() {
        return config;
    }

    public void setConfig(KubeConfig config) {
        this.config = config;
    }

    private KubeConfig config;

    @Inject
    public KubeTables(KubeConfig config)
    {
        this.tables = new HashMap<SchemaTableName, KubeTableHandle>();
        this.tableColumns = new HashMap<>();

        this.config = config;
        this.kubeCache = new KubeCache(config, this);
    }

    public KubeTableHandle getTable(SchemaTableName tableName)
    {
        return tables.get(tableName);
    }

    public List<SchemaTableName> getTables()
    {
        return ImmutableList.copyOf(tables.keySet());
    }

    public List<ColumnMetadata> getColumns(KubeTableHandle tableHandle)
    {
        checkArgument(tableColumns.containsKey(tableHandle.getSchemaTableName()), "Table %s not registered", tableHandle.getSchemaTableName());
        log.debug("get columns %s", tableColumns.get(tableHandle.getSchemaTableName()).toString());
        return tableColumns.get(tableHandle.getSchemaTableName());
    }
}
