package com.ezen.test.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezen.test.domain.CommentVO;
import com.ezen.test.service.BoardService;
import com.ezen.test.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/comment/*")
@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {

	private final CommentService csv;
	private final BoardService bsv;
	
	//ResponseEntity 객체 사용 : body내용 + httpStatus 상태(비동기 사용할 때 사용)
	//@RequestBody : body 값을 전부 추출
	//consumes : @RequesetBody에서 가져오는 데이터의 형태
	//produces : 보내는 데이터의 형식 / 나가는 타입 => MediaType
	//json : application/json / text : text_plain
	
	//produces 텍스트는 생략가능(적는걸 권장)
	@PostMapping(value="/post", consumes="application/json", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> post(@RequestBody CommentVO cvo){
		log.info(">>> cvo >>>{}",cvo);
		int isOk = csv.post(cvo);
		//댓글수 구하기 start
		//내가한방법:update,delete따로, 쌤이한방법:update,delete동시에
		int isOk_com = bsv.updateComment(cvo.getBno());
		log.info(">>> isOk_com >>> {}",isOk_com);
		//댓글수 구하기 end
		return isOk > 0 ? new ResponseEntity<String>("1", HttpStatus.OK) :
			new ResponseEntity<String>("0",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//value="/{변수이름}"
	@GetMapping(value="/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CommentVO>> list(@PathVariable("bno")int bno){
		log.info(">>> bno >>> {}", bno);
		List<CommentVO> list = csv.getList(bno);
		return new ResponseEntity<List<CommentVO>>(list, HttpStatus.OK);
	}
	
	@PutMapping(value="/modify", consumes = "application/json", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> modify(@RequestBody CommentVO cvo){
		log.info(">>> cvo >>> {}", cvo);
		int isOk = csv.update(cvo);
		return isOk > 0 ? new ResponseEntity<String>("1", HttpStatus.OK) : 
			new ResponseEntity<String>("0", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@DeleteMapping(value="/{cno}/{bno}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> remove(@PathVariable("cno")int cno, @PathVariable("bno")int bno) {
		log.info(">>> cno >>> {}", cno);
		int isOk = csv.remove(cno);
		//int isOk = csv.remove(cno, bno)로 보내고
		//CommnetServiceImpl에서 boardDAO 임포트, bno는 boardMapper로 보내는 방법도 있음
//		//댓글삭제하기 start
//		log.info(">>> bno >>> {}", bno);
//		int isOk_com = bsv.deleteComment(bno);
//		log.info(">>> isOk_com >>> {}",isOk_com);
//		//댓글삭제하기 end
		return isOk > 0 ? new ResponseEntity<String>("1", HttpStatus.OK) : 
			new ResponseEntity<String>("0", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
}
