package com.sparta.second.repository;

import com.sparta.second.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("SELECT r FROM Reply r WHERE r.replyId = :replyId AND r.deleteStatus = false")
    Optional<Reply> getReplyByReplyId(@Param("replyId") Long replyId);

    /*
    * deleteStatus가 false인 댓글들만 가져옴
    * */
    @Query("SELECT r FROM Reply r WHERE r.deleteStatus = false")
    List<Reply> findAllActiveReplies();

}
