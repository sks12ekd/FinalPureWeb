package kh.spring.service;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import kh.spring.dao.AdminDAO;
import kh.spring.dao.BoardDAO;
import kh.spring.dto.BoardDTO;
import kh.spring.dto.BoardDTO_NEX;
import kh.spring.dto.BuildDTO;
import kh.spring.dto.ClassroomDTO;
import kh.spring.dto.ColScheduleDTO;
import kh.spring.dto.CollegeDTO;
import kh.spring.dto.DepartmentDTO;
import kh.spring.dto.FilesDTO;
import kh.spring.dto.LectureDTO;
import kh.spring.dto.NoticeDTO;
import kh.spring.dto.NoticeDTO_NEX;
import kh.spring.dto.ProfessorDTO;
import kh.spring.dto.ProfessorDTO_NEX;
import kh.spring.dto.StudentDTO;
import kh.spring.dto.StudentDTO_NEX;
import kh.spring.utils.ConvertDate;

@Service
public class AdminService {
	
	@Autowired
	private AdminDAO admdao;
	
	@Autowired
	private BoardService bService;
	
	@Autowired
	private BoardDAO bdao;
	
	@Autowired
	private WebApplicationContext appContext;
	
	
	// 공지 목록 가져오기
	public List<NoticeDTO> getBoardNotice(String category) throws Exception {
		return admdao.getBoardNotice(category);
	}
	
	// 공지사항 검색
	public List<NoticeDTO> searchNotice(String target, String keyword, String category) throws Exception {
		return admdao.searchNotice(target,keyword,category);
	}
	
	// 공지사항 삭제
	public int deleteNotice(List<NoticeDTO_NEX> list) throws Exception {
		List<NoticeDTO> list2 = new ArrayList<>();
		for(int i=0;i<list.size();i++) {
			NoticeDTO dto = new NoticeDTO();
			dto.setChk(list.get(i).getChk());
			dto.setNoti_seq(list.get(i).getNoti_seq());
			dto.setNoti_title(list.get(i).getNoti_title());
			dto.setNoti_contents(list.get(i).getNoti_contents());
			dto.setNoti_writeDate(ConvertDate.stringToDate(list.get(i).getNoti_writeDate()));
			dto.setCategory(list.get(i).getCategory());
			list2.add(dto);
		}
		return admdao.deleteNotice(list2);
	}
	
	// 공지사항 가져오기
	public NoticeDTO getNotice(int noti_seq) {
		return admdao.getNotice(noti_seq);
	}
	
	// 공지사항 작성
	public int writeNotice(NoticeDTO dto) {		
		return admdao.writeNotice(dto);
	}
	
	// 공지사항 수정
	public int modifyNotice(NoticeDTO_NEX dto) throws Exception{
		NoticeDTO dto2 = new NoticeDTO();
		dto2.setNoti_seq(dto.getNoti_seq());
		dto2.setCategory(dto.getCategory());
		dto2.setNoti_title(dto.getNoti_title());
		dto2.setNoti_contents(dto.getNoti_contents());
		dto2.setNoti_writeDate(ConvertDate.stringToDate(dto.getNoti_writeDate()));
		return admdao.modifyNotice(dto2);
	}
	
	// 게시판 온로드
	public List<BoardDTO> getBoard(String boardType) throws Exception {
		return admdao.getBoard(boardType);
	}
	
	// 게시판 검색
	public List<BoardDTO> searchBoard(String target, String keyword, String boardType) throws Exception {
		return admdao.searchBoard(target, keyword, boardType);
	}
	
	// 게시글 작성
	public int writePost(BoardDTO dto) {
		int seq =  admdao.writePost(dto);
		return seq;
	}
	
	// 게시글 보기
	public BoardDTO getPost(int seq) {
		return admdao.getPost(seq);
	}
	
	// 게시글 첨부파일
	public List<FilesDTO> getFiles(int parent_code){
		return admdao.getFiles(parent_code);
	}
	
	// 게시글 수정
	public int modifyPost(BoardDTO_NEX dto) throws Exception {
		BoardDTO dto2 = new BoardDTO();
		dto2.setSeq(dto.getSeq());
		dto2.setTitle(dto.getTitle());
		dto2.setContents(dto.getContents());
		System.out.println("division_code 변환전:"+dto.getBoardType());
		dto2.setBoardType(dto.getBoardType());
		String div2 = dto2.getBoardType();
		System.out.println("division_code 변환후:"+div2);
		if(dto.getWriteDate()==null) {
			dto.setWriteDate(null);
		}else {
			dto2.setWriteDate(ConvertDate.stringToDate(dto.getWriteDate()));
		}
		return admdao.modifyPost(dto2);
	}
	
	
	
	
//	public int updateBoard(BoardDTO_NEX dto) {
//		return admdao.updateBoard(dto);
//	}

