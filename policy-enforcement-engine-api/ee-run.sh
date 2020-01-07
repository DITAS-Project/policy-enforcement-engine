#! /bin/bash

function check {
    [ -z "$2" ] && echo "Need to set $1" && exit 1;
}

if [[ ! -z "$MINIO_URI" ]]; then
 check MINIO_ACCESS_KEY $MINIO_ACCESS_KEY
 check MINIO_SECRET_KEY $MINIO_SECRET_KEY
 check KMS_URI $KMS_URI
 check MINIO_FILE $MINIO_FILE

 sed -e "s,#{MINIO_URI},$MINIO_URI,g" \
    -e "s,#{MINIO_ACCESS_KEY},$MINIO_ACCESS_KEY,g" \
    -e "s,#{MINIO_SECRET_KEY},$MINIO_SECRET_KEY,g" \
    -e "s,#{KMS_URI},$KMS_URI,g" \
    -e "s,#{MINIO_FILE},$MINIO_FILE,g" \
    /app/policy-enforcement-engine-1.0/conf/demo-dpcm-DITAS.yml.sed > /app/policy-enforcement-engine-1.0/conf/demo-dpcm-DITAS.yml

 sed -e "s,#{MINIO_URI},$MINIO_URI,g" \
    -e "s,#{MINIO_ACCESS_KEY},$MINIO_ACCESS_KEY,g" \
    -e "s,#{MINIO_SECRET_KEY},$MINIO_SECRET_KEY,g" \
    -e "s,#{KMS_URI},$KMS_URI,g" \
    -e "s,#{MINIO_FILE},$MINIO_FILE,g" \
    /app/policy-enforcement-engine-1.0/conf/connections-DITAS.yml.sed > /app/policy-enforcement-engine-1.0/conf/connections-DITAS.yml

 sed -e "s,#{MINIO_URI},$MINIO_URI,g" \
    -e "s,#{MINIO_ACCESS_KEY},$MINIO_ACCESS_KEY,g" \
    -e "s,#{MINIO_SECRET_KEY},$MINIO_SECRET_KEY,g" \
    -e "s,#{KMS_URI},$KMS_URI,g" \
    -e "s,#{MINIO_FILE},$MINIO_FILE,g" \
    /app/policy-enforcement-engine-1.0/conf/application.conf.sed > /app/policy-enforcement-engine-1.0/conf/application.conf
fi

if [[ ! -z "$MYSQL_URI" ]]; then
 sed -e "s,#{MYSQL_URI},$MYSQL_URI,g" \
    -e "s,#{MYSQL_USERNAME},$MYSQL_USERNAME,g" \
    -e "s,#{MYSQL_PASSWORD},$MYSQL_PASSWORD,g" \
    /app/policy-enforcement-engine-1.0/conf/demo-dpcm-DITAS.yml.sed > /app/policy-enforcement-engine-1.0/conf/demo-dpcm-DITAS.yml
fi


_term() {
  echo "Caught SIGTERM signal!"
  kill -TERM "$child" 2>/dev/null
}

_int() {
  echo "Caught SIGINT signal!"
  kill -INT "$child" 2>/dev/null
}

trap _term SIGTERM
trap _int SIGINT

/app/policy-enforcement-engine-1.0/bin/policy-enforcement-engine -Dplay.http.secret.key='wspl4r' -Dconfig.file=/app/policy-enforcement-engine-1.0/conf/application.conf -Dpidfile.path=/dev/null &

child=$!
wait "$child"


