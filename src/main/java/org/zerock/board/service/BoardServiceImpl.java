package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {
    private final BoardRepository repository;
    private final ReplyRepository replyRepository;

    /**
     *  글 등록
     */
    @Override
    public Long register(BoardDTO dto) {
        log.info(dto);
        Board board = dtoToEntity(dto);
        repository.save(board);

        return board.getBno();
    } // register()

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        Function<Object[], BoardDTO> fn = (en ->
                entityToDTO((Board)en[0],(Member) en[1], (Long) en[2]));

//        Page<Object[]> result = repository.getBoardWithReplyCount
//                (pageRequestDTO.getPageable(Sort.by("bno").descending()));
        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())
        );

        return new PageResultDTO<>(result, fn);
    } // getList()

    @Override
    public BoardDTO get(Long bno) {
        Object result = repository.getBoardByBno(bno);
        Object[] arr = (Object[]) result;

        return entityToDTO((Board) arr[0], (Member) arr[1], (Long) arr[2]);
    } // get()


    /**
     *  댓글 삭제
     */
    // 댓글 삭제와 게시물 삭제는 하나의 트랜젝션으로 처리되어야 한다!
    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        // 댓글부터 삭제!
        replyRepository.deleteByBno(bno);
        repository.deleteById(bno);

    } // removeWithReplies()

//    @Transactional
//    @Override
//    public void modify(BoardDTO boardDTO) {
//        Board board = repository.getById(boardDTO.getBno());
//        board.changeTitle(boardDTO.getTitle());
//        board.changeContent(boardDTO.getContent());
//
//        repository.save(board);
//    } // modify()

    /**
     *  글 수정
     */
    @Override // @Transactional 필요없음.
    public void modify(BoardDTO boardDTO) {
        Optional<Board> result = repository.findById(boardDTO.getBno());
        if(result.isPresent()) {
            Board board = result.get();
            board.changeTitle(boardDTO.getTitle());
            board.changeContent(boardDTO.getContent());
            repository.save(board);
        }
    } // modify()

} // Class End
