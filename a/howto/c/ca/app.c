#include"cap.h"
int cap_main(int argc,const char**arg){
    while(argc--)puts(*arg++);

    file*f=file_new(rom,sizeof(rom)-1);
    pl("files %lu",file_count);
    file_info(f,stdout);nl();
    file_to(f,stdout);nl();
    __block char chh='.';
    file_foreach_char_write(f,^(char*ch){
        putchar(*ch);
        *ch='x';
        putchar(chh++);
	});nl();
    file_to(f,stdout);nl();
    file_copy(f,"another",3);
    file_to(f,stdout);nl();
//    vfunc(f,info, stdout);
    file_recycle(f);
    pl("files %lu",file_count);

    return 0;
}
