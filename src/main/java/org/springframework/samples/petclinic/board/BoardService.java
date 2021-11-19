package org.springframework.samples.petclinic.board;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.boardcard.BoardCard;
import org.springframework.stereotype.Service;

/**
 * @author Jose Maria Delgado Sanchez
 */
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    public Iterable<Board> findAll(){
        Iterable<Board> res = boardRepository.findAll();
        return res;
    }

    @Transactional
    public void saveBoard(Board board){
        boardRepository.save(board);
    }
    
    @Transactional
    public Board findBoardById(int id) throws DataAccessException{
        return boardRepository.findById(id).get();
    }

    @Transactional
    public List<BoardCard> findCardList(int boardId) throws DataAccessException{
        return boardRepository.findById(boardId).get().getBoardCard();
    }
}
