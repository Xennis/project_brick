package components;
import java.awt.Graphics;
import javax.swing.JComponent;


public abstract class ComponentMain extends JComponent{

	private static final long serialVersionUID = 7279791293579917884L;

	private int[] fieldDimension = {500, 600};
	
	public ComponentMain(){
		setSize(fieldDimension[0], fieldDimension[1]);
		setPreferredSize(getSize());
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
	
	public int[] getFieldDimension() {
		return fieldDimension;
	}
}
