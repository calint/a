for p in b a c d2 d3 auplo;do
  cp -avr ../$p/* . 
done
zip deploy.zip -r .
ls -lah deploy.zip
bees app:deploy -a bb -v -d false deploy.zip 
rm deploy.zip

