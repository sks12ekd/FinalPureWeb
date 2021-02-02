package kh.spring.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kh.spring.dto.BoardDTO;
import kh.spring.dto.NoticeDTO;
import kh.spring.dto.ProfessorDTO;
import kh.spring.dto.StudentDTO;

@Repository
public class AdminDAO {
	
	@Autowired
	private SqlSession db;
	
	// 공지사항 가져오기
	public List<NoticeDTO> getNotice(String category) throws Exception {
		return db.selectList("Admin.getNotice",category);
	}
	
	// 공지사항 검색
	public List<NoticeDTO> searchNotice(String target, String keyword, String category) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("search_target", target);
		map.put("search_keyword", keyword);
		map.put("category", category);
		return db.selectList("Admin.searchNotice",map);
	}
	
	// 공지사항 식제
	public int deleteNotice(List<NoticeDTO> list) throws Exception {
		return db.delete("deleteNotice",list);
	}
	
	// 게시판 온로드
	public List<BoardDTO> getBoard(String bdDiv) throws Exception {
		return db.selectList("Admin.getBoard", bdDiv);
	}
	
	// 게시판 검색
	public List<BoardDTO> searchBoard(String target, String keyword, String bdDiv) throws Exception {
		Map<String, String> map = new HashMap<>();
		if(target.contentEquals("title")) {target = "b.title";}
		map.put("search_target", target);
		map.put("search_keyword", keyword);
		map.put("bdDiv", bdDiv);
		System.out.println(map.get("search_target")+" / "+map.get("search_keyword")+" / "+map.get("bdDiv"));
		return db.selectList("Admin.searchBoard", map);
	}
	
	// 게시판 삭제
	public int deleteBoard(List<BoardDTO> list) throws Exception {
		return db.delete("Admin.deleteBoard", list);
	}
	
	// 교수 목록 가져오기
	public List<ProfessorDTO> getProfessor() throws Exception {
		return db.selectList("Admin.getProfessor");
	}
	
	// 교수 정보 변경
	public int updateProfessor1(List<ProfessorDTO> list) throws Exception {
		return db.update("Admin.updateProfessor1", list);
	}
	public int updateProfessor2(List<ProfessorDTO> list) throws Exception {
		return db.update("Admin.updateProfessor2", list);
	}
	
	// 학생 목록 가져오기
	public List<StudentDTO> getStudentOnLoad() throws Exception {
		return db.selectList("Admin.getStudent");
	}
	
	// 학생 정보 변경
	public int modifyStudent1(List<StudentDTO> list) throws Exception {
		return db.update("Admin.modifyStudent1", list);
	}
	public int modifyStudent2(List<StudentDTO> list) throws Exception {
		return db.update("Admin.modifyStudent2", list);
	}

}
