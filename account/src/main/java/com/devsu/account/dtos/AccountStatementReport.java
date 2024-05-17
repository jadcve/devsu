package com.devsu.account.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatementReport {

    private Long accountId;
    private String numeroCuenta;
    private Double saldoCuenta;
    private List<MovementDetail> movimientos;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovementDetail {
        private Date fecha;
        private String movimiento;
        private Double monto;
    }
}
