from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
from selenium.common.exceptions import NoSuchElementException
import time
import csv
import pandas as pd
import os
# 드라이브 경로 본인환경에 맞게 변경해주세요
# 쇼츠 동영상 Date 부분 그날그날 날짜를 할당해주세요
# 주의사항: 스크립트 중단, 종료 후 다시 실행할시 기존 csv 데이터 초기화됩니다 스크립트 끝나는대로 csv 파일 꼭 저장해주세요

# 키워드
# my_list = ['맛집', '한식','중식','일식','양식','축구','배구','야구','스포츠','골프','뉴스','이슈','화제','꿀팁','투자',]
# my_list = ['개그', '블랙코미디','툰','아재개그','공감','10대','20대','30대','40대','50대','서울','부산','경기도','경상도','전라도','충청도','강원도']
# my_list = ['놀이공원','동물원','카페','한강','영화관','미술관']
# my_list = ['맛집', '한식','중식','일식','양식','축구','배구','야구','스포츠','골프','뉴스','이슈','화제','꿀팁','투자',]
# my_list = ['개그', '블랙코미디','툰','아재개그','공감','10대','20대','30대','40대','50대','서울','부산','경기도','경상도','전라도','충청도','강원도']
my_list = ['영화관','미술관']



# selenium 설정
# 드라이브 경로 본인환경에 맞게 변경해주세요 

driver = webdriver.Chrome('C:\\Users\\hbi\\Desktop\\3파이썬 예제\\프로잭트\\chromedriver.exe')
driver.implicitly_wait(10)

