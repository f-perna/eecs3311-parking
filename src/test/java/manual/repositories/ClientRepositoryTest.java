package manual.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import models.client.Client;
import models.client.GenerateClientFactory;
import repositories.ClientRepository;

public class ClientRepositoryTest extends BaseRepositoryTest {
	private ClientRepository clientRepository;

	@BeforeEach
	protected void setUp() throws IOException {
		super.setUp();
		initializeRepositories();
	}

	private void initializeRepositories() {
		clientRepository = repositoryFactory.getClientRepository();
	}

	@AfterEach
	protected void tearDown() throws NoSuchFieldException, IllegalAccessException {
		this.clientRepository = null;
		super.tearDown();
	}

	@Test
	public void verifyRegisterClient() {
		Client testClient = GenerateClientFactory.getClientType("Bob", "test@gmail.com", "123", Client.type.VISITOR,
				"TES 123", false);

		try {
			clientRepository.registerClient(testClient);
		} catch (IllegalArgumentException e) {
			fail("Client cannot be null");
		}

		clientRepository.deleteClient("test@gmail.com");
	}

	@Test
	public void verifyRegisterNullClient() {
		Client testClient = null;

		try {
			clientRepository.registerClient(testClient);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Client cannot be null", e.getMessage());
		}
	}

	@Test
	public void verifyDeleteClient() {
		Client testClient = GenerateClientFactory.getClientType("Bob", "test@gmail.com", "123", Client.type.VISITOR,
				"TES 123", false);

		clientRepository.registerClient(testClient);

		try {
			clientRepository.deleteClient("test@gmail.com");
		} catch (Exception e) {
			fail("Email cannot be null");
		}
	}

	@Test
	public void verifyInvalidDeleteClient() {
		try {
			clientRepository.deleteClient(null);
			fail("Expected IllegalArgumentException");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Email cannot be null");
		}
	}

	@Test
	public void verifyGetClientByEmail() {
		Client testClient = GenerateClientFactory.getClientType("Bob", "test@gmail.com", "123", Client.type.VISITOR,
				"TES 123", false);

		clientRepository.registerClient(testClient);

		Client getClient = clientRepository.getClientByEmail("test@gmail.com");

		assertEquals(testClient, getClient);

		clientRepository.deleteClient("test@gmail.com");
	}

	@Test
	public void verifyInvalidGetClientByEmail() {
		Client getClient = clientRepository.getClientByEmail("test@gmail.com");

		assertNull(getClient);
	}

	@Test
	public void verifyGetClientByLicense() {
		Client testClient = GenerateClientFactory.getClientType("Bob", "test@gmail.com", "123", Client.type.VISITOR,
				"TES 123", false);

		clientRepository.registerClient(testClient);

		Client getClient = clientRepository.getClientByLicensePlate("TES 123");

		assertEquals(testClient, getClient);

		clientRepository.deleteClient("test@gmail.com");
	}

	@Test
	public void verifyInvalidGetClientByLicense() {
		Client getClient = clientRepository.getClientByLicensePlate("TES 123");

		assertNull(getClient);
	}

	@Test
	public void verifyAuthClient() {
		Client testClient = GenerateClientFactory.getClientType("Bob", "test@gmail.com", "123", Client.type.VISITOR,
				"TES 123", false);

		clientRepository.registerClient(testClient);

		Client authClient = clientRepository.authenticateClient("test@gmail.com", "123");

		assertEquals(testClient, authClient);

		clientRepository.deleteClient("test@gmail.com");
	}

	@Test
	public void verifyInvalidAuthClient() {
		Client authClient = clientRepository.authenticateClient("test@gmail.com", "123");

		assertNull(authClient);
	}
}
