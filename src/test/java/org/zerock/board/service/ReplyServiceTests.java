package org.zerock.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.ReplyDTO;

import java.util.List;
import java.util.function.Consumer;

@SpringBootTest
public class ReplyServiceTests {
    @Autowired
    private ReplyService service;

    @Test
    public void testGetList() {
        Long bno = 100L;

        // bno에 해당하는 글의 댓글을 list에 담음.
        List<ReplyDTO> replyDTOList = service.getList(bno);

        replyDTOList.forEach(replyDTO -> System.out.println(replyDTO));

//        Consumer : 입력 X, 출력만 O
//        replyDTOList.forEach(new Consumer<ReplyDTO>() {
//            @Override
//            public void accept(ReplyDTO replyDTO) {
//                System.out.println(replyDTO);
//            }
//        });
    }
}
