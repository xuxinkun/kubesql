package io.kubesql.presto.kube.table;

import com.facebook.airlift.log.Logger;
import com.facebook.presto.spi.SchemaTableName;
import com.facebook.presto.spi.type.*;
import io.kubernetes.client.openapi.models.*;
import io.kubesql.presto.kube.KubeColumn;
import io.kubesql.presto.kube.KubeTables;

import java.util.*;

public class KubePodTable extends KubeResTable {
    private static final Logger log = Logger.get(KubePodTable.class);
    public static String PodUid = "uid";
    public static String PodNameSpace = "namespace";
    public static String PodName = "name";
    public static String PodAnnotationsPrefix = "annotations.";
    public static String PodLabelsPrefix = "labels.";
    public static String PodCreationTimestamp = "creationtimestamp";
    public static String PodDeletionTimestamp = "deletiontimestamp";
    public static String PodDnsPolicy = "dnspolicy";
    public static String PodnodeName = "nodename";
    public static String Podpriority = "priority";
    public static String Podhostnetwork = "hostnetwork";
    public static String Podhostpid = "hostpid";
    public static String Podhostipc = "hostipc";
    public static String PodrestartPolicy = "restartpolicy";
    public static String PodschedulerName = "schedulername";
    public static String PodserviceAccount = "serviceaccount";
    public static String PodserviceAccountName = "serviceaccountName";
    public static String PodterminationGracePeriodSeconds = "terminationgraceperiodseconds";
    public static String PodhostIP = "hostip";
    public static String Podphase = "phase";
    public static String PodpodIP = "podip";
    public static String PodqosClass = "qosclass";
    public static String PodstartTime = "starttime";
    public static String ConditonLasttransitiontime = "lasttransitiontime";
    public static String ConditionLastprobetime = "lastprobetime";
    public static String ConditionReason = "reason";
    public static String ConditionMessage = "message";
    public static String ConditionStatus = "status";


