gcc -o pc3.img -Wno-int-to-pointer-cast -nostdlib -Wl,--oformat,binary -Wl,-Ttext,0x7c00 -O1 -Wfatal-errors pc.c main.c && qemu pc3.img
