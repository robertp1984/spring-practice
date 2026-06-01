package org.softwarecave.springjpa.messaging.exactlyoncedelivery.service;

import org.softwarecave.springjpa.messaging.exactlyoncedelivery.model.ExactlyOnceDeliveryEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExactlyOnceDeliveryEntryRepository extends JpaRepository<ExactlyOnceDeliveryEntry, String> {
    Optional<ExactlyOnceDeliveryEntry> findByMessageIdAndType(String messageId, String type);
}
