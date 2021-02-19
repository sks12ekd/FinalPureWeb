package kh.spring.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import kh.spring.dto.ColScheduleDTO;
import kh.spring.dto.InfoBoardDTO;
import kh.spring.dto.TakingClassDTO;
import kh.spring.service.InfoService;

@Controller
@RequestMapping("/info")
public class InfoController {

	@Autowired
	HttpSession session;
	@Autowired
	private InfoService iservice;

	@RequestMapping("/login")
	public String login(String id, String pw, Model model) throws ParseException {
		int result = iservice.login(id, pw);
		String name = iservice.getName(id, pw);
		String major = iservice.getMajor(id, pw);

		List<InfoBoardDTO> list_std = iservice.getRecentStd();
		List<InfoBoardDTO> list_scholar = iservice.getRecentScholar();
		List<InfoBoardDTO> list_enter = iservice.getRecentEnter();

		SimpleDateFormat format2 = new SimpleDateFormat("yy/MM/dd"); // 오늘날짜 요일 가져오기
		Date time2 = new Date();
		String day2 = format2.format(time2); // 21/02/18
		String dayArr[] = day2.split("/");
		String yearMonth = dayArr[0] + "/" + dayArr[1];
		List<ColScheduleDTO> list_colSche = iservice.getColSchedule(yearMonth);

		if (result > 0) {
			session.setAttribute("loginID", id);
			session.setAttribute("userName", name);
			session.setAttribute("userMajor", major);

			session.setAttribute("list_std", list_std);
			session.setAttribute("list_scholar", list_scholar);
			session.setAttribute("list_enter", list_enter);
			session.setAttribute("list_colSche", list_colSche);

			SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
			//
			Date time = new Date();
			String current = format.format(time);
			String[] date = current.split("/");
			String currentRegYear = date[0]; // 21(년)
			int currentRegMonth = Integer.parseInt(date[1]);

			String arrId[] = id.split("-");
			String openClassYear = "20" + currentRegYear;
			if (arrId[0].contentEquals("S")) { // 학생 종합정보페이지
				session.setAttribute("userPart", "학생");

				String classRegDate;
				String semester;
				if (currentRegMonth >= 1 && currentRegMonth <= 6) {
					// 1학기
					classRegDate = currentRegYear + "-02-22";
					SimpleDateFormat fm = new SimpleDateFormat("yy-MM-dd");
					Date tempDate = fm.parse(classRegDate);

					semester = "1";
					List<TakingClassDTO> list_takingClass = iservice.takingClass(id, tempDate);
					List<TakingClassDTO> list_classSche = iservice.classSche(id, tempDate);

					String schedule;
					String classTitle;
					String classRoom;

					List<String> timeList = new ArrayList<>();
					for (int j = 0; j < list_classSche.size(); j++) {
						schedule = list_classSche.get(j).getLec_schedule();
						classTitle = list_classSche.get(j).getLec_title();
						classRoom = list_classSche.get(j).getLec_classroom();

						SimpleDateFormat format1 = new SimpleDateFormat("E"); // 오늘날짜 요일 가져오기
						Date time1 = new Date();
						String day = format1.format(time1);

						if (schedule.contains("/")) { // 월(1,4)/수(4,7) 수(1,2)/목(3)
							String[] divSche_temp = schedule.split("/");// 월(1,4) 수(4,7)

							for (int i = 0; i < divSche_temp.length; i++) {
								String divSche[] = divSche_temp[i].split("\\("); // 월(1,4)
								// System.out.println(divSche[0]); // 월 *****
								// System.out.println(divSche[1]); // 1,4)
								divSche[1] = divSche[1].replaceAll("\\)", " "); // )지우기 => 1,4 *****

								if (divSche[0].contentEquals("월")) {
									divSche[0] = "가*" + divSche[0];
								}
								if (divSche[0].contentEquals("화")) {
									divSche[0] = "나*" + divSche[0];
								}
								if (divSche[0].contentEquals("수")) {
									divSche[0] = "다*" + divSche[0];
								}
								if (divSche[0].contentEquals("목")) {
									divSche[0] = "라*" + divSche[0];
								}
								if (divSche[0].contentEquals("금")) {
									divSche[0] = "마*" + divSche[0];
								}

								if (divSche[1].contains(",")) { // divSche[1]==1,4
									String divTime[] = divSche[1].split(","); // 1 4

									for (int j1 = 0; j1 < divTime.length; j1++) {
										divTime[j1] = divTime[j1].replaceAll(" ", "");
										int clTime = Integer.parseInt(divTime[j1]);
										String clDay = divSche[0];
										String todayClassInfo = clDay + "/" + divTime[j1] + "/" + classTitle + "/"
												+ classRoom;
										timeList.add(todayClassInfo);
									}

								} else if (!divSche[1].contains(",")) {
									divSche[1] = divSche[1].replaceAll(" ", "");
									int clTime = Integer.parseInt(divSche[1]);
									String clDay = divSche[0];

									String todayClassInfo = clDay + "/" + divSche[1] + "/" + classTitle + "/"
											+ classRoom;
									timeList.add(todayClassInfo);
								}
							}

						} else if (!schedule.contains("/")) { // 금(6,7) 금(8)
							String divSche[] = schedule.split("\\("); // 금 6,7)
							// System.out.println(divSche[0]); // 금
							// System.out.println(divSche[1]); // 6,7)
							divSche[1] = divSche[1].replaceAll("\\)", " "); // )지우기
							// System.out.println(a[1]); //6,7
							if (divSche[0].contentEquals("월")) {
								divSche[0] = "가*" + divSche[0];
							}
							if (divSche[0].contentEquals("화")) {
								divSche[0] = "나*" + divSche[0];
							}
							if (divSche[0].contentEquals("수")) {
								divSche[0] = "다*" + divSche[0];
							}
							if (divSche[0].contentEquals("목")) {
								divSche[0] = "라*" + divSche[0];
							}
							if (divSche[0].contentEquals("금")) {
								divSche[0] = "마*" + divSche[0];
							}

							if (divSche[1].contains(",")) {
								String divTime[] = divSche[1].split(",");
								for (int j1 = 0; j1 < divTime.length; j1++) {
									divTime[j1] = divTime[j1].replaceAll(" ", "");
									int clTime = Integer.parseInt(divTime[j1]);
									String clDay = divSche[0];

									String todayClassInfo = clDay + "/" + divTime[j1] + "/" + classTitle + "/"
											+ classRoom;
									timeList.add(todayClassInfo);

								}
							} else if (!divSche[1].contains(",")) { // 금(8)
								String divTime[] = schedule.split("\\("); // 금 8)
								divTime[1] = divTime[1].replaceAll("\\)", " ");
								divSche[1] = divSche[1].replaceAll(" ", "");
								int clTime = Integer.parseInt(divSche[1]);
								String clDay = divSche[0];

								String todayClassInfo = clDay + "/" + divSche[1] + "/" + classTitle + "/" + classRoom;
								timeList.add(todayClassInfo);
							}
						}
					}

					timeList.add("바*토/");
					Collections.sort(timeList);
					for (String i : timeList) {
						System.out.println(i);
					}
					model.addAttribute("timeList", timeList);
					session.setAttribute("list_takingClass", list_takingClass);
					session.setAttribute("openClassYear", openClassYear);
					session.setAttribute("semester", semester);

					// test
					model.addAttribute("msg", 1);
					return "info/info";
				} else if (currentRegMonth >= 7 && currentRegMonth <= 12) {
					// 2학기
					classRegDate = currentRegYear + "-08-22";
					semester = "2";
					SimpleDateFormat fm = new SimpleDateFormat("yy-MM-dd");
					Date tempDate = fm.parse(classRegDate);

					List<TakingClassDTO> list_takingClass = iservice.takingClass(id, tempDate);
					session.setAttribute("list_takingClass", list_takingClass);
					session.setAttribute("openClassYear", openClassYear);
					session.setAttribute("semester", semester);

					// test
					model.addAttribute("msg", 2);
					return "info/info";
				}

			} else if (arrId[0].contentEquals("P")) { // 교수종합정보페이지
				session.setAttribute("userPart", "교수");
				String classOpenDate;
				String semester;
				if (currentRegMonth >= 1 && currentRegMonth <= 6) {
					// 1학기
					semester = "1";
					classOpenDate = currentRegYear + "-01-25";
					SimpleDateFormat fm = new SimpleDateFormat("yy-MM-dd");
					Date tempDate = fm.parse(classOpenDate);

					List<TakingClassDTO> list_takingClass = iservice.takingClass(id, semester, tempDate);
					List<TakingClassDTO> list_classSche = iservice.classSche(id, semester, tempDate);

					String schedule;
					String classTitle;
					String classRoom;

					List<String> timeList = new ArrayList<>();
					for (int j = 0; j < list_classSche.size(); j++) {
						schedule = list_classSche.get(j).getLec_schedule();
						classTitle = list_classSche.get(j).getLec_title();
						classRoom = list_classSche.get(j).getLec_classroom();

						SimpleDateFormat format1 = new SimpleDateFormat("E"); // 오늘날짜 요일 가져오기
						Date time1 = new Date();
						String day = format1.format(time1);

						if (schedule.contains("/")) { // 월(1,4)/수(4,7) 수(1,2)/목(3)
							String[] divSche_temp = schedule.split("/");// 월(1,4) 수(4,7)

							for (int i = 0; i < divSche_temp.length; i++) {
								String divSche[] = divSche_temp[i].split("\\("); // 월(1,4)
								// System.out.println(divSche[0]); // 월 *****
								// System.out.println(divSche[1]); // 1,4)
								divSche[1] = divSche[1].replaceAll("\\)", " "); // )지우기 => 1,4 *****
								if (divSche[0].contentEquals("월")) {
									divSche[0] = "가*" + divSche[0];
								}
								if (divSche[0].contentEquals("화")) {
									divSche[0] = "나*" + divSche[0];
								}
								if (divSche[0].contentEquals("수")) {
									divSche[0] = "다*" + divSche[0];
								}
								if (divSche[0].contentEquals("목")) {
									divSche[0] = "라*" + divSche[0];
								}
								if (divSche[0].contentEquals("금")) {
									divSche[0] = "마*" + divSche[0];
								}

								if (divSche[1].contains(",")) { // divSche[1]==1,4
									String divTime[] = divSche[1].split(","); // 1 4

									for (int j1 = 0; j1 < divTime.length; j1++) {
										divTime[j1] = divTime[j1].replaceAll(" ", "");
										String todayClassInfo = divSche[0] + "/" + divTime[j1] + "/" + classTitle + "/"
												+ classRoom;
										timeList.add(todayClassInfo);
									}

								} else if (!divSche[1].contains(",")) {
									divSche[1] = divSche[1].replaceAll(" ", "");
									String todayClassInfo = divSche[0] + "/" + divSche[1] + "/" + classTitle + "/"
											+ classRoom;
									timeList.add(todayClassInfo);
								}
							}

						} else if (!schedule.contains("/")) { // 금(6,7) 금(8)
							String divSche[] = schedule.split("\\("); // 금 6,7)
							// System.out.println(divSche[0]); // 금
							// System.out.println(divSche[1]); // 6,7)
							divSche[1] = divSche[1].replaceAll("\\)", " "); // )지우기
							// System.out.println(a[1]); //6,7
							if (divSche[0].contentEquals("월")) {
								divSche[0] = "가*" + divSche[0];
							}
							if (divSche[0].contentEquals("화")) {
								divSche[0] = "나*" + divSche[0];
							}
							if (divSche[0].contentEquals("수")) {
								divSche[0] = "다*" + divSche[0];
							}
							if (divSche[0].contentEquals("목")) {
								divSche[0] = "라*" + divSche[0];
							}
							if (divSche[0].contentEquals("금")) {
								divSche[0] = "마*" + divSche[0];
							}

							if (divSche[1].contains(",")) {
								String divTime[] = divSche[1].split(",");
								for (int j1 = 0; j1 < divTime.length; j1++) {
									divTime[j1] = divTime[j1].replaceAll(" ", "");
									String todayClassInfo = divSche[0] + "/" + divTime[j1] + "/" + classTitle + "/"
											+ classRoom;
									timeList.add(todayClassInfo);
								}
							} else if (!divSche[1].contains(",")) { // 금(8)
								String divTime[] = schedule.split("\\("); // 금 8)
								divTime[1] = divTime[1].replaceAll("\\)", " ");
								divSche[1] = divSche[1].replaceAll(" ", "");
								String todayClassInfo = divSche[0] + "/" + divSche[1] + "/" + classTitle + "/"
										+ classRoom;
								timeList.add(todayClassInfo);

							}
						}
					}
					timeList.add("바*토/");
					Collections.sort(timeList);
					for (String i : timeList) {
						System.out.println(i);
					}
					model.addAttribute("timeList", timeList);
					session.setAttribute("list_takingClass", list_takingClass);
					session.setAttribute("semester", semester);
					session.setAttribute("openClassYear", openClassYear);

					// test
					model.addAttribute("msg", 3);
					return "info/info";
				} else if (currentRegMonth >= 7 && currentRegMonth <= 12) {
					// 2학기
					classOpenDate = currentRegYear + "-08-22";
					semester = "2";
					SimpleDateFormat fm = new SimpleDateFormat("yy-MM-dd");
					Date tempDate = fm.parse(classOpenDate);

					List<TakingClassDTO> list_takingClass = iservice.takingClass(id, semester, tempDate);
					session.setAttribute("list_takingClass", list_takingClass); // 강의항목
					session.setAttribute("semester", semester); // 학기
					session.setAttribute("openClassYear", openClassYear); // 개강년도(현재년도)

					// test
					model.addAttribute("msg", 4);
					return "info/info";
				}

				return "info/info";
			} else if (arrId[0].contentEquals("A")) { // 관리자 넥사크로 페이지로 이동
				return "info/temp";
			}
		} else if (result == 0) {
			model.addAttribute("errMsg", "아이디와 비밀번호 확인");
			return "info/info";
		}
		return "info/info";
	}

	@RequestMapping("/logout")
	public String logout() {
		session.invalidate();
		return "info/info";

	}

	
}
