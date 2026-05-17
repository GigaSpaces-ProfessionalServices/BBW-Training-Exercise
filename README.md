# BBW-Training

## Setup

### Create namespace
```sh
kubectl create ns dih
```

### Docker repos secret
```sh
kubectl create secret docker-registry myregistrysecret --docker-server=https://index.docker.io/v1/ --docker-username=dihcustomers --docker-password=<Password> --docker-email=dih-customers@gigaspaces.com --namespace dih 
```

### DIH repo
```sh
helm repo add dihrepo https://s3.amazonaws.com/resources.gigaspaces.com/helm-charts-dih
helm repo update
```

## DIH Install
```sh
kubectl create configmap grafana-dashboards --from-file=Grafana-Dashboards/ -n dih --dry-run=client -o yaml | kubectl apply -f -
helm upgrade --install dih dihrepo/dih --version 17.0.1-patch-b-1 -f ConfigFiles/dih.yaml --namespace dih
```


### Port forwarding for Spacedeck
```sh
nohup kubectl port-forward --address 0.0.0.0 svc/spacedeck 4200:4200 -n dih > spacedeck-port-forward.log &
```

### Port forwarding for Redpanda
```sh
nohup kubectl port-forward --address 0.0.0.0 svc/redpanda 8080:8080 -n dih > redpanda-port-forward.log &
```

### Port forwarding for kafka-rest
```sh
nohup kubectl port-forward --address 0.0.0.0 svc/cp-kafka-rest 8082:8082 -n dih > kafka-rest.log &
```

###  Install Space
```sh
helm upgrade --install couponhub dihrepo/xap-pu --version 17.0.1-patch-b-1 -f ConfigFiles/xap-pu-ts.yaml --namespace dih
``` 

### Transaction-data-pipeline.yml 
- **Exercise:** Add #promo type to DIH space

### Install Pluggable Connector
```sh
helm install bbw-pc dihrepo/pluggable-connector --version 17.0.1 --values ConfigFiles/couponhub-override-values.yaml --set-file dataPipelineYAML=ConfigFiles/Transaction-data-pipeline.yml -ndih
```

### Load Data
```sh
./scripts/load_data.sh
```

### Implement getTransactions method:
- **Exercise:** Implement getTransactions method in coupon-hub-api/src/main/java/com/bbw/coupon/hub/controller/SpaceController.java to retrieve all Transaction type records

### Coupon-hub-api build
```sh
./coupon-hub-api/docker.sh
```

### Coupon-hub-api deploy
```sh
./coupon-hub-api/deploy.sh
```

### Port forwarding for coupon-hub-api-svc
```sh
nohup kubectl port-forward --address 0.0.0.0 svc/coupon-hub-api-svc 8175:8175 -n dih > coupon-hub-api-svc.log &
```

### Test data
```sh
curl http://localhost:8175/transactions
```


## Uninstall

### Uninstall Coupon-hub-api
```sh
kubectl delete configmap coupon-hub-config -ndih
kubectl delete deployments coupon-hub-api -ndih
kubectl delete svc coupon-hub-api-svc -ndih
```

###  Uninstall Pluggable Connector
```sh
helm uninstall bbw-pc --namespace dih 
```

### Uninstall Space
```sh
helm uninstall couponhub --namespace dih 
kubectl get pvc -ndih --no-headers | awk '/couponhub/ {print $1}' | xargs -r kubectl delete pvc -ndih
```

### Uninstall Dih
```sh
helm uninstall dih --namespace dih
```