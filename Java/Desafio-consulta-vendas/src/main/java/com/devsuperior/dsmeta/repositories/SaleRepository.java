package com.devsuperior.dsmeta.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.projections.SaleReportProjection;
import com.devsuperior.dsmeta.projections.SaleSummaryProjection;

public interface SaleRepository extends JpaRepository<Sale, Long> {

	@Query("SELECT obj.id AS id, obj.date AS date, obj.amount AS amount, obj.seller.name AS sellerName, SUM(obj.amount) "
			+ "FROM Sale obj " 
			+ "WHERE UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name ,'%')) "
			+ "AND obj.date BETWEEN :minDate AND :maxDate " 
			+ "GROUP BY obj.id, obj.date, obj.amount, obj.seller.name "
			+ "ORDER BY obj.date DESC")
	Page<SaleReportProjection> searchReportPage(LocalDate minDate, LocalDate maxDate, String name, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT tb_seller.name AS sellerName, " 
			+ "SUM(tb_sales.amount) AS total "
			+ "FROM tb_sales " 
			+ "INNER JOIN tb_seller ON tb_seller.id = tb_sales.seller_id "
			+ "WHERE tb_sales.date BETWEEN :minDate and :maxDate " 
			+ "GROUP BY tb_seller.id "
			+ "ORDER BY tb_seller.name")
	List<SaleSummaryProjection> searchSummaryList(LocalDate minDate, LocalDate maxDate);

}
