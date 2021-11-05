package com.board;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import com.board.domain.BoardDTO;
import com.board.mapper.BoardMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
public class MapperTests {

	@Autowired
	private BoardMapper boardMapper;

	@Test
	public void testOfInsert() {
		BoardDTO params = new BoardDTO();
		params.setTitle("1번 게시글 제목");
		params.setContent("1번 게시글 내용");
		params.setWriter("테스터");

		int result = boardMapper.insertBoard(params);
		System.out.println("결과는 " + result + "입니다.");
	}

	@Test
	public void testOfSelectDetail() {
		BoardDTO board = boardMapper.selectBoardDetail((long) 1);
//try 문에서는 board에 저장된 게시글 정보를 Jackson 라이브러리를 이용해서
//JSON 문자열로 변경한 뒤에 콘솔에 출력합니다.
		try {
			String boardJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(board);

			System.out.println("=========================");
			System.out.println(boardJson);
			System.out.println("=========================");

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testOfUpdate() {
		BoardDTO params = new BoardDTO();
		params.setTitle("1번 게시글 제목을 수정합니다.");
		params.setContent("1번 게시글 내용을 수정합니다.");
		params.setWriter("홍길동");
		params.setIdx((long)1);

		int result = boardMapper.updateBoard(params);

		if(result == 1) {
			BoardDTO board = boardMapper.selectBoardDetail(params.getIdx());

			try {
				String boardJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(board);

				System.out.println("=========================");
				System.out.println(boardJson);
				System.out.println("=========================");

			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testOfDelete() {
		int result = boardMapper.deleteBoard((long)1);
		if(result == 1) {
			BoardDTO board = boardMapper.selectBoardDetail((long)1);

			String boardJson;
			try {
				boardJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(board);
				System.out.println("=========================");
				System.out.println(boardJson);
				System.out.println("=========================");
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Test
	public void testMultipleInsert() {
		for(int i=50; i<=50; i++) {
			BoardDTO params = new BoardDTO();

			params.setTitle(i+"번 게시글 제목");
			params.setContent(i+"번 게시글 내용");
			params.setWriter(i+"번 게시글 작성자");
			boardMapper.insertBoard(params);
		}
	}
//
//	@Test
//	public void testOfSelectList() {
//		int boardTotalCount = boardMapper.selectBoardTotalCount(Criter);
//
//		if(boardTotalCount > 0) { // 게시글이 1개 이상일 때 조회
//			List<BoardDTO> boardList = boardMapper.selectBoardList();
//
//	//if 문에서는 스프링에서 지원해주는 CollectionsUtil의 isEmpty 메서드를 이용해서
//	//boardList가 비어있지 않은지 체크하고, forEach를 실행해서
//
//			if(CollectionUtils.isEmpty(boardList) == false) {
//				for(BoardDTO board : boardList) {
//					System.out.println("=========================");
//					System.out.println(board.getTitle());
//					System.out.println(board.getContent());
//					System.out.println(board.getWriter());
//					System.out.println("=========================");
//				}
//			}
//		}
//
//	}
}
