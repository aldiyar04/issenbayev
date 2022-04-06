package kz.iitu.itse1910.issenbayev.entity;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class User extends BaseEntity {
    public static final String ROLE_ADMIN = "Admin";
    public static final String ROLE_MANAGER = "Manager";
    public static final String ROLE_LEAD_DEV = "Lead Dev";
    public static final String ROLE_DEVELOPER = "Developer";

    @Column
    private String role;

    @Column
    private String email;

    @Column
    private String username;

    @Column
    private String password;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @PrePersist
    private void setCreatedOn() {
        createdOn = LocalDate.now();
    }

    @Builder
    public User(Long id, Long version, String role, String email, String username, String password, LocalDate createdOn) {
        super(id, version);
        this.role = role;
        this.email = email;
        this.username = username;
        this.password = password;
        this.createdOn = createdOn;
    }
}
