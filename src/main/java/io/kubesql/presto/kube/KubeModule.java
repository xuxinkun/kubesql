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

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

import static com.facebook.airlift.configuration.ConfigBinder.configBinder;
import static java.util.Objects.requireNonNull;

public class KubeModule
        implements Module
{
    private final String connectorId;

    public KubeModule(String connectorId)
    {
        this.connectorId = requireNonNull(connectorId, "connectorId is null");
    }

    @Override
    public void configure(Binder binder)
    {
        configBinder(binder).bindConfig(KubeConfig.class);

        binder.bind(KubeConnector.class).in(Scopes.SINGLETON);
        binder.bind(KubeMetadata.class).in(Scopes.SINGLETON);
        binder.bind(KubeSplitManager.class).in(Scopes.SINGLETON);
        binder.bind(KubeRecordSetProvider.class).in(Scopes.SINGLETON);
        binder.bind(KubeHandleResolver.class).in(Scopes.SINGLETON);
        binder.bind(KubeTables.class).in(Scopes.SINGLETON);

    }
}
