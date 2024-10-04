package org.example.data;

import lombok.Data;
import org.example.annotations.Column;
import org.example.annotations.Id;
import org.example.annotations.Table;

import java.lang.annotation.Target;

@Data
@Table("participants")
public class Participant {

    @Id
    private Integer id;
    @Column("first_name")
    private String firstName;
    private String secondName;
    private int age;

}
