package rocks.stalin.sw708e16.server.core.authentication;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import rocks.stalin.sw708e16.server.core.User;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Entity
@Table
public class AuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private User user;

    @Temporal(TemporalType.DATE)
    private Date created;

    @Temporal(TemporalType.DATE)
    private Date expires;


    /**
     * Create with specified token.
     * @param token The token string
     * @param user The {@link User} owning the token
     */
    public AuthToken(String token, User user) {
        this.token = token;
        this.user = user;
        created = Calendar.getInstance().getTime();
    }

    /**
     * Create with a randomly generated token.
     * @param user The {@link User} the token is for
     */
    public AuthToken(User user) {
        //Make token
        Random random = new SecureRandom();

        this.token = new BigInteger(120, random).toString(32);
        this.user = user;
        created = Calendar.getInstance().getTime();
    }

    protected AuthToken() {
    }

    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public Date getCreated() {
        return created;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
}
