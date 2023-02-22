pid=`ps -ef |grep java|grep saas-iss-bps|grep -v grep|awk '{print $2}'`
kill -5 $pid