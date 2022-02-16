package com.example.safariwebstore008.models;

import com.example.safariwebstore008.common.BaseClass;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token")
public class VerificationToken extends BaseClass {

        private String token;
        @OneToOne(fetch = LAZY)
        private User user;
        private Instant expiryDate;
}
