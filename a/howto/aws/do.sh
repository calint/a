export INSTANCE_ID=""
export INSTANCE_DNS=""

# create
echo ' • launching instance'
aws ec2 run-instances --count 1 --image-id ami-8e8a70e6 --instance-type t1.micro --key-name ramvark-keypair --security-groups allopen > reservation.txt
export INST=(`cat reservation.txt |grep INSTANCES|tr "\\t" "\n"`)
INSTANCE_ID=${INST[7]}
echo \ • instance id: $INSTANCE_ID

# wait for ip
echo ' • waiting for ip';
while [ -z $INSTANCE_DNS ]; do 
	aws ec2 describe-instances --instance-ids $INSTANCE_ID > wait_for_ip.log;
	#cat wait_for_ip.log;
	export INST=(`cat wait_for_ip.log |grep ASSOCIATION|tr "\\t" "\n"`);
	#cat wait_for_ip.log
	INSTANCE_DNS=${INST[2]};
	echo " • public dns name: $INSTANCE_DNS"
	if [ -z $INSTANCE_DNS ]; then sleep 1;fi;
done;

# sendfiles
echo ' • send files'
rsync -v --delete --exclude .svn --exclude u/ --exclude cache/ --progress -avz -e "ssh -o StrictHostKeyChecking=no -i /Users/calin/wallet/ramvark-keypair.pem" "/Users/calin/Documents/workspace/a/" root@$INSTANCE_DNS:/a/

# restart server
echo ' • restart server'
ssh -i $HOME/wallet/ramvark-keypair.pem root@$INSTANCE_DNS "killall -9 java ; /studio/suse-studio-custom"

# try
echo " • try http://$INSTANCE_DNS/"
curl $INSTANCE_DNS
echo

# delete
echo " • deleting $INSTANCE_ID"
aws ec2 terminate-instances --instance-ids $INSTANCE_ID
 