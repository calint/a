package a.ramvark;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
public @Retention(RetentionPolicy.RUNTIME)@interface in{
	int type()default 0;
	//type 1:text field
	//     2:textarea
	//     3:contains list
	//     6:input list orderable
	Class<? extends lst>lst()default lst.class;
	Class<? extends itm>itm()default itm.class;
	boolean must()default false;
}
