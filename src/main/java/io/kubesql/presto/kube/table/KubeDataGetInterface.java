package io.kubesql.presto.kube.table;

import io.kubernetes.client.openapi.models.V1Node;

public interface KubeDataGetInterface<T> {
    Object getData(T t);
    String getDataSrc();
}