package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.service.ReplyService;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/replies/")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService; // 자동 주입을 위해 final

    // ResponseEntity : 사용자의 HttpRequest에 대한 응답 데이터를 포함하는 클래스(
    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno) {

        log.info("bno >>> " + bno);

        return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);
    } // getListByBoard()

    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO) {
        log.info("replyDTO >>> " + replyDTO);
        Long rno = replyService.register(replyDTO);
        return new ResponseEntity<>(rno, HttpStatus.OK);
    } // register()

    // @PathVariable : URL 경로에 변수를 넣어줌. Mapping 어노테이션 {}내에 선언된 변수로 값을 넘김.
    // @DeleteMapping : Annotation for mapping HTTP DELETE requests onto specific handler methods.
    @DeleteMapping("/{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {

        log.info("RNO >>> " + rno);

        replyService.remove(rno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    } // remove()

    // @PutMapping : Annotation for mapping HTTP PUT(Update) requests onto specific handler methods.
    @PutMapping("/{rno}")
    public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO) {
        log.info("ReplyDTO >>> " + replyDTO);
        replyService.modify(replyDTO);
        return new ResponseEntity<>("success", HttpStatus.OK);
    } // modify()
}
