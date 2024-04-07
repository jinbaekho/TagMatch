from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options as ChromeOptions
import time
from selenium.webdriver.common.keys import Keys
import csv
import datetime

dateT = datetime.datetime.now()
chrome_options = ChromeOptions()
chrome_options.add_argument("--disable-webrtc-stun-origin")
chrome_options.add_argument("--disable-webrtc-ipv6")
chrome_options.add_argument("--disable-webrtc-multiple-routes")
chrome_options.add_argument('--mute-audio')
chrome_options.add_argument('--window-position=960,0')
chrome_options.add_argument('--disable-gpu')
# chrome_options.add_argument("--start-maximized")  # 브라우저 창을 최대화하는 옵션 추가
# chrome_options.add_argument('--headless')  # 백그라운드 실행을 위한 옵션
Driv=webdriver.Chrome(options=chrome_options)

def get_hashtags(T):
    Driv.implicitly_wait(10) # 로딩이 된다면 자연스럽게 밑으로 진행, 10초기다려도 안되면 종료.
     #포함되어 있는 div를 찾고 그안에 text를 T에 대입
    hashtags = [A for A in T.split() if A.startswith('#')] # 공백으로 글을 배열로 나누고, # 으로 시작하는 값이 있다면 참이되어 해당 문자만 대입.
    return hashtags

def check_value_in_column(csv_file, column_name, target_value):
    with open(csv_file, 'r', newline='', encoding='utf-8') as file:
        reader = csv.DictReader(file)
        for row in reader:
            if row[column_name] == target_value:
                return True
    return False

def comment(com_num):
    if com_num is None:
        return '0'
    else:
        return com_num


def crawl():
    name = "%d_%02d_%02d"%(dateT.year, dateT.month, dateT.day)+'insta_random.csv'
    Driv.get('https://www.instagram.com/reels/')
    Driv.implicitly_wait(10)
    Driv.find_element(By.NAME,('username'))
    Driv.find_element(By.NAME,('password'))
    Driv.find_element(By.NAME,('password')).send_keys(Keys.ENTER)
    time.sleep(5)
    Driv.get('https://www.instagram.com/reels/')
    time.sleep(5)
    for i in range(5000):
        try:
            Driv.implicitly_wait(10)
            reels= Driv.find_element(By.XPATH,f'/html/body/div[2]/div/div/div[2]/div/div/div[1]/div[1]/div[2]/section/main/div/div[{(i*2+1)}]')
            reels.location_once_scrolled_into_view
            Driv.implicitly_wait(10)
            try:
                reels.find_element(By.CLASS_NAME,'xlej3yl,x1rg5ohu,xdl72j9,x1c4vz4f,x2lah0s,xsgj6o6').click()
            except:
                pass
            hashtags = get_hashtags(reels.find_element(By.XPATH,'./div/div[1]/div/div/div/div/div/div/div/div/div/div/div[1]/div[2]/div/div[2]').get_attribute('textContent')) #hashTags 값을 입력받을 수 있게
            objListup=[
            reels.find_element(By.XPATH,'./div/div[1]/div/div/div/div/div/div/div/div/div/div/div[1]/div[2]/div/div[1]/a/div/span').text #작성자
            ,reels.find_element(By.XPATH,'./div/div[1]/div/div/div/div/div/div/div/div/div/div/div[1]/div[2]/div/div[2]').get_attribute('textContent') #내용
            ,"null"
            ,reels.find_element(By.XPATH,'./div/div[2]/div[1]/div/div/div/span/span').text #좋아요
            ,hashtags
            ,comment(reels.find_element(By.XPATH,'./div/div[2]/div[2]/div/div/div/div/span/span').text) #댓글수
            ,Driv.current_url
            ]
            time.sleep(1)
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
        except Exception as e:
            print("에러")

if __name__ == "__main__":
    crawl()