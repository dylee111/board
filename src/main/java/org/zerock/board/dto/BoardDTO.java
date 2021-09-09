package org.zerock.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardDTO {
    private Long bno;
    private String title;
    private String content;
    private String writerEmail; // 작성자 이메일(ID)
    private String writerName;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int replyCount; // 해당 게시글 댓글 수
}
