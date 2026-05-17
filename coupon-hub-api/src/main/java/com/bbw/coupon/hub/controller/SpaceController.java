package com.bbw.coupon.hub.controller;

import com.gigaspaces.document.SpaceDocument;
import org.openspaces.core.GigaSpace;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpaceController {

    private final GigaSpace couponsSpace;

    public SpaceController(GigaSpace couponsSpace) {
        this.couponsSpace = couponsSpace;
    }

    @GetMapping("/transactions")
    public SpaceDocument[] getTransactions() {
        //Implement transactions
        SpaceDocument[] transactions = null;
        return transactions;
    }
}
