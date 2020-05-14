package io.kubesql.presto.kube.table;

import com.facebook.airlift.log.Logger;
import com.facebook.presto.spi.type.*;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.models.*;
import io.kubesql.presto.kube.KubeColumn;
import io.kubesql.presto.kube.KubeTables;

import java.util.*;

public class KubeContainerTable extends KubeResTable {
    private static final Logger log = Logger.get(KubeContainerTable.class);
//    public static String ContainerName = "metadata.spec.container.name";
//    public static String ContainerImage = "metadata.spec.container.image";
//    public static String ContainerImagePullPolicy = "metadata.spec.container.imagepullpolicy";
//    public static String ContainerStatusName = "metadata.status.container.name";
//    public static String ContainerStatusImage = "metadata.status.containerstatus.image";
//    public static String ContainerStatusImageID = "metadata.status.containerstatus.imageid";
//    public static String ContainerStautReady = "metadata.status.containerstatus.ready";
//    public static String ContainerStatusRestartCount = "metadata.status.containerstatus.restartcount";

    public static String ColumnPod = "pod";
    public static String ColumnContainer = "container";
    public static String ColumnContainerStatus = "containerstatus";
    public static String ContainerName = "name";
    public static String ContainerImage = "image";
    public static String ContainerImagePullPolicy = "imagepullpolicy";
    public static String ContainerLimitsPrefix = "limits.";
    public static String ContainerRequestsPrefix = "requests.";
    public static String ContainerworkingDir = "workingDir";
    public static String Containertty = "tty";
    public static String Containerstdin = "stdin";
    public static String Containerstdinonce = "stdinonce";
    public static String Containerprivileged = "privileged";
    public static String ContainerterminationMessagePath = "terminationmessagepath";
    public static String ContainerterminationMessagePolicy = "terminationmessagepolicy";
    public static String Containerargs = "args";
    public static String Containercommand = "command";
    public static String Containerenv = "env";
    public static String Containerports = "ports";
    public static String Containerlifecycle = "lifecycle";
    public static String ContainerlivenessProbe = "livenessProbe";
    public static String ContainerreadinessProbe = "readinessProbe";
    public static String ContainervolumeMounts = "volumemounts";
    public static String ContainervolumeDevices = "volumedevices";
    public static String ContainersecurityContext = "securitycontext";

    public static String ContainerStatusContainerID = "containerid";
    public static String ContainerStatusImage = "status.image";
    public static String ContainerStatusImageID = "imageid";
    public static String ContainerStautReady = "ready";
    public static String ContainerStatusRestartCount = "restartcount";
    public static String ContainerStatusStatePrefix = "state.";
    public static String ContainerStatusLastStatePrefix = "laststate.";
    public static String ContainerRunningPrefix = "running.";
    public static String ContainerRunningstartedAt = ContainerRunningPrefix + "startedAt";
    public static String ContainerTerminatedPrefix = "terminated.";
    public static String ContainerTerminatedcontainerID = ContainerTerminatedPrefix + "containerID";
    public static String ContainerTerminatedexitCode = ContainerTerminatedPrefix + "exitCode";
    public static String ContainerTerminatedfinishedAt = ContainerTerminatedPrefix + "finishedAt";
    public static String ContainerTerminatedmessage = ContainerTerminatedPrefix + "message";
    public static String ContainerTerminatedreason = ContainerTerminatedPrefix + "reason";
    public static String ContainerTerminatedsignal = ContainerTerminatedPrefix + "signal";
    public static String ContainerTerminatedstartedAt = ContainerTerminatedPrefix + "startedAt";
    public static String ContainerWaitingwaitingPrefix = "waiting.";
    public static String ContainerWaitingmessage = ContainerWaitingwaitingPrefix + "message";
    public static String ContainerWaitingreason = ContainerWaitingwaitingPrefix + "reason";

    private final String TABLENAME = "containers";
    private Map<String, Map<String, Object>> cache = new HashMap<String, Map<String, Object>>();
    private KubeTables kubeTables;

