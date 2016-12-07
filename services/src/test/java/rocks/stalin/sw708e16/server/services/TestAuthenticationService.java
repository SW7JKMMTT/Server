package rocks.stalin.sw708e16.server.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.server.persistence.AuthDao;
import rocks.stalin.sw708e16.server.persistence.UserDao;
import rocks.stalin.sw708e16.server.persistence.given.GivenAuthToken;
import rocks.stalin.sw708e16.server.persistence.given.GivenUser;
import rocks.stalin.sw708e16.test.DatabaseTest;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
// Load the beans to configure, here the DAOs
@ContextConfiguration(locations = {"classpath:test-config.xml"})
// apply the transaction manager to the test class so every DAO methods are executed
// within a transaction
@Transactional
public class TestAuthenticationService extends DatabaseTest {
    @Autowired
    UserDao userDao;

    @Autowired
    AuthDao authDao;

    @Autowired
    AuthenticationService authService;

    @Test
    public void testAuthenticate_Correct() throws Exception {
        //Arrange
        User jeff = new GivenUser()
            .withName("Jeff", "Jeffsen")
            .withUsername("Jeff")
            .withPassword("password")
            .in(userDao);
        AuthenticationService.UserCredentials creds =
            new AuthenticationService.UserCredentials("Jeff", "password");

        // Act
        AuthToken token = authService.authenticate(creds);

        // Assert
        Assert.assertEquals(token.getUser(), jeff);
        Assert.assertNotNull(token.getId());
    }

    @Test(expected = NotAuthorizedException.class)
    public void testAuthenticate_WrongUsername() throws Exception {
        //Arrange
        new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("password").in(userDao);
        AuthenticationService.UserCredentials creds =
            new AuthenticationService.UserCredentials("john", "password");

        // Act
        authService.authenticate(creds);
    }

    @Test(expected = NotAuthorizedException.class)
    public void testAuthenticate_WrongPassword() throws Exception {
        //Arrange
        new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("password").in(userDao);
        AuthenticationService.UserCredentials creds =
            new AuthenticationService.UserCredentials("Jeff", "passwrong");

        // Act
        authService.authenticate(creds);
    }

    @Test(expected = BadRequestException.class)
    public void testAuthenticate_MissingUsername() throws Exception {
        //Arrange
        new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("password").in(userDao);
        AuthenticationService.UserCredentials creds =
            new AuthenticationService.UserCredentials(null, "passwrong");

        // Act
        authService.authenticate(creds);
    }

    @Test(expected = BadRequestException.class)
    public void testAuthenticate_MissingPassword() throws Exception {
        //Arrange
        new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("password").in(userDao);
        AuthenticationService.UserCredentials creds =
            new AuthenticationService.UserCredentials("Jeff", null);

        // Act
        authService.authenticate(creds);
    }

    @Test(expected = NotAuthorizedException.class)
    public void testAuthenticate_UsernameCaseSensitive() throws Exception {
        //Arrange
        new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("password").in(userDao);
        AuthenticationService.UserCredentials creds =
            new AuthenticationService.UserCredentials("jeff", "password");

        // Act
        authService.authenticate(creds);
    }

    @Test(expected = NotAuthorizedException.class)
    public void testAuthenticate_PasswordsCaseSensitive() throws Exception {
        //Arrange
        new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("password").in(userDao);
        AuthenticationService.UserCredentials creds =
            new AuthenticationService.UserCredentials("Jeff", "Password");

        // Act
        authService.authenticate(creds);
    }

    @Test
    public void testListTokens_ReturnsMine() throws Exception {
        //Arrange
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("password").in(userDao);
        AuthToken jeffToken1 = new GivenAuthToken().withToken("bomb").forUser(jeff).in(authDao);
        AuthToken jeffToken2 = new GivenAuthToken().withToken("bomb2").forUser(jeff).in(authDao);

        // Act
        Collection<AuthToken> authTokens = authService.listTokens(jeff);

        // Assert
        Assert.assertTrue(authTokens.size() == 2);
        Assert.assertTrue(authTokens.contains(jeffToken1));
        Assert.assertTrue(authTokens.contains(jeffToken2));
    }

    @Test
    public void testListTokens_ReturnsOnlyMine() throws Exception {
        //Arrange
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("password").in(userDao);
        AuthToken jeffToken1 = new GivenAuthToken().withToken("bomb").forUser(jeff).in(authDao);
        AuthToken jeffToken2 = new GivenAuthToken().withToken("bomb2").forUser(jeff).in(authDao);
        User carl = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Carl").withPassword("password").in(userDao);
        AuthToken carlToken1 = new GivenAuthToken().withToken("bomb3").forUser(carl).in(authDao);

        // Act
        Collection<AuthToken> authTokens = authService.listTokens(jeff);

        // Assert
        Assert.assertTrue(authTokens.size() == 2);
        Assert.assertTrue(authTokens.contains(jeffToken1));
        Assert.assertTrue(authTokens.contains(jeffToken2));

        Assert.assertFalse(authTokens.contains(carlToken1));
    }

    @Test
    public void testListTokens_NoTokens() throws Exception {
        //Arrange
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("password").in(userDao);

        // Act
        Collection<AuthToken> authTokens = authService.listTokens(jeff);

        // Assert
        Assert.assertTrue(authTokens.isEmpty());
    }

    @Test(expected = NotAuthorizedException.class)
    public void testListTokens_InvalidUser() throws Exception {
        // Act
        Collection<AuthToken> authTokens = authService.listTokens(null);
    }
}
