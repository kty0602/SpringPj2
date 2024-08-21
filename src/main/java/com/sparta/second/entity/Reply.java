package com.sparta.second.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "reply")
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long replyId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;
    private String contents;
    private String name;
    @Column(name = "delete_status")
    private boolean deleteStatus;

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
