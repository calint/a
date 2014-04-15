#include"cap.h"
int cap_main(int argc,const char**arg){
    while(argc--)puts(*arg++);

///--- cap compiled

file*f=file_new(ram,sizeof(ram));
file_info(f,out);nl();
file_to(f,out);nl();
file_copy(f,"another",3);
file_to(f,out);nl();


const size s=file_size_in_bytes(f);
pl("file size: %lu",s);


file_to(f,out);nl();

__block char ch='x';
file_foreach_char_write(f,^(char*c){
    putchar(*c);
    *c='x';
    putchar(ch++);
});

file_free(f);

///--- cap compiled done

    return 0;
}

