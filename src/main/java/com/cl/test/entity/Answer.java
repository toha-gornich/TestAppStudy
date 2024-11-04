package com.cl.test.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("answers")
public class Answer {
    @Id
    private Long id;

    private String text;

    @Column("is_correct")
    private Boolean isCorrect;

    @Column("question_id")
    private Long questionId;

    public void setCorrect(Boolean active) {
        this.isCorrect = active;
    }

    public Boolean isCorrect() {
        return isCorrect;
    }
}