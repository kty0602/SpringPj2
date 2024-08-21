package com.sparta.second.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "task")
public class Task extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;
    private String title;
    private String contents;
    private String name;
    @Column(name = "delete_status")
    private boolean deleteStatus;

    public void changeTitle(String title) {
        this.title = title;
    }
    public void changeContent(String contents) {
        this.contents = contents;
    }
    public void changeName(String name) {
        this.name = name;
    }
    public void changeDelete(boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }
}
