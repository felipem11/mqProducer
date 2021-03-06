package com.felipe.mq.input;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RespostaCreditoAPICaller {

	private String idSimulacao;

	private String situacaoParecerCredito;
	private LocalDateTime dataParecerCredito;
	private LocalDate dataValidadeCredito;

}
