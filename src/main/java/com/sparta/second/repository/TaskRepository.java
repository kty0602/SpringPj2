package com.sparta.second.repository;

import com.sparta.second.entity.Reply;
import com.sparta.second.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.taskId = :taskId AND t.deleteStatus = false")
    Optional<Task> checkTaskByTaskId(@Param("taskId") Long taskId);

    // 게시글, 댓글의 수를 함께 조회
    // Task객체와 Long타입의 댓글 수를 동시에 반환하므로 Object[]로 받아야 한다.
    // 기존 Task에서 -> Object[]로 변경
    // 일정 과 해당 댓글, 삭제되지 않는 것들만 필터링
    @Query(value = "SELECT t, COUNT(r) " +
            "FROM Task t " +
            "LEFT JOIN Reply r ON t.taskId = r.task.taskId " +
            "WHERE t.deleteStatus = false AND r.deleteStatus = false " +
            "GROUP BY t",
            countQuery = "SELECT COUNT(t) FROM Task t")
    Page<Object[]> getTaskWithReplyCount(Pageable pageable);

    // 특정 bno에 해당하는 게시글과 사용자 정보 댓글의 정보를 제공
    // 댓글을 삭제하여 false처리가 되어도 카운팅하는 현상을 수정
    // (r is null or r.rdelete <> false) -> false인 댓글을 카운팅하지 않는다.
    @Query("SELECT t, COUNT(r) " +
            "FROM Task t " +
            "LEFT OUTER JOIN Reply r on r.task = t " +
            "where t.taskId = :taskId and (r is null or r.deleteStatus = false) group by t")
    Object getTaskByTaskId(@Param("taskId") Long taskId);
}
