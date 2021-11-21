package org.springframework.samples.petclinic.board;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 * @author Jose Maria Delgado Sanchez
 */
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    public Iterable<Board> findAll() throws DataAccessException{
        Iterable<Board> res = boardRepository.findAll();
        return res;
    }

    @Transactional
    public void saveBoard(Board board) throws DataAccessException{
        boardRepository.save(board);
    }
    
    @Transactional
    public Board findBoardById(int id) throws DataAccessException{
        return boardRepository.findById(id).get();
    }

}


