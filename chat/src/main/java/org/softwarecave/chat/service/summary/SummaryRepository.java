package org.softwarecave.chat.service.summary;

import org.softwarecave.chat.model.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryRepository extends JpaRepository<Summary, Long> {
}
