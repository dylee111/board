package org.zerock.board.service;

import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyService {
    Long register(ReplyDTO replyDTO); // 댓글 등록

    // 매개변수 bno를 통해서 해당 글의 대한 정보를 불러옴.
    List<ReplyDTO> getList(Long bno); // 게시물의 댓글 목록

    void modify(ReplyDTO replyDTO); // 댓글 수정

    void remove(Long rno); // 댓글 삭제

    /**
     *  DTO -> Entity
     */
    default Reply dtoToEntity(ReplyDTO replyDTO) {
        Board board = Board.builder().bno(replyDTO.getBno()).build();

        Reply reply = Reply.builder()
                .rno(replyDTO.getRno())
                .text(replyDTO.getText())
                .replyer(replyDTO.getReplyer())
                .board(board)
                .build();
        return reply;
    } //dtoToEntity()


    /**
     *  Entity -> DTO
     */
    default ReplyDTO entityToDTO(Reply reply) {
        ReplyDTO dto = ReplyDTO.builder()
                .rno(reply.getRno())
                .text(reply.getText())
                .replyer(reply.getReplyer())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();
        return dto;
    } //dtoToEntity()
}
