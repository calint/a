#!/bin/sh
echo `date`&&
echo&&
cat /proc/version&&
echo&&
lsb_release -a&&
echo&&
lscpu&&
echo&&
free&&
echo&&
echo /etc/sysctl.conf&&
cat /etc/sysctl.conf&&
echo&&
echo /etc/security/limits.conf&&
cat /etc/security/limits.conf&&
echo
echo
echo sysctl -a
sysctl -a