package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    /**
     *  삭제 시 댓글 삭제
     */
    @Modifying // Queries that require a `@Modifying` annotation include INSERT, UPDATE, DELETE, and DDL statements
    @Query("DELETE FROM Reply r WHERE r.board.bno=:bno ")
    void deleteByBno(Long bno);

    /**
     *  게시물로 댓글 목록 가져오가
     */
    List<Reply> getRepliesByBoardOrderByRno(Board board);
}
