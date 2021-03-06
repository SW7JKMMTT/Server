package rocks.stalin.sw708e16.server.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class PersistFileHandle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(nullable = false, unique = true)
    @JsonIgnore
    private String filePath;

    public PersistFileHandle() {
    }

    public PersistFileHandle(String filePath) {
        this.filePath = filePath;
    }

    public long getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }
}
