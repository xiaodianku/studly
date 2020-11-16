rm -rf logs \
rm -rf caches \
rm -rf nohup.out \
kill -9 $(netstat -anp|grep 8085|awk '{printf $7}'|cut -d/ -f1) \
nohup java -jar system-1.jar &
