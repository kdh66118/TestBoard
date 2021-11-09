package com.board.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachDTO extends CommonDTO{

	private Long idx;
	private Long boardIdx;
	private String originalName;
	private String saveName;
	private long size;


}
