package com.board.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.board.domain.BoardDTO;
import com.board.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService{

	@Autowired
	private BoardMapper boardMapper;

	@Override
	public boolean registerBoard(BoardDTO params) {
		int queryResult = 0;

		if(params.getIdx() == null) {
			queryResult = boardMapper.insertBoard(params);
		}else {
			queryResult = boardMapper.updateBoard(params);
		}

		return queryResult == 1 ? true : false;
	}

	@Override
	public BoardDTO getBoardDetail(Long idx) {
		return boardMapper.selectBoardDetail(idx);
	}

	@Override
	public boolean deleteBoard(Long idx) {
		int queryResult = 0;

		BoardDTO board = boardMapper.selectBoardDetail(idx);

		if(board != null && "N".equals(board.getDeleteYn())) {
			queryResult = boardMapper.deleteBoard(idx);
		}

		return queryResult == 1 ? true : false;
	}

	@Override
	public List<BoardDTO> getBoardList() {
		//NPE 방지를 위해 Collections 클래스의 emptyList 메서드를 이용해서 비어있는 리스트를 선언합니다.
		List<BoardDTO> boardList = Collections.emptyList();

		int boardTotalCount = boardMapper.selectBoardTotalCount();

		if(boardTotalCount > 0) {
			boardList = boardMapper.selectBoardList();
		}

		return boardList;
	}

}
