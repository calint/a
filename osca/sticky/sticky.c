// requires https://download.gnome.org/sources/gtksourceview/3.12/gtksourceview-3.12.3.tar.xz
#define APP "sticky"
#include<gtk/gtk.h>
#include<gtksourceview/gtksourceview.h>
#define WIDTH 200
#define HEIGHT 200
void on_window_destroy(/*GtkWidget*widget,gpointer data*/){
	gtk_main_quit();
}
int main(int argc,char**argv){
	gtk_init(&argc,&argv);
	GdkDisplay*display=gdk_display_get_default();
	GdkScreen*screen=gdk_display_get_default_screen(display);
	GtkWidget*window=gtk_window_new(GTK_WINDOW_TOPLEVEL);
	g_signal_connect(G_OBJECT(window),"destroy",G_CALLBACK(on_window_destroy),NULL);

	GtkCssProvider*provider=gtk_css_provider_new();
	gtk_style_context_add_provider_for_screen(screen,GTK_STYLE_PROVIDER(provider),GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);
//	GError*err;
//	gtk_css_provider_load_from_path(GTK_CSS_PROVIDER(provider),"~/.gconf/sticky/sticky.css",&err);
//	printf(" loading from path: %d  %s\n",err->code,err->message);
	gtk_css_provider_load_from_data(GTK_CSS_PROVIDER(provider),
		"GtkWindow{\n"
		"	background-color:yellow;\n"
		"	font-family:monospace;\n"
		"	font-size:1em;\n"
		"}\n"
		"GtkTextView{\n"
		"	background-color:inherit;\n"
		"}\n"
		,-1,NULL);
	g_object_unref(provider);
	gtk_window_set_title(GTK_WINDOW(window),APP);
	gtk_window_set_default_size(GTK_WINDOW(window),WIDTH,HEIGHT);//? magicnum
//? set position at pointer
	GtkWidget*text_view=gtk_source_view_new();
	gtk_container_add(GTK_CONTAINER(window),text_view);
	gtk_widget_show_all(window);
	gtk_main();
	return 0;
}









/*
void on_button_clicked(GtkWidget*button,GtkTextBuffer*buffer){
	GtkTextIter start,end;
	gtk_text_buffer_get_start_iter (buffer, &start);
	gtk_text_buffer_get_end_iter (buffer, &end);
	gchar*text=gtk_text_buffer_get_text(buffer,&start,&end,FALSE);
	g_print("%s",text);
	g_free(text);
	gtk_main_quit();
}
*/

/*
	
//	GtkTextBuffer*buffer=gtk_text_view_get_buffer(GTK_TEXT_VIEW(text_view));
//	gtk_text_buffer_set_text(buffer,"Hello Text View!",-1);

	GdkColor red={0,0xffff,0x0000,0x0000};
	gtk_widget_modify_bg(text_view,GTK_STATE_ACTIVE,&red);
*/


