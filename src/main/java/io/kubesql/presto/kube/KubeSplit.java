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

import com.facebook.presto.spi.predicate.Domain;
import com.facebook.presto.spi.predicate.TupleDomain;
import com.facebook.presto.spi.type.Type;
import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.HostAddress;
import com.facebook.presto.spi.SchemaTableName;
import com.facebook.presto.spi.schedule.NodeSelectionStrategy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.facebook.presto.spi.schedule.NodeSelectionStrategy.HARD_AFFINITY;
import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

public class KubeSplit
        implements ConnectorSplit
{
    private final HostAddress address;
    private final SchemaTableName tableName;
    private final TupleDomain<KubeColumnHandle> effectivePredicate;

    @JsonCreator
    public KubeSplit(
            @JsonProperty("address") HostAddress address,
            @JsonProperty("tableName") SchemaTableName tableName,
            @JsonProperty("effectivePredicate") TupleDomain<KubeColumnHandle> effectivePredicate)
    {
        this.address = requireNonNull(address, "address is null");
        this.tableName = requireNonNull(tableName, "tableName is null");
        this.effectivePredicate = requireNonNull(effectivePredicate, "effectivePredicate is null");
    }

    @JsonProperty
    public HostAddress getAddress()
    {
        return address;
    }

    @JsonProperty
    public SchemaTableName getTableName()
    {
        return tableName;
    }

    @JsonProperty
    public TupleDomain<KubeColumnHandle> getEffectivePredicate()
    {
        return effectivePredicate;
    }

    @Override
    public NodeSelectionStrategy getNodeSelectionStrategy()
    {
        return HARD_AFFINITY;
    }

    @Override
    public List<HostAddress> getPreferredNodes(List<HostAddress> sortedCandidates)
    {
        return ImmutableList.of(address);
    }

    public List<HostAddress> getAddresses()
    {
        return ImmutableList.of(address);
    }

    @Override
    public Object getInfo()
    {
        return this;
    }

    @Override
    public String toString()
    {
        return toStringHelper(this)
                .add("address", address)
                .add("tableName", tableName)
                .toString();
    }
}
