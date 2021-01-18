<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<body>
	<div class="container">
		<div class="row header">
			<!-- Header 부분입니다. 로고, 타이틀, 상단 메뉴 등을 넣어주세요. -->
			<div class="col-12">
				<div class="row">
					<div class="col-2 "><a href="/"><img src="/resources/img/home/Logo.jpg" class="headerLogo"></a></div>
					<div class="col-8 mainMenu">
						<div class="menuItem" href="#introduce"><a href="/main?pageGroup=intro&type=intro">학사 소개</a></div>
						<div class="menuItem" href="#intCol"><a href="/main?pageGroup=info&type=info">학사 안내</a></div>
						<div class="menuItem" href="#community"><a href="/main?pageGroup=community&type=freeBoard">커뮤니티</a></div>
						<div class="menuItem" href="#signUpInfo"><a href="#">입학 안내</a></div>
					</div>
					<div class="col-2"></div>
				</div>
				<div class="row position-relative">
					<div class="col-12 collapse p-3 position-absolute" id="introduce">
						<div class="row">
							<div class="col-2"><p>학사 소개<p></div>
							<div class="col-3 menuItemWrapper">
								<ol>
									<li><a href="/main?pageGroup=intro&type=intro">학사 소개</a></li>
									<li><a href="/main?pageGroup=intro&type=history">연혁</a></li>
									<li><a href="/main?pageGroup=intro&type=organization">조직도</a></li>
								</ol>
							</div>
						</div>
						<div class="col-7"></div>
					</div>
				</div>
				<div class="row position-relative">
					<div class="col-12 collapse p-3 position-absolute" id="intCol">
						<div class="row">
							<div class="col-2"><p>학사 안내<p></div>
							<div class="col-3 menuItemWrapper">
								<ol>
									<li><a href="/main?pageGroup=info&type=info">학사 일정</a></li>
									<li><a href="#">교과 과정</a></li>
									<li>	
										<ol class="subMenu">
											<li><a href="#">복수 전공 / 부 전공</a></li>
											<li><a href="#">교직과정</a></li>
										</ol>
									</li>
								</ol>
							</div>
							<div class="col-3 menuItemWrapper">
								<ol>
									<li>수업</li>
									<li>
										<ol class="subMenu">
											<li><a href="#">성적 예고제</a></li>
											<li><a href="#">시험 및 성적</a></li>
											<li><a href="#">계절 학기</a></li>
											<li><a href="#">사회 봉사 과목 이수</a></li>
											<li><a href="#">취업 관련 교과목 이수</a></li>
											<li><a href="#">강의 평가</a></li>
											<li><a href="#">전자 출결부</a></li>
											<li><a href="#">휴강 보강</a></li>
											<li><a href="#">공결 허가</a></li>
										</ol>
									</li>
								</ol>
							</div>
							<div class="col-2 menuItemWrapper">
								<ol>
									<li>학적</li>
									<li>
										<ol class="subMenu">
											<li><a href="#">재입학</a></li>
											<li><a href="#">휴복학</a></li>
											<li><a href="#">전공배정</a></li>
											<li><a href="#">유급 제적</a></li>
											<li><a href="#">수료자 졸업</a></li>
										</ol>
									</li>
								</ol>
							</div>
							<div class="col-2 menuItemWrapper">
								<ol>
									<li>등록</li>
									<li>
										<ol class="subMenu">
											<li><a href="#">등록급 납부</a></li>
											<li><a href="#">학자금 대출</a></li>
										</ol>
									</li>
									<li><a href="#">졸업</a></li>
									<li><a href="#">대학 ROAD-MAP</a></li>
									<li>증명서 발급</li>
									<li>
										<ol class="subMenu">
											<li><a href="#">학적 증명서 발급</a></li>
											<li><a href="#">학생증 발급</a></li>
										</ol>
									</li>
								</ol>
							</div>
						</div>
					</div>
				</div>
				<div class="row position-relative">
					<div class="col-12 collapse p-3 position-absolute" id="community">
						<div class="row">
							<div class="col-2">
								<p>커뮤니티</p>
							</div>
							<div class="col-3 menuItemWrapper">
								<ol>
									<li><a href="/main?pageGroup==community&type=freeBoard">자유 게시판</a></li>
									<li><a href="/main?pageGroup==community&type=anonymBoard">대나무숲</a></li>
									<li><a href="/main?pageGroup==community&type=reportBoard">건의 게시판</a></li>
									<li><a href="/main?pageGroup==community&type=notice">공지사항</a></li>
									<li><a href="/main?pageGroup==community&type=promoteBoard">홍보 게시판</a></li>
									<li><a href="/main?pageGroup==community&type=eventBoard">행사 게시판</a></li>
								</ol>
							</div>
						</div>	
					</div>
				</div>
			</div>
		</div>
		<script>
			$(".menuItem").mouseover(function(){
				let link = $(this).attr("href");
				console.log(link);
				$(link).addClass("show");
			})
			$(".menuItem").mouseout(function(){
				let link = $(this).attr("href");
				$(link).removeClass("show");
			})
			$(".collapse").mouseover(function(){
				$(this).addClass("show");
			})
			$(".collapse").mouseout(function(){
				$(this).removeClass("show");
			})
		</script>