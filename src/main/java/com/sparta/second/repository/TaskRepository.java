package com.sparta.second.repository;

import com.sparta.second.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.taskId = :taskId AND t.deleteStatus = false")
    Optional<Task> checkTaskByTaskId(@Param("taskId") Long taskId);

    /*
    * 2024-08-24 일정 첫 생성 시 댓글이 없는 상태여서 단건, 전체 조회가 안되는 현상 해결
    * <2개의 쿼리가 다른 이유>
    * 첫번째 쿼리는 전체 목록을 반환하기 위해서 taskId를 기준으로 조인
    * 두번째 쿼리는 특정 Task 객체를 조회하기 위해서 Task 객체로 직접 조인 -> taskId로 조인을 하면 오류
    * */

    /*
    * 게시글, 댓글의 수를 함께 조회
    * Task객체와 Long타입의 댓글 수를 동시에 반환하므로 Object[]로 받아야 한다.
    * 기존 Task에서 -> Object[]로 변경
    * 일정 과 해당 댓글, 삭제되지 않는 것들만 필터링
    * */
    @Query(value = "SELECT t, COUNT(r) " +
            "FROM Task t " +
            "LEFT JOIN Reply r ON t.taskId = r.task.taskId AND r.deleteStatus = false " +
            "WHERE t.deleteStatus = false " +
            "GROUP BY t")
    Page<Object[]> getTaskWithReplyCount(Pageable pageable);

    /*
    * 특정 bno에 해당하는 게시글과 사용자 정보 댓글의 정보를 제공
    * 댓글을 삭제하여 false처리가 되어도 카운팅하는 현상을 수정
    * */
    @Query(value = "SELECT t, COUNT(r) " +
            "FROM Task t " +
            "LEFT JOIN Reply r ON r.task = t AND r.deleteStatus = false " +
            "WHERE t.taskId = :taskId AND t.deleteStatus = false " +
            "GROUP BY t")
    Optional<Object> getTaskByTaskId(@Param("taskId") Long taskId);
}
