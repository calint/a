#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include"cap.h"
int main(const int argc,const char**argv){
    pl("sizeof(%s)=%lu B","file",file_sizeof);
    pl("sizeof(%s)=%lu B","type",type_sizeof);
    pl("sizeof(%s)=%lu B","ram",sizeof(ram));
    cap_main(argc,argv);
    return 0;
}
