package services;

import models.ParkingSystemException;
import models.ParkingSystemException.ErrorType;
import models.client.Client;
import models.client.GenerateClientFactory;
import repositories.ClientRepository;

import java.util.List;
import java.util.regex.Pattern;

public class ClientService {
	private final ClientRepository clientRepository;

	private static final Pattern PASSWORD_PATTERN = Pattern
			.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_#])[A-Za-z\\d@$!%*?&_#]{8,}$");

	public ClientService(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	public boolean registerClient(String name, String email, String password, Client.type clientType,
			String licencePlate) {
		if (name == null || name.trim().isEmpty()) {
			throw new ParkingSystemException("Name cannot be empty", ErrorType.VALIDATION);
		}
		if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			throw new ParkingSystemException("Invalid email format", ErrorType.VALIDATION);
		}
		if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
			throw new ParkingSystemException(
					"Password must include uppercase, lowercase, number and symbol (min 8 chars)",
					ErrorType.VALIDATION);
		}
		if (licencePlate == null || !licencePlate.matches("^[A-Z0-9\\s]{2,8}$")) {
			throw new ParkingSystemException("Licence plate must be 2-8 uppercase letters, numbers, or spaces",
					ErrorType.VALIDATION);
		}

		if (clientRepository.getClientByEmail(email) != null) {
			throw new ParkingSystemException("Email address is already registered in the system",
					ErrorType.BUSINESS_LOGIC);
		}

		if (clientRepository.getClientByLicensePlate(licencePlate) != null) {
			throw new ParkingSystemException("License Plate is already being used by a user in the system",
					ErrorType.BUSINESS_LOGIC);
		}

		boolean approved = (clientType == Client.type.VISITOR);

		Client newClient = GenerateClientFactory.getClientType(name.trim(), email.toLowerCase(), password, clientType,
				licencePlate.toUpperCase(), approved);

		clientRepository.registerClient(newClient);
		return true;
	}

	public Client login(String email, String password) {
		if (email == null || password == null) {
			return null;
		}

		email = email.toLowerCase().trim();

		Client client = clientRepository.authenticateClient(email, password);
		if (client == null) {
			throw new ParkingSystemException("Invalid email address or password", ErrorType.AUTHENTICATION);
		}

		if (!client.isApproved() && client.getType() != Client.type.VISITOR) {
			throw new ParkingSystemException(
					"Your account is pending approval by a manager. Please wait for approval to access the system.",
					ErrorType.AUTHORIZATION);
		}

		return client;
	}

	public List<Client> getAllClients() {
		return clientRepository.getAllClients();
	}

	public boolean approveClient(String email, boolean approved) {
		if (email == null) {
			throw new ParkingSystemException("Email address must be provided", ErrorType.VALIDATION);
		}

		Client client = clientRepository.getClientByEmail(email);
		if (client == null) {
			throw new ParkingSystemException("Client account not found", ErrorType.BUSINESS_LOGIC);
		}

		if (client.getType() == Client.type.VISITOR) {
			throw new ParkingSystemException("Visitor accounts cannot be approved", ErrorType.BUSINESS_LOGIC);
		}

		client.setApproved(approved);
		clientRepository.saveClients();
		return true;
	}

	public void removeClient(String email) {
		if (email == null) {
			throw new ParkingSystemException("email cannot be null", ErrorType.VALIDATION);
		}
		clientRepository.deleteClient(email);
	}

	public Client getClientByEmail(String email) {
		if (email == null) {
			throw new ParkingSystemException("Email cannot be null", ErrorType.VALIDATION);
		}
		return clientRepository.getClientByEmail(email);
	}
}