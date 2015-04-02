IP=$(if [ -z $1 ];then echo 104.131.63.23;else echo $1;fi)
WS=$(realpath $(if [ -z $2 ];then echo /home/c/w;else echo $2;fi))
VERBOSE=--verbose
#VERBOSE=   # silent
CMD_RESET='killall java;/etc/rc.local>/var/log/ramvark.log&'
SSH_OPTS='-oBatchMode=yes -oStrictHostKeyChecking=no -oConnectTimeout=60'

DTF=%F---%H:%M:%S-
COLR=

echo "`date +$DTF`  ${COLR}•  update $IP:/a/ from $WS/a/"
while :;do
	rsync --recursive $VERVOSE --exclude .svn --exclude u/ $WS/a/  root@$IP:/a/
	if [ $? -eq 0 ];then break;fi
	sleep 1
	echo "`date +$DTF`  ${COLR}·  waiting for update $IP:/a/ from $WS/a/"
done

echo "`date +$DTF`  ${COLR}•  reset $IP with '$CMD_RESET'"
#echo $SSH_OPTS
while :;do
	ssh $SSH_OPTS root@$IP $CMD_RESET
	if [ $? -eq 0 ];then break;fi
	sleep 1
	echo "`date +$DTF`  ${COLR}·  waiting for $IP:to ssh"
done

echo "`date +$DTF`  ${COLR}•  wait for http://$IP"
while :;do
	curl $IP
	if [ $? -eq 0 ];then break;fi
	sleep 1
	echo "`date +$DTF`  ${COLR}·  waiting for $IP:to respond on port 80"
done

#while [ $(curl $IP) -neq 0 ]; do echo "`date +$DTF`  ${COLR}·  waiting for http://$IP";sleep 1;done;
#curl $IP

echo "`date +$DTF`  ${COLR}•  pid"
ssh $SSH_OPTS root@$IP 'ps aux|grep java|sed /grep/d|tr -s " "|cut -d " " -f2'

echo "`date +$DTF`  ${COLR}•  done"
