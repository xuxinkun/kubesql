package io.kubesql.presto.kube.table;

import com.facebook.airlift.log.Logger;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.models.V1Node;

import java.util.*;

import com.facebook.presto.spi.type.*;
import com.facebook.presto.spi.type.VarcharType;
import io.kubernetes.client.openapi.models.V1NodeCondition;
import io.kubesql.presto.kube.*;


public class KubeNodeTable extends KubeResTable {
    private static final Logger log = Logger.get(KubeNodeTable.class);
    public static String TABLENAME = "nodes";
    public KubeTables kubeTables;
//    public static String NodeName = "metadata.name";
//    public static String NodeAnnotationsPrefix = "metadata.annotations";
//    public static String NodeLabelsPrefix = "metadata.labels";
//    public static String NodeCreationTimestamp = "metadata.creationtimestamp";
//    public static String NodeDeletionTimestamp ="metadata.deletiontimstamp";

    public static String NodeName = "name";
    public static String NodeUid = "uid";
    public static String NodeAnnotationsPrefix = "annotations.";
    public static String NodeLabelsPrefix = "labels.";
    public static String NodeCreationTimestamp = "creationtimestamp";
    public static String NodeDeletionTimestamp = "deletiontimstamp";
    public static String Nodeunschedulable = "unschedulable";
    //    public static String NodeAddressesPrexif = "addresses";
    public static String NodeAllocatablePrefix = "allocatable.";
    public static String NodeCapacityPrefix = "capacity.";
    //    public static String NodeConditionsPrefix = "conditions";
    public static String NodePort = "port";
    public static String NodeArchitecture = "architecture";
    public static String NodebootID = "bootid";
    public static String NodecontainerRuntimeVersion = "containerruntimeversion";
    public static String NodekernelVersione = "kernelversion";
    public static String NodekubeProxyVersion = "kubeproxyversion";
    public static String NodekubeletVersion = "kubeletversion";
    public static String NodemachineID = "machineid";
    public static String NodeoperatingSystem = "operatingsystem";
    public static String NodeosImage = "osimage";
    public static String NodesystemUUID = "systemuuid";

    public static String ConditonLasttransitiontime = "lasttransitiontime";
    public static String ConditionLastheartbeattime = "lastheartbeattime";
    public static String ConditionReason = "reason";
    public static String ConditionMessage = "message";
    public static String ConditionStatus = "status";

