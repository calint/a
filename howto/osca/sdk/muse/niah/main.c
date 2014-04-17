#include<stdio.h>
#include<string.h>
typedef short bits;
static bits acc;
static bits r[16];
static bits ipf;
static bits ipfstk[16];
static bits ipfstki;
static bits ram[64*1024];
static bits rom[4*1024];
void tk(){
	bits ip=ipf&0x0fff;
	bits zn=ipf&0xf000;
	bits ir=rom[ip];
	printf("%03x %04x   %x\n",ip,ir,ipfstki);
	ip++;
	ipf=ip|zn;
	if(ir&(zn>>12))
		return;
	const bits im=(ir&0xfff0)>>4;
	switch(ir&0b1100){
	case 0b1100:
		printf("    lda %03x\n",im);
		r[0xa]=im;
		return;
	case 0b0100:
		printf("    cal %03x\n",im);
		ipfstk[ipfstki++]=ipf;
		ipf=zn|im;
		return;
	case 0b1000:
		ipf=ipfstk[--ipfstki];
		printf("    ret\n");
	}
	const bits lecs=ir&0x00f0;
	if(lecs&0x0040){
		printf("    ..c.\n");
		acc=0;
	}
	const bits rega=(ir&0xf000)>>12;
	const bits addr=r[rega];
	printf("addr: %x\n",addr);
	bits data=ram[addr];
	printf("read ram[%x] gives %x\n",addr,data);
	const bits niah=ir&0x0f00;
	if(niah&0x0100)//n
		data=-data;
	if(niah&0x0200)//i
		r[rega]++;
	if(niah&0x0400)//a
		acc+=data;
	if(niah&0x0800)//h
		acc>>=1;
	if(lecs&0x0080){
		printf("[%x]=%x\n",addr,acc);
		ram[addr]=acc;
	}
}
int main(){
	rom[0]=0x100c;
	rom[1]=0x1004;
	rom[2]=0x001c;
	rom[3]=0x100c;
	rom[0x100]=0xa640;
	rom[0x101]=0xa688;
	ram[0x100]=0x001;
	ram[0x101]=0x002;
	tk();
	tk();
	tk();
	tk();
	tk();
}
