package com.company.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseQueueInfo {
    private String exchange;
    private String routingKey;
}
