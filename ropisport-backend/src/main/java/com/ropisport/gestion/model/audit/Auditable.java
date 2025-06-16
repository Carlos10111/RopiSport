package com.ropisport.gestion.model.audit;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data  // Con @Data no necesitas @Getter @Setter
public abstract class Auditable {

    // Cuándo se creó este registro (se llena automáticamente)
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Cuándo se modificó por última vez (se actualiza automáticamente)
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Quién creó este registro (se llena automáticamente con el usuario logueado)
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    // Quién modificó por última vez (se actualiza automáticamente)
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
}