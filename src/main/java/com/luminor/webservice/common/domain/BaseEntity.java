package com.luminor.webservice.common.domain;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    private UUID id;

    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime updated;

}
