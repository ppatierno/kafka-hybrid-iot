# Secret creation with CA certificate, server certificate and key

A Secret is needed for storing the CA certificate, server certificate and key for the TLS support.

    oc create secret generic strimzi-kafka-bridge-certs \
    --from-file=ca.crt=./qdrouterd/certs/ca-cert.pem \
    --from-file=tls.crt=./qdrouterd/certs/server-cert.pem \
    --from-file=tls.key=./qdrouterd/certs/server-key.pem

After Secret creation, it needs to be mounted as volume for the Kafka bridge deployment.