from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options as ChromeOptions
from selenium.webdriver.common.keys import Keys

import datetime
import time
import csv

a = datetime.datetime.now()
# keyword=input('검색내용 : ')
chrome_options = ChromeOptions() # 크롬 옵션 설정
chrome_options.add_argument("--disable-webrtc-stun-origin") #WebRTC(STUN/TURN 서버)가 요청을 보낼 때 원래의 웹 페이지 주소(origin)를 포함하지 않도록
chrome_options.add_argument("--disable-webrtc-ipv6") #WebRTC의 IPv6 지원을 비활성화합니다.
chrome_options.add_argument("--disable-webrtc-multiple-routes") #WebRTC가 다수의 네트워크 경로를 사용하는 것을 비활성화
chrome_options.add_argument('--mute-audio') #크롬 브라우저에서 오디오 출력을 음소거. 주로 백그라운드에서 브라우저를 실행할 때 사용
chrome_options.add_argument('--window-position=960,0')
chrome_options.add_argument('--disable-gpu')
chrome_options.add_argument('--disable-software-rasterizer')
chrome_options.add_argument('--disable-audio-output')
# chrome_options.add_argument("--start-maximized")  # 브라우저 창을 최대화하는 옵션 추가
# chrome_options.add_argument('--headless')  # 백그라운드 실행을 위한 옵션

Drive=webdriver.Chrome(options=chrome_options)

def findE(byt,addr):
    Drive.implicitly_wait(3)
    tarGet=Drive.find_elements(byt,addr)
    if len(tarGet)==1:
        return tarGet[0]
    else:
        return tarGet

def getText(findtype,findname):
    tarGet=findE(findtype,findname)
    # tarGet.is_selected
    Drive.implicitly_wait(10)
    if type(tarGet)==list:
        texts=[]
        for i in range(len(tarGet)):
            if tarGet[i].text=='':
                continue
            texts.append(tarGet[i].text)
    else:
        texts = findE(findtype,findname).text
    return texts


def check_value_in_column(csv_file, column_name, target_value):
    try:
        with open(csv_file, 'r', newline='', encoding='utf-8') as file:
            reader = csv.DictReader(file)
            for row in reader:
                if row[column_name] == target_value:
                    return True
        return False
    except FileNotFoundError:
        columns_name = ['Writer', 'Content', 'Date', 'Like', 'Tag', 'Comments', 'Url']
        with open(csv_file, 'a', encoding='utf-8-sig', newline='') as file:
            writer = csv.writer(file)
            writer.writerow(columns_name)


def get_hashtags():

    Drive.implicitly_wait(10) # 로딩이 된다면 자연스럽게 밑으로 진행, 10초기다려도 안되면 종료.
    T = Drive.find_element(By.CLASS_NAME, "css-1wdx3tj-DivContainer").text #포함되어 있는 div를 찾고 그안에 text를 T에 대입
    hashtags = [A for A in T.split() if A.startswith('#')] # 공백으로 글을 배열로 나누고, # 으로 시작하는 값이 있다면 참이되어 해당 문자만 대입.
    return hashtags
    

###### 유머에 관한 내용 크롤링하는 파일

