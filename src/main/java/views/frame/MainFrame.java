package views.frame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import controllers.NavigationController;
import views.BookingPage;
import views.CheckoutPage;
import views.ClientPage;
import views.HomePage;
import views.LoginPage;
import views.ManagerDashboard;
import views.Navbar;
import views.ParkingManagementPage;
import views.RegisterPage;
import views.SuperManagerPage;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static CardLayout cardLayout;
	private static JPanel cardPanel;

	public MainFrame() {

		setTitle("YorkU Parking Booking System");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Initialize CardLayout and card panel for pages
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);

		// Initialize the Navigation Controller with the card layout and panel
		NavigationController.init(cardLayout, cardPanel);

		JPanel navbar = new Navbar(this);

		// Add application pages
		NavigationController.addPage("Home", new HomePage(this));
		NavigationController.addPage("Login", new LoginPage(this));
		NavigationController.addPage("Register", new RegisterPage(this));
		NavigationController.addPage("Client", new ClientPage(this));
		NavigationController.addPage("Booking", new BookingPage(this));
		NavigationController.addPage("Checkout", new CheckoutPage(this));

		NavigationController.addPage("ManagerDashboard", new ManagerDashboard(this));
		NavigationController.addPage("ParkingManagement", new ParkingManagementPage(this));
		NavigationController.addPage("SuperManager", new SuperManagerPage(this));

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(cardPanel, BorderLayout.CENTER);
		getContentPane().add(navbar, BorderLayout.NORTH);
		setVisible(true);
	}

}
