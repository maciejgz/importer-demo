FROM alpine:edge
MAINTAINER mac@dasddaasd.dasd

RUN apk add --no-cache openjdk11

COPY target/importer-demo.war /opt/importer-demo/lib/

ENTRYPOINT ["/usr/bin/java"]

CMD ["-jar", "/opt/importer-demo/lib/importer-demo.war"]

VOLUME /var/lib/importer-demo/repo

EXPOSE 8080
