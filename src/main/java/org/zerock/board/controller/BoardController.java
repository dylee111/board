package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.BoardService;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping({"", "/", "/list"})
    public String boardHome(PageRequestDTO pageRequestDTO, Model model) {
        log.info("/board/list...");
        log.info(">>>>" + pageRequestDTO);
        model.addAttribute("result", boardService.getList(pageRequestDTO));
        return "/board/list";
    }

    @GetMapping("/register")
    public void register() {
        log.info("Register Get...");
    }

    @PostMapping("/register")
    public String registerPost(BoardDTO dto, RedirectAttributes redirectAttributes) {
        Long bno = boardService.register(dto);

        log.info("RegisterPost : " + bno);
        redirectAttributes.addFlashAttribute("msg", bno);
        redirectAttributes.addFlashAttribute("noti", "등록");

        return "redirect:/board/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(@ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model, Long bno) {
        log.info(bno);
        BoardDTO boardDTO = boardService.get(bno);
        log.info(boardDTO);
        model.addAttribute("dto", boardDTO);
    }

    // String 의 리턴값은 임의 주소를 재지정 가능.
    @PostMapping("/modify")
    public String modify(RedirectAttributes redirectAttributes, BoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO) {

        log.info("modify---------------------------");
        log.info(dto);
        boardService.modify(dto);

        redirectAttributes.addFlashAttribute("page", requestDTO.getPage());
        redirectAttributes.addFlashAttribute("type", requestDTO.getType());
        redirectAttributes.addFlashAttribute("keyword", requestDTO.getKeyword());

        redirectAttributes.addAttribute("bno", dto.getBno());

        return "redirect:/board/read";
    }

    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes) {
        boardService.removeWithReplies(bno);

        redirectAttributes.addFlashAttribute("msg", bno);
        redirectAttributes.addFlashAttribute("noti", "삭제");

        return "redirect:/board/list";
    }

}
