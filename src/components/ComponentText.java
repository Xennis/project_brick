package components;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;


public class ComponentText extends ComponentMain {

	private static final long serialVersionUID = 6468520683490809903L;

	public ComponentText(String text, Color color){
		super();
		setLayout(new BorderLayout());
		JLabel label = new JLabel(text);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(new Font("Comic Sans MS",Font.BOLD,34));
		label.setForeground(color);
		add(label);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0,0,g.getClipBounds().width,g.getClipBounds().height);
	}
	
}
