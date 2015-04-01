clang++ -O3 -o xiinux.bin src/xiinux.cpp -lpthread &&
mv -f xiinux.bin xiinux&&
ls -la xiinux &&
./xiinux
