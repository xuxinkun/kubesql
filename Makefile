IMG ?= xuxinkub/kubesql:kubesql
KUBESQL_BUILD_IMAGE="kubesql:build"
KUBESQL_DOCKERHUB_IMAGE="xuxinkun/kubesql:latest"

build-dockerfile:
	docker build -t $(KUBESQL_BUILD_IMAGE) -f ./Dockerfile.build .

package:
	docker run -it --rm -v $(PWD):/home/kubesql $(KUBESQL_BUILD_IMAGE)

build-image:
	cd target && tar czvf kubesql.tar.gz *.jar
	docker build -t $(IMG) -f ./Dockerfile .

deploy-local:
	docker run -it -d --name kubesql -v /root/.kube/config:/home/presto/config xuxinkub/kubesql:lastest

build:
	docker build -t $(KUBESQL_DOCKERHUB_IMAGE) -f ./Dockerfile.hub .

deploy:
	docker run -it -d --name kubesql -v /root/.kube/config:/home/presto/config $(KUBESQL_DOCKERHUB_IMAGE)

undeploy:
	docker rm -f kubesql