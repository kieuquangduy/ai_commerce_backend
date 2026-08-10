package com.duy.aicommerce.backend.user.entity;


import com.duy.aicommerce.backend.role.entity.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @Column(length = 50)
    private String firstName;
    @Column(length = 50)
    private String lastName;

    @Column(name = "is_profile_completed")
    private Boolean profileCompleted;
    @Column(name = "is_verified")
    private Boolean verified;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();




}