    private Map<String, KubeColumn> kubeColumn = new TreeMap<String, KubeColumn>() {
        {
            put(KubePodTable.PodUid, new KubeColumn<V1Pod>(KubePodTable.PodUid, VarcharType.createUnboundedVarcharType(), ColumnPod) {
                @Override
                public Object getData(V1Pod v1Pod) {
                    return v1Pod.getMetadata().getUid();
                }
            });
            put(ContainerName, new KubeColumn<V1Container>(ContainerName, VarcharType.createUnboundedVarcharType(), ColumnContainer) {
                @Override
                public Object getData(V1Container v1Container) {
                    return v1Container.getName();
                }
            });
            put(ContainerImage, new KubeColumn<V1Container>(ContainerImage, VarcharType.createUnboundedVarcharType(), ColumnContainer) {
                @Override
                public Object getData(V1Container v1Container) {
                    return v1Container.getImage();
                }
            });
            put(ContainerImagePullPolicy, new KubeColumn<V1Container>(ContainerImagePullPolicy, VarcharType.createUnboundedVarcharType(), ColumnContainer) {
                @Override
                public Object getData(V1Container v1Container) {
                    return v1Container.getImagePullPolicy();
                }
            });
            put(ContainerworkingDir, new KubeColumn<V1Container>(ContainerworkingDir, VarcharType.createUnboundedVarcharType(), ColumnContainer) {
                @Override
                public Object getData(V1Container v1Container) {
                    return v1Container.getWorkingDir();
                }
            });
            put(Containertty, new KubeColumn<V1Container>(Containertty, BooleanType.BOOLEAN, ColumnContainer) {
                @Override
                public Object getData(V1Container v1Container) {
                    return v1Container.getTty();
                }
            });
            put(Containerstdin, new KubeColumn<V1Container>(Containerstdin, BooleanType.BOOLEAN, ColumnContainer) {
                @Override
                public Object getData(V1Container v1Container) {
                    return v1Container.getStdin();
                }
            });
            put(Containerstdinonce, new KubeColumn<V1Container>(Containerstdinonce, BooleanType.BOOLEAN, ColumnContainer) {
                @Override
                public Object getData(V1Container v1Container) {
                    return v1Container.getStdinOnce();
                }
            });
            put(Containerprivileged, new KubeColumn<V1Container>(Containerprivileged, BooleanType.BOOLEAN, ColumnContainer) {
                @Override
                public Object getData(V1Container v1Container) {
                    if (v1Container.getSecurityContext() != null) {
                        return v1Container.getSecurityContext().getPrivileged();
                    }
                    return false;
                }
            });
            put(ContainerterminationMessagePath, new KubeColumn<V1Container>(ContainerterminationMessagePath, VarcharType.createUnboundedVarcharType(), ColumnContainer) {
                @Override
                public Object getData(V1Container v1Container) {
                    return v1Container.getTerminationMessagePath();
                }
            });
            put(ContainerterminationMessagePolicy, new KubeColumn<V1Container>(ContainerterminationMessagePolicy, VarcharType.createUnboundedVarcharType(), ColumnContainer) {
                @Override
                public Object getData(V1Container v1Container) {
                    return v1Container.getTerminationMessagePolicy();
                }
            });
            put(Containerargs, new KubeColumn<V1Container>(Containerargs, VarcharType.createUnboundedVarcharType(), ColumnContainer) {
                @Override
                public Object getData(V1Container v1Container) {
                    if (v1Container.getArgs() != null) {
                        return v1Container.getArgs().toString();
                    }
                    return null;
                }
            });
            put(Containercommand, new KubeColumn<V1Container>(Containercommand, VarcharType.createUnboundedVarcharType(), ColumnContainer) {
                @Override
                public Object getData(V1Container v1Container) {
                    if (v1Container.getCommand() != null) {
                        return v1Container.getCommand().toString();
                    }
                    return null;
                }
            });

            //container status
            put(ContainerStatusContainerID, new KubeColumn<V1ContainerStatus>(ContainerStatusContainerID, VarcharType.createUnboundedVarcharType(), ColumnContainerStatus) {
                @Override
                public Object getData(V1ContainerStatus containerStatus) {
                    return containerStatus.getContainerID();
                }
            });
            put(ContainerStatusImage, new KubeColumn<V1ContainerStatus>(ContainerStatusImage, VarcharType.createUnboundedVarcharType(), ColumnContainerStatus) {
                @Override
                public Object getData(V1ContainerStatus containerStatus) {
                    return containerStatus.getImage();
                }
            });
            put(ContainerStatusImageID, new KubeColumn<V1ContainerStatus>(ContainerStatusImageID, VarcharType.createUnboundedVarcharType(), ColumnContainerStatus) {
                @Override
                public Object getData(V1ContainerStatus containerStatus) {
                    return containerStatus.getImageID();
                }
            });
            put(ContainerStautReady, new KubeColumn<V1ContainerStatus>(ContainerStautReady, BooleanType.BOOLEAN, ColumnContainerStatus) {
                @Override
                public Object getData(V1ContainerStatus containerStatus) {
                    return containerStatus.getReady();
                }
            });
            put(ContainerStatusRestartCount, new KubeColumn<V1ContainerStatus>(ContainerStatusRestartCount, IntegerType.INTEGER, ColumnContainerStatus) {
                @Override
                public Object getData(V1ContainerStatus containerStatus) {
                    return containerStatus.getRestartCount();
                }
            });
        }
    };

    public Map<String, Map<String, Object>> getCache() {
        return cache;
    }

