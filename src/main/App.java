package main;

import javax.swing.SwingUtilities;
import views.MainFrame;

public class App {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new MainFrame();
		});
	}
}
