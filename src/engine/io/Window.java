package engine.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3631055754530655559L;
	
	private JPanel panel;
	
	public JButton testSphereSearch;
	public JButton testConeSearch;
	public JButton testCubeSearch;
	
	public Window() {
		super("D&D Engine");
		setIconImage(new ImageIcon(System.getProperty("user.dir").replace('\\', '/') + "/src/resources/icon.png").getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000,600);
		setLayout(null);
		
		panel = new JPanel(null);
		panel.setBounds(0, 0, 500, 500);
		
		testSphereSearch = new JButton("Test Sphere Search");
		testSphereSearch.setBounds(20, 20, 100, 100);
		testSphereSearch.addActionListener(this);
		
		testConeSearch = new JButton("Test Cone Search");
		testConeSearch.setBounds(140, 20, 100, 100);
		testConeSearch.addActionListener(this);
		
		testCubeSearch = new JButton("Test Cube Search");
		testCubeSearch.setBounds(260, 20, 100, 100);
		testCubeSearch.addActionListener(this);
		
		panel.add(testSphereSearch);
		panel.add(testConeSearch);
		panel.add(testCubeSearch);
		
		add(panel);
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		if (e.getSource() == testSphereSearch) {
//			Manager.entitiesInSphere(new Vector(0, 0, 0), 5.0);
//		}
//		else if (e.getSource() == testConeSearch) {
//			Manager.entitiesInCone(new Vector(0, 0, 0), new Vector(5, 0, 0));
//		}
//		else if (e.getSource() == testCubeSearch) {
//			Manager.entitiesInCube(new Vector(0, 0, 0), new Vector(0, 0, 5));
//		}
		
	}
}
