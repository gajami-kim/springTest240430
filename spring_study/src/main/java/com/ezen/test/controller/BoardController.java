package com.ezen.test.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.test.domain.BoardDTO;
import com.ezen.test.domain.BoardVO;
import com.ezen.test.domain.FileVO;
import com.ezen.test.domain.PagingVO;
import com.ezen.test.handler.FileHandler;
import com.ezen.test.handler.PagingHandler;
import com.ezen.test.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/board/*")
@Slf4j
@RequiredArgsConstructor
@Controller
public class BoardController {
	
	private final BoardService bsv;
	private final FileHandler fhd;
	
	//controller로 들어오는 경로와 jsp로 나가는 경로가 일치하면 void 처리 가능(온곳으로 가라)
	@GetMapping("/register")
	public void register() {}
	
	//@RequestParam("name")String name : 파라미터를 받을 때
	//required : 필수여부 / false -> 파라미터가 없어도 예외가 발생하지않음
	@PostMapping("/insert")
	public String insert(BoardVO bvo, @RequestParam(name="files", required = false)MultipartFile[] files) {
		log.info(">>>> bvo >>>> {}",bvo);
		//파일 핸들러 처리
		//파일을 저장처리 -> fileList를 리턴
		List<FileVO> flist = null;
		
		//파일이 있을 경우에만 핸들러 호출
		if(files[0].getSize()>0) {
			//핸들러 호출
			flist = fhd.uploadFiles(files);
			bvo.setHas_file(flist.size()); //들어가는 파일 개수를 바로 set
			log.info(">>>> flist >>>> {}",flist);
		}
		BoardDTO bdto = new BoardDTO(bvo, flist);
		int isOk = bsv.insert(bdto);
		
		//return 타입은 목적지 경로에 대한 타입(destPage가 리턴)
		return "redirect:/board/list";
	}
	
	@GetMapping("/list")
	public String list(Model m, PagingVO pgvo) { //PagingVO 파라미터가 없으면 기본생성자 혹은 null값이 뜬다(오류X)
		//Model 객체 => request.setAttribute 역할을 하는 객체
		
		//cmt_qty, has_file update 후 리스트 가져오기
		bsv.cmtCountupdate();
		bsv.fileCountupdate();
		
		List<BoardVO> list = bsv.getList(pgvo);
		int totalCount = bsv.getTotal(pgvo);
		log.info(">>>> totalCount >>>> {}",totalCount);
		PagingHandler ph = new PagingHandler(pgvo, totalCount);
		log.info(">>>> ph >>>> {}",ph);
		m.addAttribute("list", list);
		m.addAttribute("ph", ph);
		return "/board/list";
	}
	
	//detail => /board/detail => return /board/detail 
	//modify => /board/modify => return /board/modify
	//controller로 들어오는 경로와 jsp로 나가는 경로가 일치하면 void 처리 가능(온곳으로 가라)
	@GetMapping({"/detail","/modify"})
	public void detail(Model m, @RequestParam("bno")int bno) {
		log.info(">>>> bno >>>> {}",bno);
		BoardDTO bdto = bsv.getDetail(bno);
		log.info(">>>> bdto >>>> {}",bdto);
		m.addAttribute("bdto",bdto);
	}
	
	//메서드 방식이 다르면 이름이 같아도 상관없음
	@PostMapping("/modify")
	public String modify(BoardVO bvo, @RequestParam(name="files", required=false)MultipartFile[] files) {
		log.info(">>>> modify bvo >>>> {}",bvo);
		List<FileVO> flist= null;
		
		//fileHandler multipartfile[] => return flist
		if(files[0].getSize()>0)  {
			flist = fhd.uploadFiles(files);
			bvo.setHas_file(bvo.getHas_file()+flist.size()); //들어가는 파일 개수를 바로 set
		}
		BoardDTO bdto = new BoardDTO(bvo, flist);
		bsv.update(bdto);
		//내부케이스 /board/detail.jsp로 : 새로운 데이터(바뀐값)를 가지고 가야함
		//detail로 가기위해선 파라미터가 필요 => bvo에서 가져옴
		return "redirect:/board/detail?bno="+bvo.getBno();
	}
	
	@GetMapping("/remove")
	public String remove(@RequestParam("bno")int bno) {
		log.info(">>>> remove bno >>>> {}",bno);
		bsv.isDel(bno);
		return "redirect:/board/list";
	}
	
	@DeleteMapping(value="/{uuid}/{bno}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> removeFile(@PathVariable("uuid")String uuid, @PathVariable("bno")int bno) {
		log.info(">>>uuid>>>{}",uuid);
		log.info(">>>bno>>>{}",bno);
		int isOk = bsv.removeFile(uuid,bno);
		return isOk > 0 ? new ResponseEntity<String>("1", HttpStatus.OK) :
			new ResponseEntity<String>("0", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
