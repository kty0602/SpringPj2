package com.sparta.second.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Reply> replyList;

    public void changeTitle(String title) {
        this.title = title;
    }
    public void changeContent(String contents) {
        this.contents = contents;
    }
    public void changeName(String name) {
        this.name = name;
    }
}
