package com.example.cloudflareassignment.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CERTS", indexes = @Index(name = "index_user_id", columnList = "user_id, active"))
public class CertDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDomain user;

    @Builder.Default
    @Column(name = "active")
    private boolean isActive = false;

    @Lob
    private byte[] pk;

    @Lob
    private byte[] cert;

}
