package biz.board.dao;

import java.util.List;

import biz.board.vo.BoardVO;

public interface BoardDAO {
	   int addBoard(BoardVO board) throws Exception;
	   BoardVO getBoard (int boardno);
	   List<BoardVO> getBoardList();
	   int updateBoard(BoardVO board);
	   int removeBoard(int boardno);
	   List<BoardVO> searchBoard(String condition,String keyword);
}
