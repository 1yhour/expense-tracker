package com.testing.expense_tracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "user_settings")
@Getter
@Setter
@NoArgsConstructor
public class UserSettings extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "currency_code", nullable = false, length = 3)
    @ColumnDefault("'USD'")
    private String currencyCode = "USD";

    @Column(name = "notifications_enabled", nullable = false)
    @ColumnDefault("true")
    private boolean notificationsEnabled = true;

    @Column(name = "budget_alert_enabled", nullable = false)
    @ColumnDefault("true")
    private boolean budgetAlertEnabled = true;
}
