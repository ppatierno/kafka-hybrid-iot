#!/bin/bash

oc create secret generic qdrouterd-certs \
    --from-file=ca.crt=qdrouterd/certs/ca-cert.pem \
    --from-file=tls.crt=qdrouterd/certs/server-cert.pem \
    --from-file=tls.key=qdrouterd/certs/server-key.pem
oc label secret qdrouterd-certs app=iot-demo

oc apply -f qdrouterd/deployment/qdrouterd-config.yml
oc apply -f qdrouterd/deployment/qdrouterd.yml
oc apply -f qdrouterd/deployment/qdrouterd-service.yml
oc apply -f qdrouterd/deployment/qdrouterd-route.yml