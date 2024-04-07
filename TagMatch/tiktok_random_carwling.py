from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options as ChromeOptions
from selenium.webdriver.common.keys import Keys
import time
import csv
import datetime

chrome_options = ChromeOptions()
chrome_options.add_argument("--disable-webrtc-stun-origin")
chrome_options.add_argument("--disable-webrtc-ipv6")
chrome_options.add_argument("--disable-webrtc-multiple-routes")
chrome_options.add_argument('--mute-audio')
chrome_options.add_argument('--window-position=960,0')
# chrome_options.add_argument('--disable-gpu')
# chrome_options.add_argument("--start-maximized")  # 브라우저 창을 최대화하는 옵션 추가
# chrome_options.add_argument('--headless')  # 백그라운드 실행을 위한 옵션

Driv=webdriver.Chrome(options=chrome_options)

def findE(byt,addr):
    Driv.implicitly_wait(3)
    tarGet=Driv.find_elements(byt,addr)
    if len(tarGet)==1:
        return tarGet[0]
    else:
        return tarGet

def getText(findtype,findname):
    tarGet=findE(findtype,findname)
    Driv.implicitly_wait(3)
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
    with open(csv_file, 'r', newline='', encoding='utf-8') as file:
        reader = csv.DictReader(file)
        for row in reader:
            if row[column_name] == target_value:
                return True
    return False

def get_hashtags():

    Driv.implicitly_wait(10) # 로딩이 된다면 자연스럽게 밑으로 진행, 10초기다려도 안되면 종료.
    T = Driv.find_element(By.CLASS_NAME, "css-1wdx3tj-DivContainer").text #포함되어 있는 div를 찾고 그안에 text를 T에 대입
    hashtags = [A for A in T.split() if A.startswith('#')] # 공백으로 글을 배열로 나누고, # 으로 시작하는 값이 있다면 참이되어 해당 문자만 대입.
    return hashtags

def crawl():
    Driv.get('https://www.tiktok.com/ko-KR')
    Driv.implicitly_wait(10)
    time.sleep(3)
    Driv.find_element(By.CLASS_NAME,'css-1anes8e-StyledIcon,e1gjoq3k5').click() #닫기
    time.sleep(5)
    Driv.find_element(By.XPATH,'/html/body/div[1]/div[2]/div[1]/div/div[2]/div/div[1]/div/nav/ul/li[1]/div/a').click() # 추천 누르기 (집모양)
    Driv.implicitly_wait(10)
    
    Driv.refresh()
    time.sleep(3)
    Driv.refresh()
    time.sleep(3)
    findE(By.CLASS_NAME, 'css-h2ocr8-DivVideoPlayerContainer,e1bh0wg713')[0].click() #첫영상클릭
    time.sleep(15) #캡챠해제를 위한딜레이
    totalinfo=[]
    for i in range(2000):
        try :
            hashtags = get_hashtags() #hashTags 값을 입력받을 수 있게
            objListup=[getText(By.CLASS_NAME, 'css-pocgy1-SpanEllipsis')[0]#Writer 작성자
                    ,getText(By.CLASS_NAME, "css-1wdx3tj-DivContainer")# Content 글내용
                    ,findE(By.CLASS_NAME, 'css-gg0x0w-SpanOtherInfos').find_elements(By.TAG_NAME,'span')[2].text# Date 날짜
                    ,getText(By.CLASS_NAME, 'css-w1dlre-StrongText')[0]# Like 좋아요
                    ,hashtags # Hashtag 해시태그
                    ,getText(By.CLASS_NAME, 'css-w1dlre-StrongText')[1]# Comments 댓글갯수
                    ,Driv.current_url]# Url 해당 영상주소
            totalinfo.append(objListup) # objlistup 값을 다 totalinfo 리스트에 대입
            time.sleep(1) # 잠시 대기하여 데이터 못 긁어올경우 대비
            Driv.find_element(By.TAG_NAME,'body').send_keys(Keys.DOWN) # append를 끝내면 아래키를 눌러 다음영상으로.
            a = datetime.datetime.now()
            name = "%d_%02d_%02d"%(a.year, a.month, a.day)+'tiktok_random.csv'
            
            
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
                    print('중복')
                    continue
                else :
                    with open(name, 'a', encoding='utf-8-sig', newline='') as file:
                        writer = csv.writer(file)
                        writer.writerow(objListup)
            print(i)
        except :
            print('error')
if __name__ == "__main__":
    crawl()