    private final String TABLENAME = "pods";
    private Map<String, Map<String, Object>> cache = new HashMap<String, Map<String, Object>>();
    private KubeTables kubeTables;
    private KubeContainerTable kubeContainerTable;
    private Map<String, KubeColumn> kubeColumn = new TreeMap<String, KubeColumn>() {
        {
            put(PodUid, new KubeColumn<V1Pod>(PodUid, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getMetadata().getUid();
                }
            });
            put(PodName, new KubeColumn<V1Pod>(PodName, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getMetadata().getName();
                }
            });
            put(PodNameSpace, new KubeColumn<V1Pod>(PodNameSpace, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getMetadata().getNamespace();
                }
            });
            put(PodCreationTimestamp, new KubeColumn<V1Pod>(PodCreationTimestamp, TimestampType.TIMESTAMP) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getMetadata().getCreationTimestamp();
                }
            });
            put(PodDeletionTimestamp, new KubeColumn<V1Pod>(PodDeletionTimestamp, TimestampType.TIMESTAMP) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getMetadata().getDeletionTimestamp();
                }
            });
            put(PodDnsPolicy, new KubeColumn<V1Pod>(PodDnsPolicy, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getSpec().getDnsPolicy();
                }
            });
            put(PodnodeName, new KubeColumn<V1Pod>(PodnodeName, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getSpec().getNodeName();
                }
            });
            put(Podpriority, new KubeColumn<V1Pod>(Podpriority, IntegerType.INTEGER) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getSpec().getPriority();
                }
            });
            put(Podhostnetwork, new KubeColumn<V1Pod>(Podhostnetwork, BooleanType.BOOLEAN) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getSpec().getHostNetwork();
                }
            });
            put(Podhostpid, new KubeColumn<V1Pod>(Podhostpid, BooleanType.BOOLEAN) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getSpec().getHostPID();
                }
            });
            put(Podhostipc, new KubeColumn<V1Pod>(Podhostipc, BooleanType.BOOLEAN) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getSpec().getHostIPC();
                }
            });
            put(PodrestartPolicy, new KubeColumn<V1Pod>(PodrestartPolicy, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getSpec().getRestartPolicy();
                }
            });
            put(PodschedulerName, new KubeColumn<V1Pod>(PodschedulerName, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getSpec().getSchedulerName();
                }
            });
            put(PodserviceAccount, new KubeColumn<V1Pod>(PodserviceAccount, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getSpec().getServiceAccount();
                }
            });
            put(PodserviceAccountName, new KubeColumn<V1Pod>(PodserviceAccountName, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getSpec().getServiceAccountName();
                }
            });
            put(PodterminationGracePeriodSeconds, new KubeColumn<V1Pod>(PodterminationGracePeriodSeconds, BigintType.BIGINT) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getSpec().getTerminationGracePeriodSeconds();
                }
            });
            put(PodhostIP, new KubeColumn<V1Pod>(PodhostIP, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getStatus().getHostIP();
                }
            });
            put(Podphase, new KubeColumn<V1Pod>(Podphase, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getStatus().getPhase();
                }
            });
            put(PodpodIP, new KubeColumn<V1Pod>(PodpodIP, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getStatus().getPodIP();
                }
            });
            put(PodqosClass, new KubeColumn<V1Pod>(PodqosClass, VarcharType.createUnboundedVarcharType()) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getStatus().getQosClass();
                }
            });
            put(PodstartTime, new KubeColumn<V1Pod>(PodstartTime, TimestampType.TIMESTAMP) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getStatus().getStartTime();
                }
            });
        }
    };

    public KubePodTable(KubeTables kubeTables) {
        this.kubeTables = kubeTables;
        registerTable();
        kubeContainerTable = new KubeContainerTable(kubeTables);
    }

    public void registerTableCache(Map<SchemaTableName, Map<String, Map<String, Object>>> kubeResourceCache) {
        kubeResourceCache.put(getSchemaTableName(), getCache());
        kubeContainerTable.registerTableCache(kubeResourceCache);
    }

    public Map<String, Map<String, Object>> getCache() {
        return cache;
    }

    public KubeTables getKubeTables() {
        return kubeTables;
    }

    public V1ContainerStatus getContainerStatusByName(List<V1ContainerStatus> containerStatusList, String name) {
        if (containerStatusList != null) {
            for (V1ContainerStatus containerStatus : containerStatusList) {
                if (containerStatus.getName().equals(name)) {
                    return containerStatus;
                }
            }
        }
        return null;
    }

    public void updateCache(V1Pod pod) {
        Map<String, Object> podData = kubeAPIToData(pod);
        log.debug("update pod cache %s", pod.getMetadata().getName());
        getCache().put(pod.getMetadata().getUid(), podData);
        for (V1Container container : pod.getSpec().getContainers()) {
            V1ContainerStatus containerStatus = getContainerStatusByName(pod.getStatus().getContainerStatuses(), container.getName());
            kubeContainerTable.updateCache(pod.getMetadata().getUid(), container, containerStatus);
        }
    }

    public void removeCache(V1Pod pod) {
        log.debug("remove pod cache %s", pod.getMetadata().getName());
        getCache().remove(pod.getMetadata().getUid());
        for (V1Container container : pod.getSpec().getContainers()) {
            kubeContainerTable.removeCache(pod.getMetadata().getUid(), container);
        }
    }

    public String getTableName() {
        return TABLENAME;
    }

    public Map<String, KubeColumn> getKubeColumn() {
        return kubeColumn;
    }

    public Map<String, Object> kubeAPIToData(V1Pod pod) {
        Map<String, Object> podData = new HashMap<String, Object>();
        for (String key : getKubeColumn().keySet()) {
            KubeDataGetInterface kubeDataGetInterface = getKubeColumn().get(key);
            podData.put(key, kubeDataGetInterface.getData(pod));
        }
        Map<String, String> labels = pod.getMetadata().getLabels();
        handleStringMap(labels, PodLabelsPrefix, podData);
        Map<String, String> annotations = pod.getMetadata().getAnnotations();
        handleStringMap(annotations, PodAnnotationsPrefix, podData);

        List<V1PodCondition> conditions = pod.getStatus().getConditions();
        if (conditions != null) {
            for (V1PodCondition condition : conditions) {
                String columnNamePrefix = condition.getType().toLowerCase() + ".";
                String columnName;
                columnName = columnNamePrefix + ConditonLasttransitiontime;
                podData.put(columnName, condition.getLastTransitionTime());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, TimestampType.TIMESTAMP, columnName));
                }
                columnName = columnNamePrefix + ConditionLastprobetime;
                podData.put(columnName, condition.getLastProbeTime());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, TimestampType.TIMESTAMP, columnName));
                }
                columnName = columnNamePrefix + ConditionStatus;
                podData.put(columnName, condition.getStatus());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, VarcharType.createUnboundedVarcharType(), columnName));
                }
                columnName = columnNamePrefix + ConditionMessage;
                podData.put(columnName, condition.getMessage());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, VarcharType.createUnboundedVarcharType(), columnName));
                }
                columnName = columnNamePrefix + ConditionReason;
                podData.put(columnName, condition.getReason());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, VarcharType.createUnboundedVarcharType(), columnName));
                }
            }
        }
        return podData;
    }
}
