package org.expense.tracker.budget.service.repository;

import org.expense.tracker.budget.service.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query("SELECT b FROM Budget b " +
            "WHERE :date BETWEEN b.startDate AND b.endDate " +
            "AND :userId = b.userId")
    List<Budget> findBudgetByDate(@Param("date") LocalDate date, @Param("userId") BigInteger userId);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Budget b " +
            "WHERE (:startDate <= b.endDate AND :endDate >= b.startDate) " +
            "AND b.userId = :userId")
    boolean existsByOverlappingDates(@Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate,
                                     @Param("userId") BigInteger userId);

    @Query("select case when count(b) > 0 then true else false end from Budget b where :startDate <= (select max(b1.endDate) from Budget b1 where :userId = b.userId )")
    boolean checkValidStartDate(@Param("startDate") LocalDate startDate,@Param("userId") BigInteger userId);
}