    public Map<String, KubeColumn> kubeColumn = new TreeMap<String, KubeColumn>() {
        {
            put(NodeUid, new KubeColumn<V1Node>(NodeUid, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Node v1Node) {
                    return v1Node.getMetadata().getUid();
                }
            });
            put(NodeName, new KubeColumn<V1Node>(NodeName, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Node v1Node) {
                    return v1Node.getMetadata().getName();
                }
            });
            put(NodeCreationTimestamp, new KubeColumn<V1Node>(NodeCreationTimestamp, TimestampType.TIMESTAMP) {
                @Override
                public Object getData(V1Node v1Node) {
                    return v1Node.getMetadata().getCreationTimestamp();
                }
            });
            put(NodeDeletionTimestamp, new KubeColumn<V1Node>(NodeDeletionTimestamp, TimestampType.TIMESTAMP) {
                @Override
                public Object getData(V1Node v1Node) {
                    return v1Node.getMetadata().getDeletionTimestamp();
                }
            });
            put(NodePort, new KubeColumn<V1Node>(NodePort, IntegerType.INTEGER) {
                @Override
                public Object getData(V1Node v1Node) {
                    return v1Node.getStatus().getDaemonEndpoints().getKubeletEndpoint().getPort();
                }
            });
            put(Nodeunschedulable, new KubeColumn<V1Node>(Nodeunschedulable, BooleanType.BOOLEAN) {
                @Override
                public Object getData(V1Node v1Node) {
                    return v1Node.getSpec().getUnschedulable();
                }
            });
            put(NodeArchitecture, new KubeColumn<V1Node>(NodeArchitecture, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Node v1Node) {
                    return v1Node.getStatus().getNodeInfo().getArchitecture();
                }
            });
            put(NodebootID, new KubeColumn<V1Node>(NodebootID, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Node v1Node) {
                    return v1Node.getStatus().getNodeInfo().getBootID();
                }
            });
            put(NodecontainerRuntimeVersion, new KubeColumn<V1Node>(NodecontainerRuntimeVersion, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Node v1Node) {
                    return v1Node.getStatus().getNodeInfo().getContainerRuntimeVersion();
                }
            });
            put(NodekernelVersione, new KubeColumn<V1Node>(NodekernelVersione, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Node v1Node) {
                    return v1Node.getStatus().getNodeInfo().getKernelVersion();
                }
            });
            put(NodekubeProxyVersion, new KubeColumn<V1Node>(NodekubeProxyVersion, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Node v1Node) {
                    return v1Node.getStatus().getNodeInfo().getKubeProxyVersion();
                }
            });
            put(NodekubeletVersion, new KubeColumn<V1Node>(NodekubeletVersion, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Node v1Node) {
                    return v1Node.getStatus().getNodeInfo().getKubeletVersion();
                }
            });
            put(NodemachineID, new KubeColumn<V1Node>(NodemachineID, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Node v1Node) {
                    return v1Node.getStatus().getNodeInfo().getMachineID();
                }
            });
            put(NodeoperatingSystem, new KubeColumn<V1Node>(NodeoperatingSystem, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Node v1Node) {
                    return v1Node.getStatus().getNodeInfo().getOperatingSystem();
                }
            });
            put(NodeosImage, new KubeColumn<V1Node>(NodeosImage, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Node v1Node) {
                    return v1Node.getStatus().getNodeInfo().getOsImage();
                }
            });
            put(NodesystemUUID, new KubeColumn<V1Node>(NodesystemUUID, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Node v1Node) {
                    return v1Node.getStatus().getNodeInfo().getSystemUUID();
                }
            });

        }
    };

    public Map<String, Map<String, Object>> getCache() {
        return cache;
    }

    private Map<String, Map<String, Object>> cache = new HashMap<String, Map<String, Object>>();

    public KubeTables getKubeTables() {
        return kubeTables;
    }

    public KubeNodeTable(KubeTables kubeTables) {
        this.kubeTables = kubeTables;
        registerTable();
    }

    public void updateCache(V1Node node) {
        Map<String, Object> podData = kubeAPIToData(node);
        cache.put(node.getMetadata().getName(), podData);
    }

    public void removeCache(V1Node node) {
        cache.remove(node.getMetadata().getName());
    }

    public String getTableName() {
        return TABLENAME;
    }

    public Map<String, KubeColumn> getKubeColumn() {
        return kubeColumn;
    }

    public Map<String, Object> kubeAPIToData(V1Node node) {
        Map<String, Object> nodeData = new HashMap<String, Object>();
        for (String key : kubeColumn.keySet()) {
            KubeDataGetInterface kubeDataGetInterface = kubeColumn.get(key);
            if (kubeDataGetInterface.getDataSrc() == null) {
                nodeData.put(key, kubeDataGetInterface.getData(node));
            }
        }
        Map<String, String> labels = node.getMetadata().getLabels();
        handleStringMap(labels, NodeLabelsPrefix, nodeData);
        Map<String, String> annotations = node.getMetadata().getAnnotations();
        handleStringMap(annotations, NodeAnnotationsPrefix, nodeData);
        Map<String, Quantity> allocatable = node.getStatus().getAllocatable();
        handleQuantity(allocatable, NodeAllocatablePrefix, nodeData);
        Map<String, Quantity> capacity = node.getStatus().getCapacity();
        handleQuantity(capacity, NodeCapacityPrefix, nodeData);

        List<V1NodeCondition> conditions = node.getStatus().getConditions();
        if (conditions != null) {
            for (V1NodeCondition condition : conditions) {
                String columnNamePrefix = condition.getType().toLowerCase();
                String columnName;
                columnName = columnNamePrefix + "." + ConditonLasttransitiontime;
                nodeData.put(columnName, condition.getLastTransitionTime());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, TimestampType.TIMESTAMP, columnName));
                }
                columnName = columnNamePrefix + "." + ConditionLastheartbeattime;
                nodeData.put(columnName, condition.getLastHeartbeatTime());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, TimestampType.TIMESTAMP, columnName));
                }
                columnName = columnNamePrefix + "." + ConditionStatus;
                nodeData.put(columnName, condition.getStatus());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, VarcharType.createUnboundedVarcharType(), columnName));
                }
                columnName = columnNamePrefix + "." + ConditionMessage;
                nodeData.put(columnName, condition.getMessage());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, VarcharType.createUnboundedVarcharType(), columnName));
                }
                columnName = columnNamePrefix + "." + ConditionReason;
                nodeData.put(columnName, condition.getReason());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, VarcharType.createUnboundedVarcharType(), columnName));
                }
            }
        }
        return nodeData;
    }
}
