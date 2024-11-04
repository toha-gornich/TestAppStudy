package com.cl.test.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@Table("questions")
public class Question {
    @Id
    private Long id;

    private String text;

    @Column("type")
    private QuestionType type;

    @Column("test_id")
    private Long testId;
}