package at.dahu4wa.homecontrol.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * @author Stefan Huber
 */
public class HomeControlView extends JFrame {
	private static final long serialVersionUID = 1L;

	public HomeControlView(){
		init();
	}
	
	private void init(){

		this.setSize(550, 500);
		this.setTitle("Home Control");
		this.setJMenuBar(createMenuBar());
		this.setBackground(Color.gray);
		
		Image icon = new ImageIcon( "resource/HomeControl.png" ).getImage();
		this.setIconImage(icon);
		
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(new JLabel(new ImageIcon( "resource/HomeControl.png" )));
		this.setContentPane(panel);
		
		this.setVisible(true);
	}
	
	private JMenuBar createMenuBar(){
		JMenuBar bar = new JMenuBar();
		
		JMenu fileMenu = new JMenu();
		JMenuItem file = new JMenuItem(new RefreshAction());
		
		fileMenu.add(file);
		fileMenu.setText("File");
		
		bar.add(fileMenu);
		return bar;
	}
}
