package rocks.stalin.sw708e16.server.core;

import org.bson.types.ObjectId;
import rocks.stalin.sw708e16.server.core.spatial.Path;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ObjectId id;

    @Column(unique = false, nullable = false)
    private String make;

    @Column(unique = false, nullable = false)
    private String model;

    @Column(unique = false, nullable = false)
    private int vintage;

    @Column(unique = false, nullable = false)
    private Vin vin;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private List<Path> paths = new ArrayList<>();
}
