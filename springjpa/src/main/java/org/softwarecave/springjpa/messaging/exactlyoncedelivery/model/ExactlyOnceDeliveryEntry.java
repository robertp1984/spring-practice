package org.softwarecave.springjpa.messaging.exactlyoncedelivery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exactly_once_delivery_entry")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExactlyOnceDeliveryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "message_id")
    @NotBlank
    private String messageId;

    @Column(name = "type")
    @NotBlank
    private String type;

    public ExactlyOnceDeliveryEntry(String messageId, String type) {
        this.messageId = messageId;
        this.type = type;
    }
}
