package io.kubesql.presto.kube;

import com.facebook.airlift.log.Logger;
import com.facebook.presto.spi.SchemaTableName;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.informer.*;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.CallGeneratorParams;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.kubesql.presto.kube.table.KubeNodeTable;
import io.kubesql.presto.kube.table.KubePodTable;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KubeCache {
    private static final Logger log = Logger.get(KubeCache.class);

    private SharedIndexInformer<V1Node> nodeInformer;

    private SharedIndexInformer<V1Pod> podInformer;

    public Map<SchemaTableName, Map<String, Map<String, Object>>> kubeResourceCache = new HashMap<SchemaTableName, Map<String, Map<String, Object>>>();

    public Map<String, Map<String, Object>> getCache(SchemaTableName tableName) {
        return kubeResourceCache.get(tableName);
    }

    private KubePodTable kubePodTable;
    private KubeNodeTable kubeNodeTable;

    public KubeCache(io.kubesql.presto.kube.KubeConfig config, KubeTables kubeTables) {
        ApiClient client = null;
        try {
            if (config.getKubeIncluster().equals("true")) {
                client = ClientBuilder.cluster().build();
            } else {
                String kubeConfigPath = config.getKubeConfigFile();
                log.info("read kube config file. %s", kubeConfigPath);
                client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Configuration.setDefaultApiClient(client);

        SharedInformerFactory factory = new SharedInformerFactory();

        CoreV1Api coreV1Api = new CoreV1Api();

        // Node informer
        nodeInformer = factory.sharedIndexInformerFor(
                        (CallGeneratorParams params) -> {
                            return coreV1Api.listNodeCall(
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    params.resourceVersion,
                                    params.timeoutSeconds,
                                    params.watch,
                                    null);
                        },
                        V1Node.class,
                        V1NodeList.class);

        kubeNodeTable = new KubeNodeTable(kubeTables);

        nodeInformer.addEventHandler(
                new ResourceEventHandler<V1Node>() {
                    @Override
                    public void onAdd(V1Node node) {
                        log.info("Receive node add event from %s", node.getMetadata().getName());
                        kubeNodeTable.updateCache(node);
                        log.info("Handle node add event from %s", node.getMetadata().getName());
                    }

                    @Override
                    public void onUpdate(V1Node oldNode, V1Node newNode) {
                        log.info("Receive node update event from %s", newNode.getMetadata().getName());
                        kubeNodeTable.updateCache(newNode);
                        log.info("Handle node update event from %s", newNode.getMetadata().getName());
                    }

                    @Override
                    public void onDelete(V1Node node, boolean deletedFinalStateUnknown) {
                        log.info("Receive node delete event from %s", node.getMetadata().getName());
                        kubeNodeTable.removeCache(node);
                        log.info("Handle node delete event from %s", node.getMetadata().getName());
                    }
                });



        podInformer = factory.sharedIndexInformerFor(
                (CallGeneratorParams params) -> {
                    try {
                        return coreV1Api.listPodForAllNamespacesCall(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                params.resourceVersion,
                                params.timeoutSeconds,
                                params.watch,
                                null);
                    } catch (ApiException e) {
                        throw new RuntimeException(e);
                    }
                },
                V1Pod.class,
                V1PodList.class);

        kubePodTable = new KubePodTable(kubeTables);

        podInformer.addEventHandler(
                new ResourceEventHandler<V1Pod>() {
                    @Override
                    public void onAdd(V1Pod pod) {
                        log.info("Receive pod add event from %s", pod.getMetadata().getName());
                        kubePodTable.updateCache(pod);
                        log.info("Handle pod add event from %s", pod.getMetadata().getName());
                    }

                    @Override
                    public void onUpdate(V1Pod oldpod, V1Pod newpod) {
                        log.info("Receive pod update event from %s", newpod.getMetadata().getName());
                        kubePodTable.updateCache(newpod);
                        log.info("Handle pod update event from %s", newpod.getMetadata().getName());
                    }

                    @Override
                    public void onDelete(V1Pod pod, boolean deletedFinalStateUnknown) {
                        log.info("Receive pod delete event from %s", pod.getMetadata().getName());
                        kubePodTable.removeCache(pod);
                        log.info("Handle pod delete event from %s", pod.getMetadata().getName());
                    }
                });

        factory.startAllRegisteredInformers();
        kubeNodeTable.registerTableCache(kubeResourceCache);
        kubePodTable.registerTableCache(kubeResourceCache);

//        V1Node node = nodeLister.get("noxu");
//        System.out.printf("noxu created! %s\n", node.getMetadata().getCreationTimestamp());
//        factory.stopAllRegisteredInformers();
//        Thread.sleep(3000);
//        System.out.println("informer stopped..");
    }
}