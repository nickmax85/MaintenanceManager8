package file;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OpenDirWhitespace {

	public static void main(String[] args) {
		new OpenDirWhitespace();

	}

	public OpenDirWhitespace() {

		try {
			String dir = "D:\\temp\\Ordner     2 Leerzeichen";
			File path = new File(dir);

			System.out.println(path.isDirectory());

			Runtime.getRuntime().exec("explorer " + path);

			System.out.println(path.getAbsolutePath());
			System.out.println(dir);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
