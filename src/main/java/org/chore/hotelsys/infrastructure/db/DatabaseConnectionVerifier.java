package org.chore.hotelsys.infrastructure.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseConnectionVerifier {

    private DatabaseConnectionVerifier() {
    }

    public static VerificationResult verify(String url, String username, String password) {
        if (url == null || url.isBlank()) {
            return new VerificationResult(false, "spring.datasource.url 未配置，无法验证数据库连接", null, null);
        }

        String safeUsername = username == null ? "" : username;
        String safePassword = password == null ? "" : password;

        try (Connection connection = DriverManager.getConnection(url, safeUsername, safePassword)) {
            boolean isValid = connection.isValid(2);
            if (!isValid) {
                return new VerificationResult(false, "数据库连接建立但 isValid 校验失败", null, null);
            }

            try (Statement statement = connection.createStatement(); ResultSet ignored = statement.executeQuery("SELECT 1")) {
                DatabaseMetaData metaData = connection.getMetaData();
                String message = "数据库连接成功：" + metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion();
                return new VerificationResult(true, message, metaData.getDatabaseProductName(), metaData.getDatabaseProductVersion());
            }
        } catch (SQLException e) {
            String message = "数据库连接失败：" + e.getMessage();
            return new VerificationResult(false, message, null, null);
        }
    }

    public record VerificationResult(boolean isConnected, String message, String databaseProductName, String databaseProductVersion) {
    }
}

