package bancoimobiliario;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	
	// frame
	MainFrame frame;
	
	// image list
	private ImgList l;
	
	// images
	private Image bgimg;
	
	public MainPanel(MainFrame frame)
	{
		super();
		this.frame = frame;
		l = new ImgList();
		loadSprites("sprites");
		bgimg = l.getImg("sprites_tabuleiroRJ");
		addMouseListener(new MyMouseListener());
		setLayout(new FlowLayout());
		setBackground(Color.WHITE);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(bgimg, 0, 0, this.getWidth(), this.getHeight(), 0, 0, bgimg.getWidth(null), bgimg.getHeight(null), null);
	}
	
	private void loadSprites(String spritesDir)
	{
		File dir = new File(spritesDir);
		String [] extensions = {"jpg","png"};
		FilenameFilter spriteFileFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				for( final String ext : extensions )
					if( name.endsWith("."+ext) )
						return true;
				return false;
			}
		};
		for( File f : dir.listFiles(spriteFileFilter) )
		{
			System.out.printf("Image '%s' added.",f.getPath());
			l.addImg(f.getPath());
		}
	}
	
}
