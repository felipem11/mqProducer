package com.example.mqspring.input;

import java.time.LocalDateTime;

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
	
//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime dataHoraDecisao;
	private LocalDateTime validadeDecisao;

}
