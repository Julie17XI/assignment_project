#!/bin/bash

# Define certificate details
COUNTRY="US"
STATE="New York"
LOCALITY="New York City"
ORGANIZATION="MyOrganization"
ORGANIZATIONAL_UNIT="DEV"
COMMON_NAME="www.example.com"
EMAIL="email@example.com"

# Generate a Private Key and a Certificate Signing Request (CSR)
openssl req -newkey rsa:2048 -nodes -keyout mykey.key -out mycsr.csr \
    -subj "/C=$COUNTRY/ST=$STATE/L=$LOCALITY/O=$ORGANIZATION/OU=$ORGANIZATIONAL_UNIT/CN=$COMMON_NAME/emailAddress=$EMAIL"

# Generate a Self-Signed Certificate
openssl x509 -signkey mykey.key -in mycsr.csr -req -days 365 -out mycertificate.crt

echo "Certificate Generated:"
openssl x509 -in mycertificate.crt -noout -text