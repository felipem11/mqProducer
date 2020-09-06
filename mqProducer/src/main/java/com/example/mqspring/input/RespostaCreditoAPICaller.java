package com.example.mqspring.input;

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

	private String codigoDecisao;
	private String nomeDecisao;
	private String documentosSolicitados;
	
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dataHoraDecisao;
	private LocalDate validadeDecisao;

}
