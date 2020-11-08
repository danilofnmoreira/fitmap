#!/bin/bash

BASEDIR="$( cd "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"

run_maven() {

    docker run --rm \
               -w /build \
               -v $BASEDIR:/build \
               -v ~/.m2:/root/.m2 \
               maven:3-adoptopenjdk-11-openj9 $@
}

case $1 in
    up)
        docker-compose up -d
        ;;
    log)
        docker logs -f --tail 1000 fitmap.function.$2
        ;;
    down)
        docker-compose down
        ;;
    build)
        run_maven mvn clean package --batch-mode
        ;;
    check-deploy)
        gcloud meta list-files-for-upload
        ;;
    deploy)
        gcloud functions deploy $2 \
                                --region=southamerica-east1 \
                                --entry-point $3 \
                                --runtime java11 \
                                --trigger-http \
                                --max-instances 30
        ;;
    *)
        echo -e "Invalid option"
        ;;
esac
