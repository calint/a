INSTANCE_AMI="ami-b0659fd8"
WALLET=/Users/calin/wallet
KEY=ramvark-keypair
KEY_PATH=$WALLET/$KEY.pem
SECURITY=allopen
WORKSPACE=/Users/calin/Documents/workspace-2

#~~ steps
LAUNCH=y         # launch new instance
WAIT=y           # wait for server to respond on http requests (empty for no)
STOP=y           # stop the web server
DEPLOY=y         # deploy
REDEPLOY=        #  loop
STRESS_TEST=     # stress
TERMINATE=y      # terminate instance
#~~



DTF=%F---%H:%M:%S-

echo " • config"
echo " · workspace: $WORKSPACE"
echo " ·     image: $INSTANCE_AMI"
echo " ·  firewall: $SECURITY"
echo " ·       key: $KEY"
echo " ·      path: $KEY_PATH"
echo
echo " • steps"
echo " ·    launch: $LAUNCH"
echo " ·      wait: $WAIT"
echo " ·      stop: $STOP"
echo " ·    deploy: $DEPLOY"
echo " ·      loop: $REDEPLOY"
echo " ·    stress: $STRESS_TEST"
echo " · terminate: $TERMINATE"

if ! [ -d $WORKSPACE/a ];then echo workspace does not contain /a;exit 1;fi

if ! [ -z $LAUNCH ];then
	echo "`date +$DTF`  •  launching instance"
	for((;;));do
		aws ec2 run-instances --count 1 --image-id $INSTANCE_AMI --instance-type t1.micro --key-name $KEY --security-groups $SECURITY > reservation.txt
		if [ $? -eq 0 ];then break;fi
		sleep 1
		echo
		echo "  ·  trying to launch instance"
	done
	INST=(`cat reservation.txt |grep INSTANCES|tr "\\t" "\n"`)
	INSTANCE_ID=${INST[7]}
	echo "  · $INSTANCE_ID"
	
	echo "`date +$DTF`  •  waiting for public address"
	for((;;));do
		aws ec2 describe-instances --instance-ids $INSTANCE_ID > reservations.txt;
		#cat reservations.txt;
		export INST=(`cat reservations.txt|grep ASSOCIATION|tr "\\t" "\n"`);
		INSTANCE_DNS=${INST[2]};
		echo "  · $INSTANCE_DNS"
		if ! [ -z $INSTANCE_DNS ];then break;fi;
		sleep 1
	done;
fi

if ! [ -z $WAIT ];then
	echo
	echo "`date +$DTF`  •  waiting for http://$INSTANCE_DNS/"
	for((;;));do curl --connect-timeout 10 --verbose $INSTANCE_DNS;if [ $? -eq 0 ];then break;fi;
	    echo
		echo "  ·  waiting for http://$INSTANCE_DNS/"
	done
	echo
fi

if ! [ -z $STOP ];then
	echo
	echo "`date +$DTF`  •  stopping web server on $INSTANCE_DNS"
	for((;;));do
		ssh -v -o "StrictHostKeyChecking no" -o "PasswordAuthentication no" -o "ConnectTimeout 2" -i $KEY_PATH root@$INSTANCE_DNS "killall -9 java"
		if [ $? -eq 0 ];then break;fi
		sleep 1
		echo
		echo "  ·  trying to stop the web server on $INSTANCE_DNS"
	done
fi

if ! [ -z $DEPLOY ];then 
	for((;;));do
		echo
		echo "`date +$DTF`  •  updating $INSTANCE_DNS from $WORKSPACE/a"
		for((;;));do
			#    --verbose  --progress                                                          -v 
			rsync --timeout=30 --progress --delete --verbose --exclude .svn --exclude u/ --exclude cache/ -aze "ssh -o StrictHostKeyChecking=no -i $KEY_PATH" "$WORKSPACE/a/" root@$INSTANCE_DNS:/a/
			if [ $? -eq 0 ];then break;fi
			sleep 1
			echo
			echo "  · trying to update $INSTANCE_DNS from $WORKSPACE/a"
		done
		
		echo
		echo "`date +$DTF`  •  restart web server on $INSTANCE_DNS"
		ssh -o StrictHostKeyChecking=no -o PasswordAuthentication=no -i $KEY_PATH root@$INSTANCE_DNS "killall -9 java ; /studio/suse-studio-custom &"
	
		if [ -z $REDEPLOY ];then break;fi
	done;
fi

if ! [ -z $STRESS_TEST ];then
	echo
	echo "`date +$DTF`  •  trying http://$INSTANCE_DNS/"
	curl --verbose $INSTANCE_DNS
	curl --verbose $INSTANCE_DNS/typealine
	echo
	
	echo
	echo "`date +$DTF`  •  qa http://$INSTANCE_DNS"
	echo todo
	
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
	echo
	echo "`date +$DTF`  •  terminating $INSTANCE_ID"
	for((;;));do
		aws ec2 terminate-instances --instance-ids $INSTANCE_ID
		if [ $? -eq 0 ];then break;fi
		sleep 1
		echo
		echo "  · trying to terminate instance $INSTANCE_ID"
	done
fi

echo
echo "`date +$DTF`  ••  done"
 
