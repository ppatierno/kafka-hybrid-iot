# Install on Fedora

Just run following command:

    yum install qpid-dispatch-router qpid-dispatch-tools

# Configuring TLS

For having TLS support for the AMQPS listener, a CA and a server (router) certificates are needed.

First we create the private key and self-signed certificate for the CA:

    openssl genrsa -out ca-key.pem 2048

    openssl req -new -sha256 -key ca-key.pem -out ca-csr.pem

    openssl x509 -req -in ca-csr.pem -signkey ca-key.pem -out ca-cert.pem

Then we create private keys and certificates, signed by the CA this time, for the client:

    openssl genrsa -out server-key.pem 2048

    openssl req -new -sha256 -key server-key.pem -out server-csr.pem

    openssl x509 -req -in server-csr.pem -CA ca-cert.pem -CAkey ca-key.pem -out server-cert.pem -CAcreateserial

# Running

Running with provided configuration file:

    qdrouterd --config qdrouterd.conf