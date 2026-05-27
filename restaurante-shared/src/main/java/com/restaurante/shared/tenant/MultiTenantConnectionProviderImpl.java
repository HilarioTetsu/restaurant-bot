package com.restaurante.shared.tenant;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final DataSource dataSource;

    public MultiTenantConnectionProviderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    // Hibernate 6 requiere que el tenantIdentifier sea un Object
    @Override
    public Connection getConnection(Object tenantIdentifier) throws SQLException {
        final Connection connection = getAnyConnection();
        try (Statement statement = connection.createStatement()) {
            // Convertimos el objeto a String de forma segura para PostgreSQL
            statement.execute("SET search_path TO " + tenantIdentifier.toString());
        } catch (SQLException e) {
            connection.close();
            throw e;
        }
        return connection;
    }

    // Hibernate 6 requiere que el tenantIdentifier sea un Object
    @Override
    public void releaseConnection(Object tenantIdentifier, Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("SET search_path TO " + TenantContext.DEFAULT_TENANT);
        } catch (SQLException e) {
            connection.close();
            throw e;
        }
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        if (unwrapType.isInstance(this)) {
            return unwrapType.cast(this);
        }
        return null;
    }
}