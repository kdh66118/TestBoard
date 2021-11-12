package com.board.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.board.adapter.GsonLocalDateTimeAdapter;
import com.board.domain.CommentDTO;
import com.board.service.CommentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RestController
public class CommentController {

	@Autowired
	private CommentService commentService;

	@RequestMapping(value= { "/comments", "comments/{idx}" }, method = { RequestMethod.POST, RequestMethod.PATCH })
	public JsonObject registerComment(@PathVariable(value="idx", required=false)Long idx, @RequestBody final CommentDTO params) {

		JsonObject jsonObj = new JsonObject();

		try {
			boolean isRegistered = commentService.registerComment(params);
			jsonObj.addProperty("result", isRegistered);

		} catch (DataAccessException e) {
			jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");
		} catch (Exception e) {
			jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
		}

		return jsonObj;
	}


	@GetMapping(value = "/comments/{boardIdx}")
	public JsonObject getCommentList(@PathVariable("boardIdx") Long boardIdx, @ModelAttribute("parms") CommentDTO params) {

		JsonObject jsonObj = new JsonObject();

		List<CommentDTO> commentList = commentService.getCommentList(params);
		if(CollectionUtils.isEmpty(commentList) == false) {

			Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();

			JsonArray jsonArr = gson.toJsonTree(commentList).getAsJsonArray();
			jsonObj.add("commentList", jsonArr);
		}
		return jsonObj;
	}

	@DeleteMapping(value = "/comments/{idx}")
	public JsonObject deleteComment(@PathVariable("idx") final Long idx) {

		JsonObject jsonObj = new JsonObject();

		try {
			boolean isDeleted = commentService.deleteComment(idx);
			jsonObj.addProperty("result", isDeleted);
		} catch (DataAccessException e) {
			jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");

		} catch (Exception e) {
			jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
		}

		return jsonObj;
	}

}
/*
	 @PathVariable

	@RequestParam과 유사한 기능을 하며,

	REST 방식에서 리소스를 표현하는 데 사용됩니다.

	@PathVariable은 URI에 파라미터로 전달받을 변수를 지정할 수 있습니다.

	"/comments/{boardIdx}" URI의 {boardIdx}는 게시글 번호를 의미하며,

	@PathVariable의 boardIdx와 매핑(바인딩)됩니다.


	@ModelAttribute

	파라미터로 전달받은 객체를 자동으로 화면(뷰)로 전달합니다.

	기본적인 댓글 기능의 구현이 마무리되면 댓글 목록의 페이징 처리에 사용됩니다.

	지금은 CommentService의 getCommentList 메서드의 인자로

	게시글 번호(boardIdx)를 포함하고 있는 params를 전달해서

	특정 게시글에 등록된 댓글을 조회하는 것이 전부입니다.
 * */

