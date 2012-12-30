package at.dahu4wa.homecontrol.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class RefreshAction extends AbstractAction{
	private static final long serialVersionUID = 1L;

	public RefreshAction(){
		super("Refresh", new ImageIcon("/src/resource/refreshAction.png"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("pressed!!");
	}

}