	//파일 개별삭제
	public List<FilesDTO> delSpecFile(String name, int fileseq,int parent_code) {
		List<FilesDTO> list = new ArrayList<>();
		deleteFileInBoard(name,fileseq);
		return bdao.getFiles(parent_code);
	}
	//파일삭제
	private boolean deleteFileInBoard(String name, int files_seq) {
		ServletContext sc = appContext.getServletContext();
		String realPath = sc.getRealPath("/");
		String filePath = realPath + "resources/files/board/";
	    File deleteFile = new File(filePath+name); 
        // 파일이 존재하는지 체크 존재할경우 true, 존재하지않을경우 false
        if(deleteFile.exists()) {
            deleteFile.delete();     
            System.out.println("파일을 삭제하였습니다.");
            bService.delSpecFile(files_seq);
            return true;
        } else {
            System.out.println("파일이 존재하지 않습니다.");
            return false;
        }
		
	}
	// 게시판 삭제
	public int deleteBoard(List<BoardDTO_NEX> list, String boardType) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("list", list);
		map.put("boardType", boardType);
		int result =  admdao.deleteBoard(map);
		if(result >0) {
			for(int i=0; i<list.size(); i++) {
				int f_seq = list.get(i).getSeq();
				List<FilesDTO> fList = bService.getFiles(f_seq);
				if(!fList.isEmpty()) {
					for(FilesDTO m : fList) {
						System.out.println(m.getSavedName());
						deleteFileInBoard(m.getSavedName(), m.getSeq());				
					}
				}
			}
		}
		return result;

	}
	// 단과대 목록
	public List<CollegeDTO> getCollege() throws Exception {
		return admdao.getCollege();
	}
	
	// 학과 목록
	public List<DepartmentDTO> getDepartment() throws Exception {
		return admdao.getDepartment();
	}
	
	// 교수 목록 가져오기
	public List<ProfessorDTO> getProfessor() throws Exception {
		return admdao.getProfessor();
	}
	
	// 교수 명단 업데이트
	public int updateProfessor1(List<ProfessorDTO_NEX> list) throws Exception {
		List<ProfessorDTO> list2 = new ArrayList<>();
		for(int i=0; i<list.size(); i++) {
			ProfessorDTO dto = new ProfessorDTO();
			dto.setId(list.get(i).getId());
			dto.setPw(list.get(i).getPw());
			dto.setName(list.get(i).getName());
			dto.setBirth(ConvertDate.utilToSql(list.get(i).getBirth()));
			dto.setGender(list.get(i).getGender());
			dto.setCountry(list.get(i).getCountry());
			dto.setInDate(ConvertDate.utilToSql(list.get(i).getInDate()));
			if(list.get(i).getOutDate()==null) {
				dto.setOutDate(null);
			}else {
				dto.setOutDate(ConvertDate.utilToSql(list.get(i).getOutDate()));
			}
			dto.setColcode(list.get(i).getColcode());
			dto.setDeptcode(list.get(i).getDeptcode());
			dto.setCol_title(list.get(i).getCol_title());
			dto.setDept_title(list.get(i).getDept_title());
			dto.setZipcode(list.get(i).getZipcode());
			dto.setAddr1(list.get(i).getAddr1());
			dto.setAddr2(list.get(i).getAddr2());
			dto.setEmail(list.get(i).getEmail());
			dto.setPhone(list.get(i).getPhone());
			dto.setBank(list.get(i).getBank());
			dto.setAccountnum(list.get(i).getAccountnum());
			dto.setPro_office(list.get(i).getPro_office());
			dto.setPro_status(list.get(i).getPro_status());
			list2.add(dto);
		}
		return admdao.updateProfessor1(list2);
	}
	public int updateProfessor2(List<ProfessorDTO_NEX> list) throws Exception {
		List<ProfessorDTO> list2 = new ArrayList<>();
		for(int i=0; i<list.size(); i++) {
			ProfessorDTO dto = new ProfessorDTO();
			dto.setId(list.get(i).getId());
			dto.setPw(list.get(i).getPw());
			dto.setName(list.get(i).getName());
			dto.setBirth(ConvertDate.utilToSql(list.get(i).getBirth()));
			dto.setGender(list.get(i).getGender());
			dto.setCountry(list.get(i).getCountry());
			dto.setInDate(ConvertDate.utilToSql(list.get(i).getInDate()));
			if(list.get(i).getOutDate()==null) {
				dto.setOutDate(null);
			}else {
				dto.setOutDate(ConvertDate.utilToSql(list.get(i).getOutDate()));
			}
			dto.setColcode(list.get(i).getColcode());
			dto.setDeptcode(list.get(i).getDeptcode());
			dto.setCol_title(list.get(i).getCol_title());
			dto.setDept_title(list.get(i).getDept_title());
			dto.setZipcode(list.get(i).getZipcode());
			dto.setAddr1(list.get(i).getAddr1());
			dto.setAddr2(list.get(i).getAddr2());
			dto.setEmail(list.get(i).getEmail());
			dto.setPhone(list.get(i).getPhone());
			dto.setBank(list.get(i).getBank());
			dto.setAccountnum(list.get(i).getAccountnum());
			dto.setPro_office(list.get(i).getPro_office());
			dto.setPro_status(list.get(i).getPro_status());
			list2.add(dto);
		}
		return admdao.updateProfessor2(list2);
	}
	
	// 학생 목록 가져오기
	public List<StudentDTO> getStudentOnLoad() throws Exception {
		return admdao.getStudentOnLoad();
	}
	
	// 학생 명단 등록
	public int modifyStudent1(List<StudentDTO_NEX> list) throws Exception {
		List<StudentDTO> list2 = new ArrayList<>();
		for(int i=0; i<list.size(); i++) {
			StudentDTO dto = new StudentDTO();
			dto.setId(list.get(i).getId());
			dto.setPw(list.get(i).getPw());
			dto.setName(list.get(i).getName());
			Date d1 = ConvertDate.stringToDate(list.get(i).getBirth());
			dto.setBirth(d1);
			dto.setGender(list.get(i).getGender());
			dto.setCountry(list.get(i).getCountry());
			Date d2 = ConvertDate.stringToDate(list.get(i).getInDate());
			dto.setInDate(d2);
			Date d3 = ConvertDate.stringToDate(list.get(i).getOutDate());
			dto.setOutDate(d3);
			dto.setColcode(list.get(i).getColcode());
			dto.setDeptcode(list.get(i).getDeptcode());
			dto.setCol_title(list.get(i).getCol_title());
			dto.setDept_title(list.get(i).getDept_title());
			dto.setZipcode(list.get(i).getZipcode());
			dto.setAddr1(list.get(i).getAddr1());
			dto.setAddr2(list.get(i).getAddr2());
			dto.setEmail(list.get(i).getEmail());
			dto.setPhone(list.get(i).getPhone());
			dto.setBank(list.get(i).getBank());
			dto.setAccountnum(list.get(i).getAccountnum());
			dto.setStd_year(list.get(i).getStd_year());
			dto.setStd_status(list.get(i).getStd_status());
			list2.add(dto);
		}
		return admdao.modifyStudent1(list2);
	}
	
	// 학생 명단 등록
	public int modifyStudent2(List<StudentDTO_NEX> list) throws Exception {
		List<StudentDTO> list2 = new ArrayList<>();
		for(int i=0; i<list.size(); i++) {
			StudentDTO dto = new StudentDTO();
			dto.setId(list.get(i).getId());
			dto.setPw(list.get(i).getPw());
			dto.setName(list.get(i).getName());
			Date d1 = ConvertDate.stringToDate(list.get(i).getBirth());
			dto.setBirth(d1);
			dto.setGender(list.get(i).getGender());
			dto.setCountry(list.get(i).getCountry());
			Date d2 = ConvertDate.stringToDate(list.get(i).getInDate());
			dto.setInDate(d2);
			Date d3 = ConvertDate.stringToDate(list.get(i).getOutDate());
			dto.setOutDate(d3);
			dto.setColcode(list.get(i).getColcode());
			dto.setDeptcode(list.get(i).getDeptcode());
			dto.setCol_title(list.get(i).getCol_title());
			dto.setDept_title(list.get(i).getDept_title());
			dto.setZipcode(list.get(i).getZipcode());
			dto.setAddr1(list.get(i).getAddr1());
			dto.setAddr2(list.get(i).getAddr2());
			dto.setEmail(list.get(i).getEmail());
			dto.setPhone(list.get(i).getPhone());
			dto.setBank(list.get(i).getBank());
			dto.setAccountnum(list.get(i).getAccountnum());
			dto.setStd_year(list.get(i).getStd_year());
			dto.setStd_status(list.get(i).getStd_status());
			list2.add(dto);
		}
		return admdao.modifyStudent2(list2);
	}
	
	// 강의계획서 가져오기
	public List<LectureDTO> getSyllabus() {
		return admdao.getSyllabus();
	}
	
	// 강의계획서 승인
	public int syllabusApproved(LectureDTO dto) {
		return admdao.syllabusApproved(dto);
	}
	
	// 강의계획서 반려
	public int syllabusRejected(LectureDTO dto) {
		return admdao.syllabusRejected(dto);
	}
	
	// 건물 정보
	public List<BuildDTO> getBuild(){
		return admdao.getBuild();
	}
	
	// 강의실 정보
	public List<ClassroomDTO> getClassroom(){
		return admdao.getClassroom();
	}
	
	// 강의실 정보 조회
	public List<LectureDTO> searchClsTimetable(String classroom, String year, int semester){
		return admdao.searchTimetable(classroom, year, semester);
	}
	
	// 학사일정 조회
	public List<ColScheduleDTO> getColSchedule(){
		return admdao.getColSchedule();
	}
	
	// 학사일정 추가
	public int addColSchedule (ColScheduleDTO dto) {
		return admdao.addColSchedule(dto);
	}
	
	// 학사일정 수정
	public int updateColSchedule (ColScheduleDTO dto) {
		return admdao.updateColSchedule(dto);
	}
	
	// 학사일정 삭제
	public int delColSchedule (int seq) {
		return admdao.delColSchedule(seq);
	}
}