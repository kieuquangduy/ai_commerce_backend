package com.duy.aicommerce.backend.notification.template;



public class VerificationEmailTemplate {
    public VerificationEmailTemplate() {}

    public String build(String verifyLink) {

        return """
                <html>
                    <h1>Xác minh tài khoản</h1>
                    <p>Vui lòng nhấp vào liên kết dưới đây để tiến hành xác minh tài khoản</p>
                    <a href = %s> Xác minh tài khoản </a>
                </html>
                """.formatted(verifyLink);
    }
}
