package bancoimobiliario;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class PainelPrincipal extends JFrame {

	public final int DEF_H = 600;
	public final int DEF_W = 800;
	
	public PainelPrincipal() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int s_w = d.width;
		int s_h = d.height;
		int x = (s_w - DEF_W)/2;
		int y = (s_h - DEF_H)/2;
		setBounds(x,y,DEF_W,DEF_H);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		PainelPrincipal painel = new PainelPrincipal();
		painel.setTitle("Banco Imobiliário");
		painel.setVisible(true);
		//code
	}
	
}
