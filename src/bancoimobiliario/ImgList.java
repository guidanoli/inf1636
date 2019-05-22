package bancoimobiliario;
import java.awt.*;
import java.io.*;
import javax.imageio.*;

public class ImgList {
	private Image [] v;
	private int count = 0;
	private int max;
	
	public ImgList(int maxSize) throws Exception {
		if( maxSize <= 0 )
		{
			throw new Exception("Lista de imagens deve ter tamanho positivo.");
		}
		max = maxSize;
		v = new Image[max];
	}
	
	Image addImg(String path) {
		Image i;
		try {
			i=ImageIO.read(new File(path));
			if( count < max )
			{
				v[count]=i;
				count++;
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
	
	int getImgCount() { return count; }
	Image [] getImg() { return v; }
}
