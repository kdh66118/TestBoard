package com.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.board.domain.CommentDTO;

@Mapper
//@Mapper	해당 인터페이스가 데이터베이스와 통신하는 인터페이스임을 의미합니다.
public interface CommentMapper {

	public int insertComment(CommentDTO Params);

	public CommentDTO selectCommentDetail(Long idx);

	public int updateComment(CommentDTO params);

	public int deleteComment(Long idx);

	public List<CommentDTO> selectCommentList(CommentDTO params);

	public int selectCommentTotalCount(CommentDTO params);
}
