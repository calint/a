echo \ • $*
ssh -i $SUSE_STUDIO_EC2_KEYS/suse-studio-b2018027-us-east-1-key.pem root@ec2-54-85-223-192.compute-1.amazonaws.com "$*" > ec2-54-85-223-192.log &
ssh -i $SUSE_STUDIO_EC2_KEYS/suse-studio-b2018049-us-east-1-key.pem root@ec2-54-88-187-221.compute-1.amazonaws.com "$*" > ec2-54-88-187-221.log &
wait
