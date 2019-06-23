package gui;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import game.Logic;
import io.ImgList;
import io.LocalResources;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements MouseListener {
	
	/* *********
	 * VARIABLES
	 * ********* */
		
	// parent frame
	MainFrame frame;
		
	// logic
	Logic logic = Logic.getInstance();
	
	// swing components
	private JButton rollBtn = new JButton("Rolar dados");
	private JButton buyBtn = new JButton("Comprar");
	private JButton upgradeBtn = new JButton("Construir");
	private JButton endTurnBtn = new JButton("Terminar turno");
	private JButton propertyBtn = new JButton("Meu patrimônio");
	private JButton saveBtn = new JButton("Salvar");
	private JButton loadBtn = new JButton("Carregar");
	private JButton endBtn = new JButton("Terminar");

	JButton [] upperBtnGrid = {
		saveBtn,
		loadBtn,
		endBtn,
	};
	
	JButton [] lowerBtnGrid = {
			rollBtn ,
			propertyBtn ,
			upgradeBtn ,
			buyBtn ,
			endTurnBtn,
	};
	
	// graphical components
	private ImgList imgList = ImgList.getInstance();
	private Image bgimg;
	private Image dice1, dice2;
	private ArrayList<Image> playersimg = new ArrayList<Image>();

	// panel mouse listener and image bounds
	private PanelMouseListener listener = new PanelMouseListener();
	private int dice_lower_x = 150;
	private int dice_side = 70;
	private int dice_margin = 10;
	private Rectangle dice1_ret = new Rectangle(dice_lower_x, 780, dice_side, dice_side);
	private Rectangle dice2_ret = new Rectangle(dice_lower_x + dice_side + dice_margin, 780, dice_side, dice_side);
	private Rectangle bgimg_ret = new Rectangle(1000,1000);
	
	// board measures
	private final int longMeasure = 121;
	private final int shortMeasure = 94;
	
	public MainPanel( MainFrame frame, int numOfPlayers )
	{
		super();
		this.frame = frame;
		this.logic.setNumOfPlayers(numOfPlayers);
		addMouseListener(listener);
		setBackground(Color.WHITE);
		loadSprites(LocalResources.spritesFolder);
		storeSprites();
		setAreaListeners();
		addComponents();
		repaint();
	}
	
	/* **********
	 * COMPONENTS
	 * ********** */
	
	private void addComponents()
	{
		int w = 150;
		int h = 30;
		int margin = 10;
		int lower_y = 830;
		int upper_y = 140;
		
		// lower buttons
		for(int i = 0 ; i < lowerBtnGrid.length; i++) {
			JButton btn = lowerBtnGrid[i];
			btn.setBounds(bgimg_ret.width/2 - w/2, lower_y-(h+margin)*i, w, h);
			btn.addMouseListener(this);
			add(btn);
		}
		
		// upper buttons
		for(int i = 0 ; i < upperBtnGrid.length; i++) {
			JButton btn = upperBtnGrid[i];
			btn.setBounds(bgimg_ret.width/2 - w/2, upper_y+(h+margin)*i, w, h);
			btn.addMouseListener(this);
			add(btn);
		}
		
		updateButtons();
	}
	
	/* **************
	 * AREA LISTENERS
	 * ************** */
	
	private void setAreaListeners()
	{
		for(int pos = 0; pos < logic.numOfCells; pos++)
		{
			Rectangle cellBounds = getCellFormatAndOffset(pos);
			final int finalPos = pos;
			listener.addArea(cellBounds, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					logic.clickedOnCell(finalPos);
				}
			});
		}
	}
	
	/* ****
	 * DICE
	 * **** */
	
	private void UpdateDice()
	{
		int [] rollResult = logic.dice.getLastRolls();
		dice1 = imgList.getImg(String.format("die_face_%d",rollResult[0]));
		dice2 = imgList.getImg(String.format("die_face_%d",rollResult[1]));
	}
	
	/* ******************
	 * PAINTING FUNCTIONS
	 * ****************** */
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		paintBoard(g);
		paintDice(g);
		paintPlayers(g);
	}
	
	private void paintGameImage(Graphics g, Image i, Rectangle r)
	{
		g.drawImage(i, r.x, r.y, r.width+r.x, r.height+r.y,
					0, 0, i.getWidth(this), i.getHeight(this), this);
	}
	
	private void paintDice(Graphics g)
	{
		int margin = 10;
		g.setColor(logic.getCurrentPlayerColor());
		g.fillRect(
			(int) (dice1_ret.getMinX() - margin),
			(int) (dice1_ret.getMinY() - margin),
			(int) (dice2_ret.getMaxX() - dice1_ret.getMinX() + 2*margin),
			(int) (dice2_ret.getMaxY() - dice1_ret.getMinY() + 2*margin)
		); // player turn indicator
		paintGameImage(g,dice1,dice1_ret);
		paintGameImage(g,dice2,dice2_ret);
	}
	
	private void paintPlayers(Graphics g) 
	{
		int pId = 0;
		for( Integer pos : logic.getPlayersPos()) {
			Rectangle rect = getPlayerRect(pos, pId);
			Image i = playersimg.get(pId);
			paintGameImage(g, i , rect);
			pId++;
		}
	}
	
	private void paintBoard(Graphics g)
	{
		paintGameImage(g,bgimg,bgimg_ret);
	}
	
	/* ***************
	 * SPRITES LOADING
	 * *************** */
	
	private void storeSprites()
	{
		bgimg = imgList.getImg("tabuleiroRJ");
		UpdateDice();
		for( int pId = 0; pId < this.logic.getNumPlayers(); pId++ )
			playersimg.add(imgList.getImg(String.format("pin%d", pId)));
	}
	
	private void loadSprites(String spritesDir)
	{
		File dir = new File(spritesDir);
		try (Stream<Path> paths = Files.walk(Paths.get(dir.getPath()))) {
			paths
				.filter(f -> Files.isRegularFile(f) && accept(f.toString()))
				.forEach(this::addImgThroughPath);
		} catch (IOException e) { System.out.println(e.getMessage()); }
	}
	
	private void addImgThroughPath(Path path) {
		imgList.addImg(path.toString());
		System.out.printf("Image '%s' added.\n",path.toString());
	}
	
	private boolean accept(String name) {
		String [] extensions = {"jpg","png"};
		for( final String ext : extensions )
			if( name.endsWith("."+ext) )
				return true;
		return false;
	}
	
	/* ****************************
	 * BOARD HOUSE POSITION MAPPING
	 * **************************** */
	
	private Rectangle getPlayerRect(int pos, int pId) {
		Rectangle cellOffset = getCellOffset(pos);
		Rectangle playerOffset = getPlayerOffset(pos,pId);
		Rectangle rect = new Rectangle(20,30);
		rect.setLocation(cellOffset.x + playerOffset.x - rect.width/2 , cellOffset.y + playerOffset.y - rect.height);
		return rect;
	}
	
	private Rectangle getCellFormatAndOffset(int pos) {
		Rectangle offset = getCellOffset(pos);
		Rectangle format = getCellFormat(pos);
		return new Rectangle(
			offset.x ,
			offset.y ,
			format.width ,
			format.height
		);
	}
	
	private Rectangle getCellOffset(int pos) {
		int x = 7, y = 7;
		int [] const_coord = {7,7,871,871};
		double adjustedShortMeasure = shortMeasure + 2.5; /* accounts for borders */
		int side = ( pos % logic.numOfCells ) / 9;
		int posInLine = pos % 9;
		switch(side)
		{
		case 0:
			x = const_coord[side];
			if( posInLine != 0 ) y += longMeasure + (8-posInLine)*adjustedShortMeasure;
			else y = const_coord[(side+3)%4];
			break;
		case 1:
			y = const_coord[side];
			if( posInLine != 0 ) x += longMeasure + (posInLine-1)*adjustedShortMeasure;
			else x = const_coord[(side+3)%4];
			break;
		case 2:
			x = const_coord[side];
			if( posInLine != 0 ) y += longMeasure + (posInLine-1)*adjustedShortMeasure;
			else y = const_coord[(side+3)%4];
			break;
		case 3:
			y = const_coord[side];
			if( posInLine != 0 ) x += longMeasure + (8-posInLine)*adjustedShortMeasure;
			else x = const_coord[(side+3)%4];
			break;
		}
		return new Rectangle(x,y,0,0);
	}
	
	private Rectangle getPlayerOffset(int pos, int pId) {
		Rectangle cellRect = getCellFormat(pos);
		int row = pId % 3 + 1;
		int col = pId / 3 + 1;
		int row_size = cellRect.width / 4;
		int col_size = cellRect.height / 3;
		int width = (row)*row_size;
		int height = (col)*col_size;
		return new Rectangle(width,height,0,0);
	}
	
	private Rectangle getCellFormat(int pos) {
		if( pos % 9 == 0 )
			return new Rectangle(longMeasure,longMeasure); /* corner */
		int side = pos / 9;
		if( side % 2 == 0 )
			return new Rectangle(longMeasure,shortMeasure); /* left & right */
		else
			return new Rectangle(shortMeasure,longMeasure); /* upper & lower */
	}

	/* ***************
	 * MOUSE LISTENERS
	 * *************** */
	
	public void updateButtons() {
		rollBtn.setEnabled(logic.canRoll());
		buyBtn.setEnabled(logic.canBuy());
		endTurnBtn.setEnabled(logic.canEndTurn());
		upgradeBtn.setEnabled(logic.canUpgrade());
	}
	
	public void mouseClicked(MouseEvent e) {
		if( e.getSource() instanceof JButton )
		{
			/* if source is an inactive button, ignore */
			JButton btnSource = (JButton) e.getSource();
			if( !btnSource.isEnabled() ) return;
			
			if( btnSource == rollBtn )
			{
				logic.roll();
				UpdateDice();
				repaint();
			}
			else if( btnSource == endTurnBtn )
			{
				logic.endTurn();
				repaint();
			}
			else if( btnSource == propertyBtn )
			{
				JDialog propertyDlg = new PropertyDialog(frame);
				propertyDlg.setVisible(true);
			}
			else if( btnSource == buyBtn )
			{
				logic.buy();
			}
			else if( btnSource == upgradeBtn )
			{
				logic.upgrade();
			}
			else if( btnSource == saveBtn )
			{
				File saveFile = openStateFileDialog(
						"Salvar estado do jogo",
						"Salvar",
						"Salvar estado do jogo em arquivo" 
				);
				if( saveFile != null ) logic.saveStateToFile(saveFile);
			}
			else if( btnSource == loadBtn )
			{
				File saveFile = openStateFileDialog(
						"Carregar estado de jogo",
						"Carregar",
						"Carregar estado de jogo de arquivo"
				);
				if( saveFile != null ) {
					logic.loadStateFromFile(saveFile);
					repaint();
				}
			}
			else if( btnSource == endBtn )
			{
				logic.endGame();
			}
			
			updateButtons();
		}
	}
	
	private File openStateFileDialog(String title, String approveLabel, String approveTip) {
		JFileChooser chooser = new JFileChooser();
		String ext = "gamestate";
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Estado de Jogo (*."+ext+")", ext);
	    chooser.setFileFilter(filter);
	    chooser.setDialogTitle(title);
	    chooser.setApproveButtonText(approveLabel);
	    chooser.setApproveButtonToolTipText(approveTip);
		chooser.showOpenDialog(this);
		File f = chooser.getSelectedFile();
		if( f != null )
		{
			String filename = f.toString();
			if (!filename .endsWith("." + ext))
			{
		    	filename += "." + ext;
		    	f = new File(filename);
			}
		}
		return f;
	}
	
	// Unimplemented methods
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	
	/* END */
	
}
