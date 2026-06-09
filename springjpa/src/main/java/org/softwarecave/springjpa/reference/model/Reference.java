package org.softwarecave.springjpa.reference.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table( name = "reference")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("ref")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Reference {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "valueString")
    @NotBlank
    private String valueString;
}
