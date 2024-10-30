package com.example.QLDA.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentControllr {
    @GetMapping("/thanhtoan")
    public String login() {
        return "payment/thanhtoan";
    }

}

