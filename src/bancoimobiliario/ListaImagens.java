package bancoimobiliario;
import java.awt.*;
import java.io.*;
import javax.imageio.*;

public class ListaImagens {
	private Image []vet=new Image[100];
	private Image i;
	private int tam;
	private int max_tam = 100;
	public ListaImagens() {
		try {
			i=ImageIO.read(new File("sprites/img.png"));
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	void novaImagem() {
		if( tam < max_tam )
		{
			vet[tam]=i;
			tam++;
		}
	}
	int getTam() { return tam; }
	Image []getImagem() { return vet; }
}
