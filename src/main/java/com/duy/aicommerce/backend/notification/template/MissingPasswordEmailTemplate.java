package com.duy.aicommerce.backend.notification.template;

public class MissingPasswordEmailTemplate {

    public MissingPasswordEmailTemplate() {}

    public String build(String verifyLink) {

        return """
                <html>
                    <h1>Quên mật khẩu</h1>
                    <p>Vui lòng nhấp vào liên kết dưới đây để tiến hành đổi mật khẩu của bạn</p>
                    <a href = %s> Đổi mật khẩu </a>
                </html>
                """.formatted(verifyLink);
    }
}
