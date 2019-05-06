package bancoimobiliario;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	// size parameters
	public final int DEF_H = 400;
	public final int DEF_W = 400;
	
	// containers
	JPanel p = new MainPanel();
	
	// components
	ArrayList<JButton> btn_array = new ArrayList<JButton>();
	String button_lbls[] = {"wow","such","buttons","very","java"};
	ButtonGroup button_group = new ButtonGroup();
	ArrayList<JRadioButton> tgl_array = new ArrayList<JRadioButton>();
	String tgl_lbls[] = {"male","female","neither","both"};
	ArrayList<JCheckBox> cb_array = new ArrayList<JCheckBox>();
	String cb_lbls[] = {"smol","cute","fluf"};
	String lst_items_lbls[] = {"black","cream","white"};
	JList<String> lst = new JList<String>(lst_items_lbls);
	
	public MainFrame(String name) {
		// set frame name
		super(name);
		// set bounds in the middle
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int s_w = d.width;
		int s_h = d.height;
		int x = (s_w - DEF_W)/2;
		int y = (s_h - DEF_H)/2;
		setBounds(x,y,DEF_W,DEF_H);
		// exit on close
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// set panel layout
		p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
		for( String lbl : button_lbls )
		{
			JButton b = new JButton(lbl);
			btn_array.add(b);
			b.setToolTipText(b.getText()+"!");
			p.add(b);
		}
		for( String lbl : tgl_lbls )
		{
			JRadioButton t = new JRadioButton(lbl);
			tgl_array.add(t);
			button_group.add(t);
			t.setToolTipText(t.getText()+" is an option.");
			p.add(t);
		}
		for( String lbl : cb_lbls )
		{
			JCheckBox t = new JCheckBox(lbl,false);
			cb_array.add(t);
			t.setToolTipText(t.getText()+" is a characteristic.");
			p.add(t);
		}
		lst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		p.add(lst);
		JRadioButton rnd_select = tgl_array.get(new Random().nextInt(tgl_array.size()));
		rnd_select.setSelected(true);
		p.setBackground(Color.WHITE);
		getContentPane().add(p);
		// set look and feel
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Class not found.");
		}
		catch (UnsupportedLookAndFeelException e)
		{
			System.out.println("Unsupported look & feel.");
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		MainFrame painel = new MainFrame("Doge");
		painel.setVisible(true);
		//code
	}
	
}
