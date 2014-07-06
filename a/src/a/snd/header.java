package a.snd;

import java.io.IOException;
import java.io.OutputStream;

public interface header{
	void set_data_size_in_bytes(int n);
	void set_samples_per_second(int n);
	void set_number_of_channels(int n);
	void to(OutputStream os)throws IOException;
}