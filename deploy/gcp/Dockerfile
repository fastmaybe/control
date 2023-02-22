# base image
# FROM ccr.ccs.tencentyun.com/ibu-iwms-web/springboot:v1.0-202108191100
FROM ccr.ccs.tencentyun.com/ibu-iwms-web/springboot:v1.0-202111151530
# add file
ADD saas-iss-bps-business/target/saas-iss-bps.zip /app/deploy/
RUN unzip /app/deploy/saas-iss-bps.zip -d /app/deploy/ \
    && rm /app/deploy/saas-iss-bps.zip