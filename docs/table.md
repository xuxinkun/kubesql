# Tables

## nodes

|                          Column                          |   Type    | Extra | Comment |
|----------------------------------------------------------|-----------|-------|---------|
| allocatable.cpu                                          | double    |       |         |
| allocatable.devices.kubevirt.io/fuse                     | bigint    |       |         |
| allocatable.devices.kubevirt.io/kvm                      | bigint    |       |         |
| allocatable.devices.kubevirt.io/tun                      | bigint    |       |         |
| allocatable.devices.kubevirt.io/vhost-net                | bigint    |       |         |
| allocatable.ephemeral-storage                            | bigint    |       |         |
| allocatable.hugepages-1gi                                | bigint    |       |         |
| allocatable.hugepages-2mi                                | bigint    |       |         |
| allocatable.memory                                       | bigint    |       |         |
| allocatable.pods                                         | bigint    |       |         |
| annotations.flannel.alpha.coreos.com/backend-data        | varchar   |       |         |
| annotations.flannel.alpha.coreos.com/backend-type        | varchar   |       |         |
| annotations.flannel.alpha.coreos.com/kube-subnet-manager | varchar   |       |         |
| annotations.flannel.alpha.coreos.com/public-ip           | varchar   |       |         |
| annotations.node.alpha.kubernetes.io/ttl                 | varchar   |       |         |
| architecture                                             | varchar   |       |         |
| bootid                                                   | varchar   |       |         |
| capacity.cpu                                             | double    |       |         |
| capacity.devices.kubevirt.io/fuse                        | bigint    |       |         |
| capacity.devices.kubevirt.io/kvm                         | bigint    |       |         |
| capacity.devices.kubevirt.io/tun                         | bigint    |       |         |
| capacity.devices.kubevirt.io/vhost-net                   | bigint    |       |         |
| capacity.ephemeral-storage                               | bigint    |       |         |
| capacity.hugepages-1gi                                   | bigint    |       |         |
| capacity.hugepages-2mi                                   | bigint    |       |         |
| capacity.memory                                          | bigint    |       |         |
| capacity.pods                                            | bigint    |       |         |
| containerruntimeversion                                  | varchar   |       |         |
| creationtimestamp                                        | timestamp |       |         |
| deletiontimstamp                                         | timestamp |       |         |
| diskpressure.lastheartbeattime                           | timestamp |       |         |
| diskpressure.lasttransitiontime                          | timestamp |       |         |
| diskpressure.message                                     | varchar   |       |         |
| diskpressure.reason                                      | varchar   |       |         |
| diskpressure.status                                      | varchar   |       |         |
| kernelversion                                            | varchar   |       |         |
| kubeletversion                                           | varchar   |       |         |
| kubeproxyversion                                         | varchar   |       |         |
| labels.beta.kubernetes.io/arch                           | varchar   |       |         |
| labels.beta.kubernetes.io/os                             | varchar   |       |         |
| labels.kubernetes.io/hostname                            | varchar   |       |         |
| labels.kubernetes.io/role                                | varchar   |       |         |
| machineid                                                | varchar   |       |         |
| memorypressure.lastheartbeattime                         | timestamp |       |         |
| memorypressure.lasttransitiontime                        | timestamp |       |         |
| memorypressure.message                                   | varchar   |       |         |
| memorypressure.reason                                    | varchar   |       |         |
| memorypressure.status                                    | varchar   |       |         |
| name                                                     | varchar   |       |         |
| networkunavailable.lastheartbeattime                     | timestamp |       |         |
| networkunavailable.lasttransitiontime                    | timestamp |       |         |
| networkunavailable.message                               | varchar   |       |         |
| networkunavailable.reason                                | varchar   |       |         |
| networkunavailable.status                                | varchar   |       |         |
| operatingsystem                                          | varchar   |       |         |
| osimage                                                  | varchar   |       |         |
| outofdisk.lastheartbeattime                              | timestamp |       |         |
| outofdisk.lasttransitiontime                             | timestamp |       |         |
| outofdisk.message                                        | varchar   |       |         |
| outofdisk.reason                                         | varchar   |       |         |
| outofdisk.status                                         | varchar   |       |         |
| pidpressure.lastheartbeattime                            | timestamp |       |         |
| pidpressure.lasttransitiontime                           | timestamp |       |         |
| pidpressure.message                                      | varchar   |       |         |
| pidpressure.reason                                       | varchar   |       |         |
| pidpressure.status                                       | varchar   |       |         |
| port                                                     | integer   |       |         |
| ready.lastheartbeattime                                  | timestamp |       |         |
| ready.lasttransitiontime                                 | timestamp |       |         |
| ready.message                                            | varchar   |       |         |
| ready.reason                                             | varchar   |       |         |
| ready.status                                             | varchar   |       |         |
| systemuuid                                               | varchar   |       |         |
| uid                                                      | varchar   |       |         |

