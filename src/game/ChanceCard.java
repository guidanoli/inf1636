package game;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import io.ImgList;

/**
 * <p>A card that can have either a fortune or a misfortune.
 * In most cases, the player will be either handed or subtracted
 * a certain financial amount. Except for those, there are also:
 * <ul>
 * <li>Prison escape pass cards - guarded by the player and used
 * whenever it goes to jail to be freed.</li>
 * <li>Prison visiting cards - sends the player to the jail for
 * a 'visit'.</li>
 * </ul>
 * @author guidanoli
 *
 */
public class ChanceCard {

	private Image cardImg;
	private ActionListener ae;
	
	public ChanceCard(String imgPath, ActionListener listener) {
		ImgList imgList = ImgList.getInstance();
		cardImg = imgList.addImg(imgPath);
		ae = listener;
	}
	
	public void triggerCard(Player player) {
		ae.actionPerformed(new ActionEvent(player,0,"Player gets a card!"));
	}
	
	/**
	 * @return card image object
	 */
	public Image getCardImage() { return cardImg; }
	
}
