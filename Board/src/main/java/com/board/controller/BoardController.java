package com.board.controller;

import java.lang.ProcessBuilder.Redirect;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.board.constant.Method;
import com.board.domain.AttachDTO;
import com.board.domain.BoardDTO;
import com.board.service.BoardService;
import com.board.util.UiUtils;

@Controller
public class BoardController extends UiUtils{

	@Autowired
	private BoardService boardService;

	@GetMapping(value="/board/write.do")
	public String openBoardWrite(@ModelAttribute("params") BoardDTO params, @RequestParam(value="idx", required = false) Long idx, Model model) {

		if(idx == null) {
			model.addAttribute("board", new BoardDTO());
		}else {
			BoardDTO board = boardService.getBoardDetail(idx);
			if(board == null || "Y".equals(board.getDeleteYn())) {
				return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/list.do", Method.GET, null, model);
			}
			model.addAttribute("board", board);

			List<AttachDTO> fileList = boardService.getAttachFileList(idx);
			model.addAttribute("fileList", fileList);
		}
		return "board/write";
	}

	@PostMapping(value="/board/register.do")
	public String registerBoard(final BoardDTO params, final MultipartFile[] files, Model model) throws Exception {
		Map<String, Object> pagingParams = getPagingParams(params);
			try {
				boolean isRegisterd = boardService.registerBoard(params, files);
				if(isRegisterd == false) {
					return showMessageWithRedirect("게시글 등록에 실패하였습니다.", "/baord/list.do", Method.GET, pagingParams, model);
				}
			} catch (DataAccessException e) {
				return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
			} catch (Exception e) {
				return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
			}
			return showMessageWithRedirect("게시글 등록이 완료되었습니다.", "/board/list.do", Method.GET, pagingParams, model);
		}

	@GetMapping(value="/board/list.do")
	public String openBoardList(@ModelAttribute("params") BoardDTO params, Model model) {
		List<BoardDTO> boardList = boardService.getBoardList(params);
/*
	@ModelAttribute를 이용하면 파라미터로 전달받은 객체를 자동으로 뷰까지 전달할 수 있습니다.
 	Model 인터페이스의 addAttribute(key, value) 메서드를 이용해서 일일이 화면(View)으로 전달하는데요,
	@ModelAttribute는 별다른 처리 없이 화면(View)으로 파라미터를 전송합니다.
	ModelAttribute 괄호 안의 "criteria"는 화면(View)에서 사용할 별칭(Alias)입니다.
	만약, @ModelAttribute("a") Criteria criteria로 지정했다면
	뷰에서는 ${a.currentPageNo}과 같은 방식으로 객체에 접근할 수 있습니다.
 */

		model.addAttribute("boardList", boardList);

		return "board/list";
	}

	@GetMapping(value = "/board/view.do")
	public String openBoardDetail(@ModelAttribute("params") BoardDTO params, @RequestParam(value = "idx", required = false)Long idx, Model model ) {

		if(idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/list.do", Method.GET, null, model);
		}

		BoardDTO board = boardService.getBoardDetail(idx);
		if(board == null || "Y".equals(board.getDeleteYn())) {
			return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/list.do", Method.GET, null, model);
		}
		model.addAttribute("board", board);
		return "board/view";
	}

	@PostMapping(value="/board/delete.do")
	public String deleteBoard(@ModelAttribute("params") BoardDTO params, @RequestParam(value="idx", required = false)Long  idx, Model model ) {
		if(idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/list.do", Method.GET, null, model);
		}

		Map<String, Object> pagingParams = getPagingParams(params);

		try {
			boolean isDeleted = boardService.deleteBoard(idx);
			if(isDeleted == false) {
				return showMessageWithRedirect("게시글 삭제에 실패하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
			}
		}catch (DataAccessException e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
		}catch(Exception e) {

		}
		return showMessageWithRedirect("게시글 삭제가 완료되었습니다.", "/board/list.do", Method.GET, pagingParams, model);
	}

}