## pods
|                Column                 |   Type    | Extra | Comment |
|---------------------------------------|-----------|-------|---------|
| annotations.prometheus.io/path        | varchar   |       |         |
| annotations.prometheus.io/port        | varchar   |       |         |
| annotations.prometheus.io/scrape      | varchar   |       |         |
| containersready.lastprobetime         | timestamp |       |         |
| containersready.lasttransitiontime    | timestamp |       |         |
| containersready.message               | varchar   |       |         |
| containersready.reason                | varchar   |       |         |
| containersready.status                | varchar   |       |         |
| creationtimestamp                     | timestamp |       |         |
| deletiontimestamp                     | timestamp |       |         |
| dnspolicy                             | varchar   |       |         |
| hostip                                | varchar   |       |         |
| hostipc                               | boolean   |       |         |
| hostnetwork                           | boolean   |       |         |
| hostpid                               | boolean   |       |         |
| initialized.lastprobetime             | timestamp |       |         |
| initialized.lasttransitiontime        | timestamp |       |         |
| initialized.message                   | varchar   |       |         |
| initialized.reason                    | varchar   |       |         |
| initialized.status                    | varchar   |       |         |
| labels.app                            | varchar   |       |         |
| labels.app.kubernetes.io/component    | varchar   |       |         |
| labels.app.kubernetes.io/instance     | varchar   |       |         |
| labels.app.kubernetes.io/managed-by   | varchar   |       |         |
| labels.app.kubernetes.io/name         | varchar   |       |         |
| labels.control-plane                  | varchar   |       |         |
| labels.controller-revision-hash       | varchar   |       |         |
| labels.helm.sh/chart                  | varchar   |       |         |
| labels.jenkins/label                  | varchar   |       |         |
| labels.pod-template-generation        | varchar   |       |         |
| labels.pod-template-hash              | varchar   |       |         |
| labels.tier                           | varchar   |       |         |
| name                                  | varchar   |       |         |
| namespace                             | varchar   |       |         |
| nodename                              | varchar   |       |         |
| phase                                 | varchar   |       |         |
| podip                                 | varchar   |       |         |
| podscheduled.lastprobetime            | timestamp |       |         |
| podscheduled.lasttransitiontime       | timestamp |       |         |
| podscheduled.message                  | varchar   |       |         |
| podscheduled.reason                   | varchar   |       |         |
| podscheduled.status                   | varchar   |       |         |
| podsynclimitsready.lastprobetime      | timestamp |       |         |
| podsynclimitsready.lasttransitiontime | timestamp |       |         |
| podsynclimitsready.message            | varchar   |       |         |
| podsynclimitsready.reason             | varchar   |       |         |
| podsynclimitsready.status             | varchar   |       |         |
| priority                              | integer   |       |         |
| qosclass                              | varchar   |       |         |
| ready.lastprobetime                   | timestamp |       |         |
| ready.lasttransitiontime              | timestamp |       |         |
| ready.message                         | varchar   |       |         |
| ready.reason                          | varchar   |       |         |
| ready.status                          | varchar   |       |         |
| restartpolicy                         | varchar   |       |         |
| schedulername                         | varchar   |       |         |
| serviceaccount                        | varchar   |       |         |
| serviceaccountname                    | varchar   |       |         |
| starttime                             | timestamp |       |         |
| terminationgraceperiodseconds         | bigint    |       |         |
| uid                                   | varchar   |       |         |

## containers

|             Column              |   Type    | Extra | Comment|
|---------------------------------|-----------|-------|--------|
|args                             | varchar   |       |        |
|command                          | varchar   |       |        |
|containerid                      | varchar   |       |        |
|image                            | varchar   |       |        |
|imageid                          | varchar   |       |        |
|imagepullpolicy                  | varchar   |       |        |
|laststate.terminated.containerid | varchar   |       |        |
|laststate.terminated.exitcode    | integer   |       |        |
|laststate.terminated.finishedat  | timestamp |       |        |
|laststate.terminated.message     | varchar   |       |        |
|laststate.terminated.reason      | varchar   |       |        |
|laststate.terminated.signal      | integer   |       |        |
|laststate.terminated.startedat   | timestamp |       |        |
|limits.cpu                       | double    |       |        |
|limits.hugepages-2mi             | bigint    |       |        |
|limits.memory                    | bigint    |       |        |
|name                             | varchar   |       |        |
|privileged                       | boolean   |       |        |
|ready                            | boolean   |       |        |
|requests.cpu                     | double    |       |        |
|requests.hugepages-2mi           | bigint    |       |        |
|requests.memory                  | bigint    |       |        |
|restartcount                     | integer   |       |        |
|state.running.startedat          | timestamp |       |        |
|state.terminated.containerid     | varchar   |       |        |
|state.terminated.exitcode        | integer   |       |        |
|state.terminated.finishedat      | timestamp |       |        |
|state.terminated.message         | varchar   |       |        |
|state.terminated.reason          | varchar   |       |        |
|state.terminated.signal          | integer   |       |        |
|state.terminated.startedat       | timestamp |       |        |
|state.waiting.message            | varchar   |       |        |
|state.waiting.reason             | varchar   |       |        |
|status.image                     | varchar   |       |        |
|stdin                            | boolean   |       |        |
|stdinonce                        | boolean   |       |        |
|terminationmessagepath           | varchar   |       |        |
|terminationmessagepolicy         | varchar   |       |        |
|tty                              | boolean   |       |        |
|uid                              | varchar   |       |        |
|workingdir                       | varchar   |       |        |