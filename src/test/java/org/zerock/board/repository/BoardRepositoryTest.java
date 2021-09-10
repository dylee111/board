package org.zerock.board.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

//    @Test
//    public void insertBoard() {
//        IntStream.rangeClosed(1, 100).forEach(i -> {
//            Member member = Member.builder().email("user"+i+"@ds.com").build();
//
//            Board board = Board.builder()
//                    .title("Title..." + i)
//                    .content("Content..." + i)
//                    .writer(member)
//                    .build();
//            boardRepository.save(board);
//        });
//    } // insertBoard()

    @Transactional
    @Test
    public void testRead1() {
        Optional<Board> result = boardRepository.findById(100L);

        if(result.isPresent()) {
            Board board = result.get();

            System.out.println(board);
            System.out.println(board.getWriter());
        }
    } // testRead1()

    @Test
    public void testReadWithWriter() {
        Object result = boardRepository.getBoardWithWriter(100L);

        Object[] arr = (Object[]) result;
        System.out.println("==================================");
        System.out.println(Arrays.toString(arr));
    } // testReadWithWriter()

    @Test
    public void testReadWithReply() {
        List<Object[]> result = boardRepository.getBoardWithReply(100L);

        for(Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    } // testReadWithReply()

    @Test
    public void testWithReplyCount() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        result.get().forEach(row -> {
            Object[] arr = (Object[]) row;
            System.out.println(Arrays.toString(arr));
        });
    } // testWithReplyCount()

    @Test
    public void testRead3() {
        Object result = boardRepository.getBoardByBno(100L);
        Object[] arr = (Object[]) result;
        System.out.println(Arrays.toString(arr));
    } // testRead3()

    @Test
    public void testSearch1() {
        /**
         * BoardRepository는 SearchBoardRepository를 상속 받았지만,
         * Spring에 의해서 실제 구현된 SearchBoardRepository의 search1()을 사용 가능.
         */
        boardRepository.search1();
    }
}