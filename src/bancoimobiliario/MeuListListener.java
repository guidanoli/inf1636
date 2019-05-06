package bancoimobiliario;
import javax.swing.*;
import javax.swing.event.*;

public class MeuListListener implements ListSelectionListener {
	public void valueChanged(ListSelectionEvent e)
	{
		if(e.getValueIsAdjusting())
			return;
		JList lst = (JList) e.getSource();
		if( lst.isSelectionEmpty() )
		{
			
		}
		else
		{
			int index = lst.getSelectedIndex();
			String val = (String) lst.getSelectedValue();
			System.out.println(val);
		}
	}
}
