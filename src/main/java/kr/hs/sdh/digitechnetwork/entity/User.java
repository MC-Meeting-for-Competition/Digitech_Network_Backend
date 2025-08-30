package kr.hs.sdh.digitechnetwork.entity;

import jakarta.persistence.*;
import kr.hs.sdh.digitechnetwork.enums.UserType;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.UserDetails;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class User extends BaseEntity implements UserDetails {
    @Column(nullable = false)
    protected String name;

    @Column(nullable = false)
    protected String hashedPassword;

    @Column(nullable = false, unique = true)
    protected String email;

    @Column(nullable = false)
    protected String phoneNumber;

    @Enumerated(EnumType.STRING)
    protected UserType role;
}
