package dk.aau.giraf.rest.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.aau.giraf.rest.core.weekschedule.WeekSchedule;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "Department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "department")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Collection<User> members;

    protected Department() {
    }

    public Department(String name) {
        this.name = name;
    }

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "department")
    private Collection<WeekSchedule> weekSchedules;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonIgnore
    public Collection<User> getMembers() {
        return members;
    }

    public Collection<WeekSchedule> getWeekSchedules() {
        return weekSchedules;
    }

    @JsonProperty
    public int getMemberCount() {
        return getMembers().size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return (int)(getId() ^ (getId() >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || this.getClass() != obj.getClass()) return false;

        Department that = (Department)obj;

        return this.getId() == that.getId();
    }
}