def crawl(keyword):
        # Drive.get('https://www.tiktok.com/')
        search_keyword= 'https://www.tiktok.com/search/video?q='+keyword # 입력한 키워드를 검색url안에 넣어서 주소연결
        Drive.get(search_keyword) # url로 바로 입력되게끔

        time.sleep(11) #캡챠해제를 위한딜레이. 틀렸을 경우에 대비, 여유로운 시간으로

        findE(By.CLASS_NAME, 'css-1jxhpnd-DivContainer')[0].click() #첫 영상클릭. 0번째 값을 클릭하게 만듬

        time.sleep(5) # 영상이 로딩될때까지 잠시 대기

        Drive.implicitly_wait(10) # 로딩이 끝나지않았다면 여유시간을 더 주어서 진행.

        totalinfo=[] # 리스트 초기화
        url=0
        url2=0
        url3=0
        for i in range(1000): # 원하는 영상 갯수만큼 range값에 입력
            url3=url2 #중복값떄문에
            url2= url #중복값떄문에
            url = Drive.current_url #중복값떄문에

            if url==url2==url3: #중복값떄문에
                break

            name = "%d_%02d_%02d"%(a.year, a.month, a.day)+'tiktok_'+keyword+'.csv'

            if check_value_in_column(name,'Url',url):
                        print('중복')
                        Drive.find_element(By.TAG_NAME,'body').send_keys(Keys.DOWN) # append를 끝내면 아래키를 눌러 다음영상으로.
                        time.sleep(1)
                        continue
            try:
                hashtags = get_hashtags() #hashTags 값을 입력받을 수 있게
                objListup=[getText(By.CLASS_NAME, 'css-pocgy1-SpanEllipsis')[0]#Writer 작성자
                        ,getText(By.XPATH, '/html/body/div[1]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/div[1]/div/div[1]/div[1]/div[2]/div[1]/div/div[2]/div/span[1]')# Content 글내용
                        ,findE(By.CLASS_NAME, 'css-gg0x0w-SpanOtherInfos').find_elements(By.TAG_NAME,'span')[2].text# Date 날짜
                        ,getText(By.CLASS_NAME, 'css-w1dlre-StrongText')[0]# Like 좋아요
                        ,hashtags # Hashtag 해시태그
                        ,getText(By.CLASS_NAME, 'css-w1dlre-StrongText')[1]# Comments 댓글갯수
                        ,url]# Url 해당 영상주소
                        # ,getText(By.CLASS_NAME, 'css-pvx3oa-DivMusicText')]# Music 음악
                        # ,getText(By.CLASS_NAME, 'css-ws4x78-StrongVideoCount')]
                totalinfo.append(objListup) # objlistup 값을 다 totalinfo 리스트에 대입
                time.sleep(1) # 잠시 대기하여 데이터 못 긁어올경우 대비
                Drive.find_element(By.TAG_NAME,'body').send_keys(Keys.DOWN) # append를 끝내면 아래키를 눌러 다음영상으로.
                
                try:
                    with open(name, 'r'):
                        pass
                except FileNotFoundError:
                    columns_name = ['Writer', 'Content', 'Date', 'Like', 'Tag', 'Comments', 'Url']
                    with open(name, 'a', encoding='utf-8-sig', newline='') as file:
                        writer = csv.writer(file)
                        writer.writerow(columns_name)
                finally:
                    if check_value_in_column(name,'Url',objListup[6]):
                        print('중복',)
                        continue 
                    else :
                        with open(name, 'a', encoding='utf-8-sig', newline='') as file:
                            writer = csv.writer(file)
                            writer.writerow(objListup)
                print(i,keyword)
            except Exception as e:
                print(e,keyword) 
            
if __name__ == "__main__":
    searchlist=['미디어','영화','드라마','뮤지컬','연극','OTT','콘서트',
                '패션','뷰티','OOTD','화장','코디','헤어','레트로',
                '직업','엔터테이너','회사원','직장인','인플루언서','대학생',
                '동물','강아지','고양이','판다','호랑이','햄스터',
                '취미','요리','악기','필라테스','게임','여행',
                '장소','놀이공원','동물원','카페','한강','미술관','전시회',
                '음식','맛집','한식','중식','일식','양식',
                '스포츠','축구','배구','야구','e-스포츠','골프',
                '계발','헬스','공부','자격증','독서','외국어',
                '교육','코딩','학원','학교','국비지원','수능','모의고사',
                '관계','가족','친구','애인','직장동료','반려동물',
                '정보','뉴스','이슈','화제','꿀팁','투자',
                '유머','개그','코미디','툰','공감',
                '장소','경기도','서울','강원도','충청도','전라도','경상도','제주도']

    for idd in range(len(searchlist)):
        print(idd)
        print(searchlist[idd])
        crawl(searchlist[idd])