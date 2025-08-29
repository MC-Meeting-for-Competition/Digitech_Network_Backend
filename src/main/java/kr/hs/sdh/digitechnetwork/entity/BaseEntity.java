package kr.hs.sdh.digitechnetwork.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime lastUpdateTime;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
