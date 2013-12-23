echo sokio qa&&
echo `date`&&
echo  episode 2&&
cat ep2.soi|nc localhost 8888|cat>file&&
diff file ep2.soi.qa&&
rm file&&
echo `date`&&
echo done
