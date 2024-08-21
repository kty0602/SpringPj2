package com.sparta.second.repository;

import com.sparta.second.entity.Reply;
import com.sparta.second.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("SELECT r FROM Reply r WHERE r.replyId = :replyId AND r.deleteStatus = false")
    Optional<Reply> getReplyByReplyId(@Param("replyId") Long replyId);

    // case문을 활용하여 deleteStatus가 true인 경우 true를 반환, false라면 false를 반환한다.
    @Query("SELECT CASE WHEN r.deleteStatus = true THEN true ELSE false END FROM Reply r WHERE r.replyId = :replyId")
    boolean isDelete(@Param("replyId") Long replyId);

    // 댓글 삭제
    @Modifying
    @Query("UPDATE Reply r SET r.deleteStatus = true WHERE r.replyId = :replyId")
    void delete(@Param("replyId") Long replyId);

    // deleteStatus가 false인 댓글들만 가져옴
    @Query("SELECT r FROM Reply r WHERE r.deleteStatus = false")
    List<Reply> findAllActiveReplies();
}