    public KubeTables getKubeTables() {
        return kubeTables;
    }

    public KubeContainerTable(KubeTables kubeTables) {
        this.kubeTables = kubeTables;
        registerTable();
    }

    public void updateCache(String uid, V1Container v1Container, V1ContainerStatus containerStatus) {
        log.debug("update container cache %s/%s", uid, v1Container.getName());
        Map<String, Object> containerData = kubeAPIToData(uid, v1Container, containerStatus);
        getCache().put(uid + v1Container.getName(), containerData);
    }

    public void removeCache(String uid, V1Container v1Container) {
        log.debug("remove container cache %s/%s", uid, v1Container.getName());
        getCache().remove(uid + v1Container.getName());
    }

    public String getTableName() {
        return TABLENAME;
    }

    public Map<String, KubeColumn> getKubeColumn() {
        return kubeColumn;
    }

    public Map<String, Object> kubeAPIToData(String uid, V1Container container, V1ContainerStatus containerStatus) {
        Map<String, Object> containerData = new HashMap<String, Object>();
        for (String key : getKubeColumn().keySet()) {
            KubeDataGetInterface kubeDataGetInterface = getKubeColumn().get(key);
            if (kubeDataGetInterface.getDataSrc().equals(ColumnPod)) {
                containerData.put(key, uid);
            } else if (kubeDataGetInterface.getDataSrc().equals(ColumnContainer)) {
                containerData.put(key, kubeDataGetInterface.getData(container));
            } else {
                if (containerStatus != null) {
                    // container status
                    containerData.put(key, kubeDataGetInterface.getData(containerStatus));
                }
            }
        }
        V1ResourceRequirements resourceRequirements = container.getResources();
        if (resourceRequirements != null) {
            Map<String, Quantity> limits = resourceRequirements.getLimits();
            handleQuantity(limits, ContainerLimitsPrefix, containerData);
            Map<String, Quantity> requests = resourceRequirements.getRequests();
            handleQuantity(requests, ContainerRequestsPrefix, containerData);
        }

        if (containerStatus != null) {
            handleState(containerStatus.getState(), ContainerStatusStatePrefix, containerData);
            handleState(containerStatus.getLastState(), ContainerStatusLastStatePrefix, containerData);
        }
        return containerData;
    }

    public void handleState(V1ContainerState state, String statePrefix, Map<String, Object> containerData) {
        String columnName;
        if (state != null) {
            if (state.getRunning() != null) {
                columnName = (statePrefix + ContainerRunningstartedAt).toLowerCase();
                containerData.put(columnName, state.getRunning().getStartedAt());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, TimestampType.TIMESTAMP, columnName));
                }
            }
            if (state.getWaiting() != null) {
                columnName = (statePrefix + ContainerWaitingmessage).toLowerCase();
                containerData.put(columnName, state.getWaiting().getMessage());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, VarcharType.createUnboundedVarcharType(), columnName));
                }
                columnName = (statePrefix + ContainerWaitingreason).toLowerCase();
                containerData.put(columnName, state.getWaiting().getReason());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, VarcharType.createUnboundedVarcharType(), columnName));
                }
            }
            if (state.getTerminated() != null) {
                columnName = (statePrefix + ContainerTerminatedcontainerID).toLowerCase();
                containerData.put(columnName, state.getTerminated().getContainerID());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, VarcharType.createUnboundedVarcharType(), columnName));
                }
                columnName = (statePrefix + ContainerTerminatedexitCode).toLowerCase();
                containerData.put(columnName, state.getTerminated().getExitCode());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, IntegerType.INTEGER, columnName));
                }
                columnName = (statePrefix + ContainerTerminatedreason).toLowerCase();
                containerData.put(columnName, state.getTerminated().getReason());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, VarcharType.createUnboundedVarcharType(), columnName));
                }
                columnName = (statePrefix + ContainerTerminatedmessage).toLowerCase();
                containerData.put(columnName, state.getTerminated().getMessage());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, VarcharType.createUnboundedVarcharType(), columnName));
                }
                columnName = (statePrefix + ContainerTerminatedsignal).toLowerCase();
                containerData.put(columnName, state.getTerminated().getSignal());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, IntegerType.INTEGER, columnName));
                }
                columnName = (statePrefix + ContainerTerminatedstartedAt).toLowerCase();
                containerData.put(columnName, state.getTerminated().getStartedAt());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, TimestampType.TIMESTAMP, columnName));
                }
                columnName = (statePrefix + ContainerTerminatedfinishedAt).toLowerCase();
                containerData.put(columnName, state.getTerminated().getFinishedAt());
                if (!getKubeColumn().containsKey(columnName)) {
                    UpdateColumns(columnName, new KubeColumn<V1Node>(columnName, TimestampType.TIMESTAMP, columnName));
                }
            }
        }

    }
}
