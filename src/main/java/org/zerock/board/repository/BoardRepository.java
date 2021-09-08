package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 한 개의 ROW(Object) 내에 Objectp[]로 나옴.
    // 엔티티 내부에 연관 관계가 있는 경우 (ON 없음.)
    @Query("SELECT b, w " +
            "FROM Board b " +
            "LEFT JOIN b.writer w " +
            "WHERE b.bno =:bno ")
    Object getBoardWithWriter(@Param("bno") Long bno);

    // 엔티티 간에 연관 관계가 없는 경우 (ON 있음.)
    @Query("SELECT b, r " +
            "FROM Board b " +
            "LEFT JOIN Reply r " +
            "ON r.board = b " +
            "WHERE b.bno =:bno ")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    @Query(value = "SELECT b, w, count(r) " +
            " FROM Board b " +
            " LEFT JOIN b.writer w " +
            " LEFT JOIN Reply r " +
            " ON r.board = b " +
            " GROUP BY b ",
            countQuery = "SELECT count(b) FROM Board b ")
    // countQuery : 메인 쿼리문에 대한 결과가 몇 개 존재하는지에 관련된 속성.
    Page<Object[]> getBoardWithReplyCount(Pageable pageable); // 목록 화면에 필요한 데이터

}
