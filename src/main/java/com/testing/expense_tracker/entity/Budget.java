package com.testing.expense_tracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "budgets",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_budgets_user_period_category",
                columnNames = {"user_id", "period_month", "category_id"}),
        indexes = @Index(name = "idx_budgets_user_period", columnList = "user_id, period_month"))
@Check(name = "chk_budgets_amount_limit_and_period_start",
        constraints = "amount_limit > 0 and extract(day from period_month) = 1")
@Getter
@Setter
@NoArgsConstructor
public class Budget extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Null means the budget applies to all categories (overall budget).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * First day of the budgeted month.
     */
    @Column(name = "period_month", nullable = false)
    private LocalDate periodMonth;

    @Column(name = "amount_limit", nullable = false, precision = 15, scale = 2)
    private BigDecimal amountLimit;
}
