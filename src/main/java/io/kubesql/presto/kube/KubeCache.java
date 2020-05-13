package io.kubesql.presto.kube;

import com.facebook.airlift.log.Logger;
import com.facebook.presto.spi.SchemaTableName;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.informer.*;
import io.kubernetes.client.models.V1Node;
import io.kubernetes.client.models.V1NodeList;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.util.CallGeneratorParams;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.kubesql.presto.kube.table.KubeNodeTable;
import io.kubesql.presto.kube.table.KubePodTable;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
            client.getHttpClient().setReadTimeout(0, TimeUnit.MILLISECONDS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Configuration.setDefaultApiClient(client);

        SharedInformerFactory factory = new SharedInformerFactory();

        CoreV1Api coreV1Api = new CoreV1Api();

        // Node informer
        nodeInformer = factory.sharedIndexInformerFor(
                (CallGeneratorParams params) -> {
                    try {
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
                                null,
                                null);
                    } catch (ApiException e) {
                        throw new RuntimeException(e);
                    }
                },
                V1Node.class,
                V1NodeList.class);

        kubeNodeTable = new KubeNodeTable(kubeTables);

        nodeInformer.addEventHandler(
                new ResourceEventHandler<V1Node>() {
                    @Override
                    public void onAdd(V1Node node) {
                        kubeNodeTable.updateCache(node);
                        System.out.printf("%s node added!\n", node.getMetadata().getName());
                    }

                    @Override
                    public void onUpdate(V1Node oldNode, V1Node newNode) {
                        kubeNodeTable.updateCache(newNode);
                        System.out.printf(
                                "%s => %s node updated!\n",
                                oldNode.getMetadata().getName(), newNode.getMetadata().getName());
                    }

                    @Override
                    public void onDelete(V1Node node, boolean deletedFinalStateUnknown) {
                        kubeNodeTable.removeCache(node);
                        System.out.printf("%s node deleted!\n", node.getMetadata().getName());
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
                                null,
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
                        kubePodTable.updateCache(pod);
                        System.out.printf("%s pod added!\n", pod.getMetadata().getName());
                    }

                    @Override
                    public void onUpdate(V1Pod oldpod, V1Pod newpod) {
                        kubePodTable.updateCache(newpod);
                        System.out.printf(
                                "%s => %s pod updated!\n",
                                oldpod.getMetadata().getName(), newpod.getMetadata().getName());
                    }

                    @Override
                    public void onDelete(V1Pod pod, boolean deletedFinalStateUnknown) {
                        kubePodTable.removeCache(pod);
                        System.out.printf("%s pod deleted!\n", pod.getMetadata().getName());
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