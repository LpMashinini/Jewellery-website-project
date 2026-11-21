package com.Backend.Jewellery.event;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class OrderConfirmationEmailTemplate {

    private String storeName;

    public String subject(OrderConfirmation confirmation){
        return "Order #" + confirmation.orderId() + "confirmed -" + storeName;
    }

    public String html(OrderConfirmation confirmation) {

        NumberFormat money = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-ZA"));

        String customerName = (confirmation.customerName() == null || confirmation.customerName().isBlank() ? "Valued Customer" : confirmation.customerName());

        String rows = confirmation.lines().stream()
                .map(line -> """
                        <tr>
                          <td style="padding:10px;border-bottom:1px solid #eeeeee;">%s</td>
                          <td style="padding:10px;border-bottom:1px solid #eeeeee;text-align:center;">%d</td>
                          <td style="padding:10px;border-bottom:1px solid #eeeeee;text-align:right;">%s</td>
                        </tr>
                        """.formatted(
                        HtmlUtils.htmlEscape(line.productName()),
                        line.quantity(),
                        money.format(line.unitPrice().multiply(BigDecimal.valueOf(line.quantity())))))
                .collect(Collectors.joining());


        return """
                <!DOCTYPE html>
                <html>
                <body style="margin:0;padding:0;background:#f7f5f2;font-family:Arial,sans-serif;color:#333333;">
                  <div style="max-width:600px;margin:30px auto;background:#ffffff;border-radius:12px;
                              overflow:hidden;border:1px solid #e6e0d8;">
                    <div style="background:#8a6d3b;color:#ffffff;padding:28px;text-align:center;">
                      <h1 style="margin:0;font-size:26px;">%s</h1>
                      <p style="margin:10px 0 0;">Order Confirmation</p>
                    </div>
                    <div style="padding:28px;">
                      <h2 style="color:#8a6d3b;">Thank you for your order!</h2>
                      <p>Dear %s,</p>
                      <p>Your payment has been confirmed. Here is a summary of order #%s.</p>
                      <table style="width:100%%;border-collapse:collapse;">
                        <tr>
                          <th style="padding:10px;text-align:left;border-bottom:2px solid #8a6d3b;">Item</th>
                          <th style="padding:10px;text-align:center;border-bottom:2px solid #8a6d3b;">Qty</th>
                          <th style="padding:10px;text-align:right;border-bottom:2px solid #8a6d3b;">Price</th>
                        </tr>
                        %s
                        <tr>
                          <td colspan="2" style="padding:10px;font-weight:bold;">Total paid</td>
                          <td style="padding:10px;text-align:right;font-weight:bold;">%s</td>
                        </tr>
                      </table>
                      <p style="margin-top:24px;">Please keep this email for your records.</p>
                      <p style="margin-top:28px;">Warm regards,<br><strong>%s</strong></p>
                    </div>
                    <div style="background:#f7f5f2;padding:16px;text-align:center;font-size:12px;color:#777777;">
                      This is an automated order confirmation email.
                    </div>
                  </div>
                </body>
                </html>
                """.formatted(
                HtmlUtils.htmlEscape(storeName),
                HtmlUtils.htmlEscape(customerName),
                confirmation.orderId(),
                rows,
                money.format(confirmation.total()),
                HtmlUtils.htmlEscape(storeName));
    }
}
