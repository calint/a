void osca_keyb_ev();//called from keyboard interrupt when new keycode from keyboard
volatile const int osca_key;//last received keycode (char)
volatile const int osca_t;//time lower 32b
volatile const int osca_t1;//time higher 32b
inline void osca_pass(){asm("hlt");}
