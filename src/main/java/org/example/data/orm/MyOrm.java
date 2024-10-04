package org.example.data.orm;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.annotations.Column;
import org.example.annotations.Id;
import org.example.annotations.Table;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

@AllArgsConstructor
public class MyOrm {

    private DataSource dataSource;

    @SneakyThrows
    public <T> T findById(Class<T> type, Object id) {
        try (Connection connection = dataSource.getConnection()) {
            String tableName = type.getAnnotation(Table.class).value();
            String fieldName = Arrays.stream(type.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Id.class))
                    .findFirst()
                    .map(this::resolveColumnName)
                    .orElseThrow(() -> new RuntimeException("Smth is wrong with field name"));
            String query = "SELECT * FROM %s WHERE %s =?".formatted(tableName, fieldName);
            try (PreparedStatement prepareStatement = connection.prepareStatement(query)) {
                prepareStatement.setObject(1, id);
                ResultSet resultSet = prepareStatement.executeQuery();
                resultSet.next();
               return createEntityFromRS(type, resultSet);
            }
        }
    }

    @SneakyThrows
    private <T> T createEntityFromRS(Class<T> type, ResultSet resultSet) {
        T entity = type.getConstructor().newInstance();
        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            String columnName = convertToUnderscore(field.getName());
            Object columnValue = resultSet.getObject(columnName);
            field.set(entity, columnValue);
        }
        return entity;
    }

    private String resolveColumnName(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            return field.getAnnotation(Column.class).value();
        }
        return convertToUnderscore(field.getName());
    }

    private String convertToCamelCase(String underscoreString) {
        String[] splitStrings = underscoreString.split("_");
        StringBuilder camelCaseString = new StringBuilder(splitStrings[0]);
        for (int i = 1; i < splitStrings.length; i++) {
            camelCaseString.append(splitStrings[i].substring(0, 1).toUpperCase())
                    .append(splitStrings[i].substring(1).toLowerCase());
        }
        return camelCaseString.toString();
    }

    private String convertToUnderscore(String camelCaseString) {
        return camelCaseString.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

}
