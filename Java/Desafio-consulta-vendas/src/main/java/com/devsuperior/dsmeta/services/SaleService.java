package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.projections.SaleReportProjection;
import com.devsuperior.dsmeta.projections.SaleSummaryProjection;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;

	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleReportDTO> getReportPage(String minDate, String maxDate, String name, Pageable pageable) {
		LocalDate min = convertMinDate(minDate);
		LocalDate max = convertMaxDate(maxDate);
		Page<SaleReportProjection> page = repository.searchReportPage(min, max, name, pageable);
		Page<SaleReportDTO> result = page.map(x -> new SaleReportDTO(x));
		return result;
	}

	public List<SaleSummaryDTO> searchSummaryList(String minDate, String maxDate) {
		LocalDate min = convertMinDate(minDate);
		LocalDate max = convertMaxDate(maxDate);
		List<SaleSummaryProjection> list = repository.searchSummaryList(min, max);
		List<SaleSummaryDTO> result = list.stream().map(x -> new SaleSummaryDTO(x)).collect(Collectors.toList());
		return result;

	}

	private LocalDate convertMaxDate(String maxDate) {
		LocalDate now = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate max = "".equals(maxDate) ? now : LocalDate.parse(maxDate);
		return max;
	}

	private LocalDate convertMinDate(String minDate) {
		LocalDate now = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate yearAgo = now.minusYears(1L);
		LocalDate min = "".equals(minDate) ? yearAgo : LocalDate.parse(minDate);
		return min;
	}

}
