package org.example;

import org.apache.commons.dbcp2.BasicDataSource;
import org.example.data.Participant;
import org.example.data.orm.MyOrm;

import javax.sql.DataSource;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = initDataSource();
        MyOrm myOrm = new MyOrm(dataSource);
        Participant participant = myOrm.findById(Participant.class, 2);
        System.out.println(participant);
//        -----------------------------------------------------------------------------
//         JDBC
//        try (Connection connection = dataSource.getConnection()) {
//            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM participants WHERE id = ?")) {
//                preparedStatement.setObject(1, 3);
//                ResultSet resultSet = preparedStatement.executeQuery();
//                resultSet.next();
//                Participant participant = new Participant();
//                participant.setId(resultSet.getInt("id"));
//                participant.setFirstName(resultSet.getString("first_name"));
//                participant.setSecondName(resultSet.getString("second_name"));
//                participant.setAge(resultSet.getInt("age"));
//                System.out.println(participant);
//            }
//        }
    }

    private static DataSource initDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/my_database");
        dataSource.setUsername("root");
        dataSource.setPassword("1111");
        return dataSource;
    }


}