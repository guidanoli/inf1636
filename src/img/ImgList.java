package img;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.*;

/**
 * 
 * <p>Loads and stores image objects in a {@link java.util.Map Map} structure, that
 * can be accessed solely by its name (not even the extension is considered).
 * 
 * <p><b>Observation:</b> That way, images with the same name but with different full paths and/or
 * extensions cannot be stored in the same Image List, since their keys would be the same.
 * 
 * @author guidanoli
 *
 */
public class ImgList {
	protected Map<String,Image> imglist;
	
	/**
	 * <h1>ImgList</h1>
	 * <p>{@code public ImgList()}
	 * <p>Constructs an empty Image List
	 */
	public ImgList() {
		imglist = new HashMap<String,Image>();
	}
	
	/**
	 * <p>{@code public Image addImg(String path)}
	 * <p>Tries to load an image object through the given path and
	 * store it on the map with the path as its key.
	 * <p>It should be used as such:
	 * <p>{@code ImgList imgList = new ImageList();}<br>
	 * {@code Image i = imgList.addImg("resources/img.png");}</p>
	 * @param path - Image absolute path
	 * @return Loaded image object
	 */
	public Image addImg(String path) {
		String formattedPath = formatPath(path);
		if( imglist.containsKey(formattedPath) )
		{
			System.out.printf("Image '%s' already loaded!",path);
			return imglist.get(formattedPath);
		}
		try {
			Image i = ImageIO.read(new File(path));
			imglist.put(formattedPath, i);
			return i;
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
			return null;
		}
	}
	
	/**
	 * <p>{@code int getImgCount()}
	 * <p>Get image count in image list
	 * @return Number of images stored
	 */
	public int getImgCount() {
		return imglist.size();
	}
	
	/**
	 * <p>{@code public Image getImg(String path)}
	 * <p>Get image object from its formatted path
	 * <p>In the formatted form of the path, every file separator
	 * is replaced by an underscore character, and the extension
	 * is ignored.
	 * <h1>Example:</h1>
	 * <ul>
	 * <li>{@code resources/img.png} turns to {@code resources_img}</li>
	 * <li>{@code a\b\c\img.png} turns to {@code a_b_c_img}</li>
	 * <li>{@code a_b/c_img.jpg} also turns to {@code a_b_c_img}</li>
	 * </ul>
	 * @param path - Formatted image path
	 * @return image object, if stored. Null otherwise.
	 */
	public Image getImg(String path) {
		Image i = imglist.get(formatPath(path));
		if( i == null )
			System.out.printf("Image '%s' not found!",path);
		return i;
	}
	
	private String formatPath(String path) {
		path = path.replaceAll("\\..*", ""); /* remove extensions */
		path = path.replaceAll("\\\\|/", "_"); /* even file separators */
		return path;
	}
	
}
