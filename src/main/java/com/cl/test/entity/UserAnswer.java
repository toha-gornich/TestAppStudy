package com.cl.test.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("user_answers")
public class UserAnswer {
    @Column("test_result_id")
    private Long testResultId;

    @Column("question_id")
    private Long questionId;

    @Column("answer_id")
    private Long answerId;
}