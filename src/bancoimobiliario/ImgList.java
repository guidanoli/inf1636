package bancoimobiliario;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.*;

public class ImgList {
	protected Map<String,Image> imglist;
	
	/* Initialize a list without cap */
	public ImgList() {
		imglist = new HashMap<String,Image>();
	}
	
	/* Add image to image list through its path */
	public Image addImg(String path) {
		/* Does not let overwrite unnecessarly */
		if( imglist.containsKey(path) )
		{
			System.out.printf("Image '%s' already loaded!",path);
			return imglist.get(path);
		}
		try {
			Image i = ImageIO.read(new File(path));
			imglist.put(path, i);
			return i;
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
			return null;
		}
	}
	
	/* Get image count */
	public int getImgCount() {
		return imglist.size();
	}
	
	/* Get image object from its path */
	public Image getImg(String path) {
		Image i = imglist.get(path);
		if( i == null )
			System.out.printf("Image '%s' not found!",path);
		return i;
	}
}
