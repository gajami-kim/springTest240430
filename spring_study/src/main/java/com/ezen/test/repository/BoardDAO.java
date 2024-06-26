package com.ezen.test.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ezen.test.domain.BoardVO;
import com.ezen.test.domain.PagingVO;

public interface BoardDAO {

	int insert(BoardVO bvo);

	List<BoardVO> getList(PagingVO pgvo);

	BoardVO getDetail(int bno);

	int update(BoardVO bvo);

	void isDel(int bno);

	void update_readCount(int bno);

	int getTotal(PagingVO pgvo);

	int selectBno();

	//댓글수
	int updateComment(@Param("bno")int bno, @Param("cnt")int cnt);
	//댓글수

	//댓삭제
	int deleteComment(int bno);
	//댓삭제

	//파일카운트
	void fileCount(int bno);

	//기존 DB 값 반영하기
	void cmtCountupdate();
	void fileCountupdate();

	
}
