package pl.lewandowski;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DbUtil {
    private volatile static DbUtil instance;
    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASS;

    private HikariConfig config = new HikariConfig();
    private HikariDataSource dataSource;

    private static final Logger log = LoggerFactory.getLogger(DbUtil.class);

    private DbUtil() throws IOException {
        Map<String, String> configMap = getDbAccessFromFile();

        if (configMap.containsKey("solar-database-url")) {
            this.DB_URL = configMap.get("solar-database-url");
        } else {
            throw new IOException("Database URL not found");
        }

        if (configMap.containsKey("solar-database-user")) {
            this.DB_USER = configMap.get("solar-database-user");
        } else {
            throw new IOException("Database user name not found");
        }

        if (configMap.containsKey("solar-database-password")) {
            this.DB_PASS = configMap.get("solar-database-password");
        } else {
            throw new IOException("Database password not found");
        }

        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASS);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }

    public static DbUtil getInstance() throws IOException{
        if (instance == null) {
            synchronized (DbUtil.class) {
                if (instance == null) {
                    instance = new DbUtil();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    private Map<String, String> getDbAccessFromFile() throws IOException {
        Path config = Paths.get("db-connection.txt");
        Map<String, String> configMap = Files.readAllLines(config).stream()
                .map(line -> new AbstractMap.SimpleEntry<>(
                        line.substring(0, line.indexOf('=')).trim(),
                        line.substring(line.indexOf('=') + 1).trim()))
                .collect(Collectors.toMap(
                        AbstractMap.SimpleEntry::getKey,
                        AbstractMap.SimpleEntry::getValue));

        if (log.isTraceEnabled()) {
            configMap.forEach((key, value) -> log.trace("new config entry with key '{}' and value '{}' created", key, value));
        }
        return configMap;
    }

}
