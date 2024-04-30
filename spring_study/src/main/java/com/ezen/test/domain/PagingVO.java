package com.ezen.test.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Setter
@Getter
public class PagingVO {

	//select * from board limit 0,10;
	//필요한 데이터 => 0, 10(시작,개수)
	//페이징 => pageNo / qty
	//검색 => type / keyword
	
	private int pageNo;
	private int qty; //한 화면에 보여줄 게시글 수(10)
	
	private String type;
	private String keyword;
	
	public PagingVO() {
		this.pageNo = 1;
		this.qty = 10;
	}
	
	public int getPageStart() {
		//DB상에서 limit의 시작번지를 구하는 메서드
		//1=>0 2=>10 3=>20
		return (this.pageNo-1)*this.qty;
	}
	
	//여러가지의 타입을 검색하기 위해 타입을 배열로 구분
	//type.split("") => twc => t, w, c
	public String[] getTypeToArray() {
		return this.type == null ? new String[] {} : this.type.split("");
	}
}