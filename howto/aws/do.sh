INSTANCE_AMI="ami-1435f07c"
WALLET=/Users/calin/wallet
KEY=ramvark-keypair
KEY_PATH=$WALLET/$KEY.pem
SECURITY=allopen
WORKSPACE=/Users/calin/Documents/workspace-2
VERBOSE=   #-v
PROGRESS=  #--progress
SILENT=--silent
QUIET=-q

#~~ steps
LAUNCH=y         # launch new instance
WAIT=y           # wait for server to respond on http requests (empty for no)
STOP=y           # stop the web server
DEPLOY=y         # deploy
QA=y             # quality assurance
REDEPLOY=        #  loop
STRESS_TEST=     # stress
TERMINATE=y       # terminate instance
#~~



DTF=%F---%H:%M:%S-
COLR=

echo " ${COLR}• config"
echo " · workspace: $WORKSPACE"
echo " ·     image: $INSTANCE_AMI"
echo " ·  firewall: $SECURITY"
echo " ·       key: $KEY"
echo " ·      path: $KEY_PATH"
echo " ·   verbose: $VERBOSE"
echo
echo " ${COLR}• steps"
echo " ·    launch: $LAUNCH"
echo " ·      wait: $WAIT"
echo " ·      stop: $STOP"
echo " ·    deploy: $DEPLOY"
echo " ·        qa: $QA"
echo " ·      loop: $REDEPLOY"
echo " ·    stress: $STRESS_TEST"
echo " · terminate: $TERMINATE"
echo
if ! [ -d $WORKSPACE/a ];then echo workspace does not contain /a;exit 1;fi

if ! [ -z $LAUNCH ];then
	echo "`date +$DTF`  ${COLR}•  launching"
	for((;;));do
		aws ec2 run-instances --count 1 --image-id $INSTANCE_AMI --instance-type t1.micro --key-name $KEY --security-groups $SECURITY > reservation.txt
		if [ $? -eq 0 ];then break;fi
		echo "`date +$DTF`  ${COLR}·  trying to launch instance"
		sleep 1
	done
	INST=(`cat reservation.txt |grep INSTANCES|tr "\\t" "\n"`)
	INSTANCE_ID=${INST[7]}
	echo "`date +$DTF`  · $INSTANCE_ID"
	echo $INSTANCE_ID>instance.id
	
	echo "`date +$DTF`  ${COLR}•  waiting for address"
	for((;;));do
		aws ec2 describe-instances --instance-ids $INSTANCE_ID > reservations.txt;
		#cat reservations.txt;
		export INST=(`cat reservations.txt|grep ASSOCIATION|tr "\\t" "\n"`);
		INSTANCE_DNS=${INST[2]};
		if ! [ -z $INSTANCE_DNS ];then break;fi;
		echo "`date +$DTF`  ${COLR}·  waiting for address"
		sleep 1
	done;
fi
echo "`date +$DTF`  ·  $INSTANCE_DNS"
echo $INSTANCE_DNS>dns.id

if ! [ -z $WAIT ];then
	echo "`date +$DTF`  ${COLR}•  waiting for http://$INSTANCE_DNS/"
	for((;;));do curl $VERBOSE $SILENT --connect-timeout 60 $INSTANCE_DNS>wait_for_${INSTANCE_DNS}.html;if [ $? -eq 0 ];then break;fi;
		echo "`date +$DTF`  ${COLR}·  waiting for http://$INSTANCE_DNS/"
		sleep 1
	done
fi

if ! [ -z $STOP ];then
	echo "`date +$DTF`  ${COLR}•  stopping http://$INSTANCE_DNS"
	for((;;));do
		ssh $VERBOSE $QUIET -oBatchMode=yes -oStrictHostKeyChecking=no -oConnectTimeout=10 -i$KEY_PATH root@$INSTANCE_DNS "killall java"
		if [ $? -eq 0 ];then break;fi
		echo "`date +$DTF`  ${COLR}·  trying to stop http://$INSTANCE_DNS"
		sleep 1
	done
fi

if ! [ -z $DEPLOY ];then 
	for((;;));do
		echo "`date +$DTF`  ${COLR}•  updating http://$INSTANCE_DNS/ from $WORKSPACE/a/"
		for((;;));do
			#    --verbose  --progress                                                          -v 
			rsync $VERBOSE --timeout=30 $PROGRESS --delete --exclude .svn --exclude u/ --exclude cache/ -aze "ssh -oStrictHostKeyChecking=no -i$KEY_PATH" "$WORKSPACE/a/" root@$INSTANCE_DNS:/a/
			if [ $? -eq 0 ];then break;fi
			sleep 1
			echo "`date +$DTF`  ${COLR}· trying to update http://$INSTANCE_DNS/ from $WORKSPACE/a/"
		done
		echo "`date +$DTF`  ${COLR}•  restart web server on $INSTANCE_DNS"
		ssh $VERBOSE -oBatchMode=yes -oStrictHostKeyChecking=no -i$KEY_PATH root@$INSTANCE_DNS "killall -9 java ; /studio/suse-studio-custom &"
		if [ -z $REDEPLOY ];then break;fi
	done;
fi

if ! [ -z $QA ];then
	echo "`date +$DTF`  ${COLR}•  qa http://$INSTANCE_DNS"
	echo "`date +$DTF`  ${COLR}·  todo"
fi

if ! [ -z $STRESS_TEST ];then
	echo "`date +$DTF`  ${COLR}•  trying http://$INSTANCE_DNS"
	curl $VERBOSE  $INSTANCE_DNS
	curl $VERBOSE  $INSTANCE_DNS/typealine
	echo
	
	echo
	
	echo " •  stressing http://$INSTANCE_DNS"
	ab -c1    -t5 http://$INSTANCE_DNS/
	ab -c10   -t5 http://$INSTANCE_DNS/
	ab -c100  -t5 http://$INSTANCE_DNS/
	ab -c1    -t5 http://$INSTANCE_DNS/typealine
	ab -c10   -t5 http://$INSTANCE_DNS/typealine
	ab -c100  -t5 http://$INSTANCE_DNS/typealine
	ab -c1    -t5 http://$INSTANCE_DNS/qa.t013
	ab -c10   -t5 http://$INSTANCE_DNS/qa.t013
	ab -c100  -t5 http://$INSTANCE_DNS/qa.t013
	ab -c1    -t5 http://$INSTANCE_DNS/qa/t001.txt
	ab -c10   -t5 http://$INSTANCE_DNS/qa/t001.txt
	ab -c100  -t5 http://$INSTANCE_DNS/qa/t001.txt
fi # continuous build

if ! [ -z $TERMINATE ];then
	echo "`date +$DTF`  ${COLR}•  terminating $INSTANCE_ID"
	for((;;));do
		aws ec2 terminate-instances --instance-ids $INSTANCE_ID > aws_terminate_${INSTANCE_ID}.txt
		if [ $? -eq 0 ];then break;fi
		echo "`date +$DTF`  ${COLR}· trying to terminate instance $INSTANCE_ID"
		sleep 1
	done
fi

echo "`date +$DTF`  ${COLR}•${COLR}•  done"