for keyword in my_list:
    print(keyword)
    # 유튜브 접속 및 검색
    driver.get('https://www.youtube.com/')
    time.sleep(7)  # 웹사이트가 완전히 로딩될 때까지 기다림
    search = driver.find_element(By.NAME, 'search_query')
    search.send_keys(keyword)
    search.send_keys(Keys.RETURN)  # 검색 결과 페이지로 이동
    time.sleep(3)

    # 필터 변경하기
    filter_button_xpath = '/html/body/ytd-app/div[1]/ytd-page-manager/ytd-search/div[1]/div/ytd-search-header-renderer/div[3]/ytd-button-renderer/yt-button-shape/button'
    WebDriverWait(driver, 12).until(EC.element_to_be_clickable((By.XPATH, filter_button_xpath))).click()
    time.sleep(3)

    second_filter_button_xpath = '/html/body/ytd-app/ytd-popup-container/tp-yt-paper-dialog/ytd-search-filter-options-dialog-renderer/div[2]/ytd-search-filter-group-renderer[1]/ytd-search-filter-renderer[2]/a'
    WebDriverWait(driver, 12).until(EC.element_to_be_clickable((By.XPATH, second_filter_button_xpath))).click()
    time.sleep(3)
        
    # 데이터프레임 생성
    # 일반 동영상 데이터를 저장할 DataFrame 생성
    data = pd.DataFrame(columns=['Writer', 'Title', 'Content', 'Date', 'Like', 'Tag', 'Comments', 'Url', 'Views'])

    # 쇼츠 동영상 데이터를 저장할 DataFrame 생성
    shorts_data = pd.DataFrame(columns=['Writer', 'Title', 'Content', 'Date', 'Like', 'Tag', 'Comments', 'Url'])

    count = 0
    duplication_url = set()

    # 무한루프
    while count < 65:

        # 동영상 리스트 가져오기
        html = driver.page_source
        soup = BeautifulSoup(html, 'html.parser')
        videos = soup.select('ytd-video-renderer')
       
        if len(videos)== 0 : 
            print(len(videos))
            driver.back()
            time.sleep(1.4)

        
        # 동영상 정보 크롤링
        for video in videos:
            try:
                title = video.select_one('a#video-title').text.replace('\n', ' ')
                url = 'https://www.youtube.com' + video.select_one('a#video-title')['href']
                
                if url in duplication_url:
                    continue

                # 동영상 종류 판별 ('watch' -> 일반 동영상, 'shorts' -> Shorts 동영상)
                # 일반 동영상 크롤링
                if 'watch' in url:
                    
                    # 일반 동영상 크롤링 로직
                    driver.get(url)
                    WebDriverWait(driver, 30).until(EC.presence_of_element_located((By.CSS_SELECTOR, 'span.view-count')))
                    html = driver.page_source
                    soup = BeautifulSoup(html, 'html.parser')
                    
                    # 조회수
                    views = [item.text.replace('\n', ' ') for item in soup.select('span.view-count')][0]
                    
                    # 좋아요 수
                    try:
                        likes = soup.select_one('button[aria-label*="이 동영상을 좋아함"]').get('aria-label')
                        likes_count = ''.join(filter(str.isdigit, likes))
                    except Exception:
                        likes_count = "N/A"

                    for _ in range(3):
                        driver.execute_script("window.scrollTo(0, document.documentElement.scrollHeight);")  # 스크롤 내리기
                        time.sleep(1.4)  # 스크롤이 완전히 내려가도록 시간 추가
                        
                    # 댓글 수
                    try:
                        comments_element = None
                        while comments_element is None:  # 댓글 수 정보가 보일 때까지 스크롤
                            driver.execute_script("window.scrollTo(0, document.documentElement.scrollHeight);")
                            time.sleep(1.4)
                            comments_element = driver.find_element(By.XPATH, '/html/body/ytd-app/div[1]/ytd-page-manager/ytd-watch-flexy/div[5]/div[1]/div/div[2]/ytd-comments/ytd-item-section-renderer/div[1]/ytd-comments-header-renderer/div[1]/h2/yt-formatted-string/span[2]')

                        if comments_element is None:  # 댓글 수 정보가 없는 경우
                            comments_count = 'N/A'
                        else:
                            comments = comments_element.text.split(' ')[-1]
                            comments_count = ''.join(filter(str.isdigit, comments))
                    except:
                        print("댓글 에러" )   
                    # 해시태그
                    hashtags = [tag.text.replace('\n', ' ') for tag in soup.select('a.yt-simple-endpoint.style-scope.yt-formatted-string') if tag.text.startswith('#')]

                    # 동영상 게시자 찾기
                    writer_element = soup.select_one('yt-formatted-string.ytd-channel-name')
                    writer = 'N/A' if writer_element is None else writer_element.text.strip()

                    # 동영상 내용 찾기
                    content_element = soup.select_one('div#description')
                    content = 'N/A' if content_element is None else content_element.text.strip()

                    # 동영상 게시일 찾기
                    
                    date=driver.find_element(By.XPATH,'/html/body/ytd-app/div[1]/ytd-page-manager/ytd-watch-flexy/div[5]/div[1]/div/div[2]/div[8]/div[1]/div[2]/ytd-video-primary-info-renderer/div/div/div[1]/div[2]/yt-formatted-string').get_attribute('textContent')

                    # 데이터프레임에 데이터 추가
                    data = data.append({
                        'Writer': writer.replace('\n', ' '),
                        'Title': title.replace('\n', ' '),
                        'Content': content.replace('\n', ' '),
                        'Date': date.replace('\n', ' '),
                        'Like': likes_count.replace('\n', ' '),
                        'Tag': [tag.replace('\n', ' ') for tag in hashtags],
                        'Comments': comments_count.replace('\n', ' '),
                        'Url': url.replace('\n', ' '),
                        'Views': views.replace('\n', ' ')
                    }, ignore_index=True)


        
                    # CSV 파일에 실시간 저장
                    data.to_csv(f'C:\\Users\\hbi\\Desktop\\3파이썬 예제\\20231215_youtubeBasic_{keyword}.csv', index=False, encoding='utf-8-sig')

                
                # Shorts 동영상일때
                    pass
                
                # 쇼츠 동영상 크롤링
                elif 'shorts' in url:
                    
                    # Shorts 동영상 크롤링 로직
                    driver.get(url)
                    
                    WebDriverWait(driver, 30).until(EC.presence_of_element_located((By.CSS_SELECTOR, 'div#info-contents')))
                    html = driver.page_source
                    soup = BeautifulSoup(html, 'html.parser')
                    
                    # ... 버튼 클릭
                    button_xpath = '/html/body/ytd-app/div[1]/ytd-page-manager/ytd-shorts/div[3]/div[2]/ytd-reel-video-renderer[1]/div[2]/ytd-reel-player-overlay-renderer/div[2]/div[6]/ytd-menu-renderer/yt-button-shape/button/yt-touch-feedback-shape/div/div[2]'
                    button = driver.find_element(By.XPATH, button_xpath)
                    button.click()
                    time.sleep(1.6)

                    # 설명 버튼 클릭
                    second_button_xpath = '/html/body/ytd-app/ytd-popup-container/tp-yt-iron-dropdown/div/ytd-menu-popup-renderer/tp-yt-paper-listbox/ytd-menu-service-item-renderer/tp-yt-paper-item'
                    second_button = driver.find_element(By.XPATH, second_button_xpath)
                    second_button.click()
                    time.sleep(1.6)
                    
                    # 동영상 게시자
                    writer_element = soup.select_one('yt-formatted-string.ytd-channel-name')
                    writer = 'N/A' if writer_element is None else writer_element.text.strip()

                    # 동영상 제목
                    title_xpath = '//yt-formatted-string[@class="style-scope ytd-reel-player-header-renderer"]'
                    title_element = driver.find_element(By.XPATH, title_xpath)
                    title = 'N/A' if title_element is None else title_element.text.strip()
                        
                    # 동영상 내용
                    content_element = soup.select_one('div#description')
                    content = 'N/A' if content_element is None else content_element.text.strip()

                    # 그날그날 날짜 할당필요
                    # 동영상 게시일

                    date_element = soup.select_one('div#date')
                    date = '2023. 12. 15' if date_element is None else date_element.text.strip()

                    # 좋아요 수 크롤링
                    like_xpath = '/html/body/ytd-app/div[1]/ytd-page-manager/ytd-shorts/div[3]/div[2]/ytd-reel-video-renderer[1]/div[1]/div[2]/ytd-engagement-panel-section-list-renderer[2]/div[2]/ytd-structured-description-content-renderer/div/ytd-video-description-header-renderer/div[2]/ytd-factoid-renderer[1]/div'
                    try:
                        like = driver.find_element(By.XPATH, like_xpath).text
                        like = like.replace('좋아요 수', '').strip()
                    except NoSuchElementException:
                        like = 'N/A'

                    # 해시태그
                    tag_elements = soup.select('a.yt-simple-endpoint')
                    tag = 'N/A' if not tag_elements else [tag_element.text.strip() for tag_element in tag_elements if tag_element.text.startswith('#')]

                    # 댓글 수

                    comments_xpath = '/html/body/ytd-app/div[1]/ytd-page-manager/ytd-shorts/div[3]/div[2]/ytd-reel-video-renderer[1]/div[2]/ytd-reel-player-overlay-renderer/div[2]/div[4]/ytd-button-renderer/yt-button-shape/label/div/span'
                    
                    comments_element = driver.find_element(By.XPATH, comments_xpath)
                    if comments_element is None:  # 댓글 수 정보가 없는 경우
                        comments_count = 'N/A'
                    else:
                        comments = comments_element.text.split(' ')[-1]
                        comments_count = ''.join(filter(str.isdigit, comments))

                    
                    # 데이터프레임에 데이터 추가
                    shorts_data = shorts_data.append({
                        'Writer': writer.replace('\n', ' '),
                        'Title': title.replace('\n', ' '),
                        'Content': content.replace('\n', ' '),
                        'Date': date.replace('\n', ' '),
                        'Like': like.replace('\n', ' '),
                        'Tag': [t.replace('\n', ' ') for t in tag],
                        'Comments': comments_count.replace('\n', ' '),
                        'Url': url.replace('\n', ' ')
                    }, ignore_index=True)

       
                    # CSV 파일에 실시간 저장
                    shorts_data.to_csv(f'C:\\Users\\hbi\\Desktop\\3파이썬 예제\\20231215_shorts_{keyword}.csv', index=False, encoding='utf-8-sig')
                    
                    pass
                    
                count += 1
                print(f'크롤링 성공: {count}')
                if count==65 :
                    print("카운트  . 종료 ")
                    break

                duplication_url.add(url)
                driver.back()
                time.sleep(3.5)
            except:
                print("에러")
            
        
        driver.execute_script("window.scrollTo(0, document.documentElement.scrollHeight);")  # 스크롤 다운
        time.sleep(3.5)  # 로딩 대기