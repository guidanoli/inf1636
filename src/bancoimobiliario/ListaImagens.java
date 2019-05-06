package bancoimobiliario;
import java.awt.*;
import java.io.*;
import javax.imageio.*;

public class ListaImagens {
	private Image [] v;
	private int tam = 0;
	private int max_tam = 100;
	
	public ListaImagens() {
		v = new Image[max_tam];
	}
	
	Image novaImagem(String path) {
		Image i;
		try {
			i=ImageIO.read(new File(path));
			if( tam < max_tam )
			{
				v[tam]=i;
				tam++;
				return i;
			}
			else
			{
				return null;
			}
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
			return null;
		}
	}
	
	int getTam() { return tam; }
	
	Image []getImagem() { return v; }
}
