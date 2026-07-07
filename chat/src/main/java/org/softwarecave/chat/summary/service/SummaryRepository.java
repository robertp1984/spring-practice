package org.softwarecave.chat.summary.service;

import org.softwarecave.chat.summary.model.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryRepository extends JpaRepository<Summary, Long> {
}
