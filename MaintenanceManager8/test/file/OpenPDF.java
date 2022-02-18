package file;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class OpenPDF {

	public static void main(String[] args) {

		new OpenPDF();

	}

	public OpenPDF() {
		
		openPdf("");
		
		
	}
	
	public void openPdf(String pdf){
        if (Desktop.isDesktopSupported())   
        {   
            InputStream jarPdf = getClass().getClassLoader().getResourceAsStream("Stueckzahlimport.pdf");

            try {
                File pdfTemp = new File("temp.pdf");
              
                FileOutputStream fos = new FileOutputStream(pdfTemp);
                while (jarPdf.available() > 0) {
                      fos.write(jarPdf.read());
                }  
                fos.close();
               
                Desktop.getDesktop().open(pdfTemp);
            }   

            catch (IOException e) {
                System.out.println("erreur : " + e);
            }  
        }
    }

}
