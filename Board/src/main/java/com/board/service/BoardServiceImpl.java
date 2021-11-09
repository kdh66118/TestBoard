package com.board.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.board.domain.AttachDTO;
import com.board.domain.BoardDTO;
import com.board.mapper.AttachMapper;
import com.board.mapper.BoardMapper;
import com.board.paging.PaginationInfo;
import com.board.util.FileUtils;

@Service
public class BoardServiceImpl implements BoardService{

	@Autowired
	private BoardMapper boardMapper;

	@Autowired
	private AttachMapper attachMapper;

	@Autowired
	private FileUtils fileUtils;

	@Override
	public boolean registerBoard(BoardDTO params)
			throws RuntimeException {
		int queryResult = 0;

		if(params.getIdx() == null) {
			queryResult = boardMapper.insertBoard(params);
		}else {
			queryResult = boardMapper.updateBoard(params);
		}
//
//		BoardDTO board = null;
//		System.out.println(board.getTitle());

		return queryResult == 1 ? true : false;
	}

	@Override
	public boolean registerBoard(BoardDTO params, MultipartFile[] files) {
		int queryResult = 1;

		if(registerBoard(params) == false) {
			return false;
		}

		List<AttachDTO> fileList = fileUtils.uploadFiles(files, params.getIdx());

		if(CollectionUtils.isEmpty(fileList) == false) {
			queryResult = attachMapper.insertAttach(fileList);
			if(queryResult < 1) {
				queryResult = 0;
			}
		}


		return (queryResult > 0);
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
	public List<BoardDTO> getBoardList(BoardDTO params) {
		//NPE 방지를 위해 Collections 클래스의 emptyList 메서드를 이용해서 비어있는 리스트를 선언합니다.
		List<BoardDTO> boardList = Collections.emptyList();

		int boardTotalCount = boardMapper.selectBoardTotalCount(params);

		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(boardTotalCount);

		params.setPaginationInfo(paginationInfo);

		if(boardTotalCount > 0) {
			boardList = boardMapper.selectBoardList(params);
		}

		return boardList;
	}


}
