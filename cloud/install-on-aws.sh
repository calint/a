. ./env.sh
echo `date` allow root login
ssh -oStrictHostKeyChecking=no -i$KEY_ ubuntu@$HOST_ 'sh -s'<allow-root-login-on-aws.sh
. ./install.sh
