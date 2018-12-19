package org.mybatis.generator;

import org.mybatis.config.DBQuerySQL;
import org.mybatis.generator.api.ConnectionFactory;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.JDBCConnectionFactory;
import org.mybatis.generator.internal.ObjectFactory;
import org.springframework.beans.BeanUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mengtian on 2018-12-19
 */
public class MyGenerator extends MyBatisGenerator {

    public MyGenerator(Configuration configuration, ShellCallback shellCallback, List<String> warnings) throws InvalidConfigurationException {
        super(configuration, shellCallback, warnings);
    }

    public MyGenerator(Configuration configuration, ShellCallback shellCallback, List<String> warnings, boolean generatorAllTables) throws InvalidConfigurationException {
        super(configuration, shellCallback, warnings);
        if (generatorAllTables) {
            generatorAllTables(configuration);
        }
    }

    private void generatorAllTables(Configuration configuration) {
        List<Context> contexts = configuration.getContexts();
        for (Context context : contexts) {
            try (Connection connection = getConnection(context)) {

                DBQuerySQL dbQuerySQL = checkDB(context.getJdbcConnectionConfiguration());

                String dbSchema = getDBSchema(context.getJdbcConnectionConfiguration());

                StringBuilder sqlBuilder = new StringBuilder(dbQuerySQL.getTableQuerySQL());
                sqlBuilder.append("'").append(dbSchema).append("'");
                PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString());

                ResultSet resultSet = statement.executeQuery();

                List<String> allTables = new LinkedList<>();
                while (resultSet.next()) {
                    allTables.add(resultSet.getString(dbQuerySQL.getFieldTableName()));
                }

                List<String> toAddTables = new LinkedList<>();
                List<TableConfiguration> tableConfigurations = context.getTableConfigurations();
                for (TableConfiguration tableConfiguration : tableConfigurations) {
                    for (String table : allTables) {
                        if (!tableConfiguration.getTableName().equals(table)) {
                            toAddTables.add(table);
                        }
                    }
                }

                TableConfiguration tableConfiguration = tableConfigurations.get(0);
                for (String toAddTable : toAddTables) {
                    TableConfiguration newTableConfig = new TableConfiguration(context);
                    BeanUtils.copyProperties(tableConfiguration, newTableConfig);

                    newTableConfig.setTableName(toAddTable);
                    newTableConfig.setDomainObjectName(camel(toAddTable));

                    context.getTableConfigurations().add(newTableConfig);
                }

            } catch (Exception e) {
                throw new RuntimeException("query tables error", e);
            }
        }
    }

    private String camel(String toAddTable) {
        String[] words = toAddTable.split("_");

        StringBuilder tableDomainName = new StringBuilder();
        for (String word : words) {
            tableDomainName.append(word.substring(0, 1).toUpperCase());
            tableDomainName.append(word.substring(1).toLowerCase());
        }
        return tableDomainName.toString();
    }

    private String getDBSchema(JDBCConnectionConfiguration jdbcConnectionConfiguration) {
        String connectionURL = jdbcConnectionConfiguration.getConnectionURL();
        return connectionURL.substring(connectionURL.lastIndexOf("/") + 1, connectionURL.lastIndexOf("?"));
    }

    private DBQuerySQL checkDB(JDBCConnectionConfiguration jdbcConnectionConfiguration) {
        String driverClass = jdbcConnectionConfiguration.getDriverClass();
        for (DBQuerySQL value : DBQuerySQL.values()) {
            if (value.getDriverClass().equals(driverClass)) {
                return value;
            }
        }
        throw new RuntimeException("unSupport db driverClass, please check config or add new driverClass in DBQuerySQL ");
    }

    private Connection getConnection(Context context)
            throws SQLException {
        ConnectionFactory connectionFactory;
        if (context.getJdbcConnectionConfiguration() != null) {
            connectionFactory = new JDBCConnectionFactory(context.getJdbcConnectionConfiguration());
        } else {
            connectionFactory = ObjectFactory.createConnectionFactory(context);
        }

        return connectionFactory.getConnection();
    }
}
