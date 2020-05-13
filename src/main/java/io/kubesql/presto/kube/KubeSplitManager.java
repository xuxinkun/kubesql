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

import com.facebook.presto.spi.predicate.TupleDomain;
import com.facebook.presto.spi.*;
import com.facebook.presto.spi.connector.ConnectorSplitManager;
import com.facebook.presto.spi.connector.ConnectorTransactionHandle;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class KubeSplitManager
        implements ConnectorSplitManager
{
    private final NodeManager nodeManager;

    @Inject
    public KubeSplitManager(NodeManager nodeManager)
    {
        this.nodeManager = requireNonNull(nodeManager, "nodeManager is null");
    }

    @Override
    public ConnectorSplitSource getSplits(
            ConnectorTransactionHandle transactionHandle,
            ConnectorSession session,
            ConnectorTableLayoutHandle layout,
            SplitSchedulingContext splitSchedulingContext)
    {
        KubeTableLayoutHandle layoutHandle = (KubeTableLayoutHandle) layout;
        KubeTableHandle tableHandle = layoutHandle.getTable();

        TupleDomain<KubeColumnHandle> effectivePredicate = layoutHandle.getConstraint()
                .transform(KubeColumnHandle.class::cast);

        List<ConnectorSplit> splits = nodeManager.getAllNodes().stream()
                .map(node -> new KubeSplit(node.getHostAndPort(), tableHandle.getSchemaTableName(), effectivePredicate))
                .collect(Collectors.toList());

        return new FixedSplitSource(splits);
    }
}
