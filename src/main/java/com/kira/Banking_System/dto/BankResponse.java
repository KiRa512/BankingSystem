package com.kira.Banking_System.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankResponse {

    private String ResponseCode;
    private String ResponseMessage;
    private AccountInfo accountInfo;
}
