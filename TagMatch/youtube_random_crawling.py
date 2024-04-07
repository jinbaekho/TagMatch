from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options as ChromeOptions
import time
import csv

chrome_options = ChromeOptions()
chrome_options.add_argument("--disable-webrtc-stun-origin")
chrome_options.add_argument("--disable-webrtc-ipv6")
chrome_options.add_argument("--disable-webrtc-multiple-routes")
chrome_options.add_argument('--mute-audio')
chrome_options.add_argument('--window-position=960,0')
chrome_options.add_argument('--disable-gpu')

driver = webdriver.Chrome("C:\\Users\\hbi\\Desktop\\3파이썬 예제\\프로잭트\\chromedriver.exe")

def findE(_by,_add):
    return driver.find_element(_by,_add)

def crawl():
    try:
        with open('20231227_youtubeShortsRandom.csv', 'r'):
            pass
    except FileNotFoundError:
        columns_name = ['Writer', 'Content', 'Date', 'Like', 'Tag', 'Comments', 'Url']
        with open('20231227_youtubeShortsRandom.csv', 'a', encoding='utf-8-sig', newline='') as file:
            writer = csv.writer(file)
            writer.writerow(columns_name)

    driver.get('https://www.youtube.com/shorts')

    for i in range(700):
        driver.implicitly_wait(10)
        actShort=findE(By.ID,i)

        time.sleep(1)

        driver.implicitly_wait(10) 
        actShort.location_once_scrolled_into_view

        time.sleep(1)

        driver.implicitly_wait(10)

        try:
            try:
                hashTag=actShort.find_element(By.XPATH,'.//*[@id="description"]/yt-formatted-string').get_attribute('textContent') #태그
                hashTag=[A for A in hashTag.split() if A.startswith('#')]
            except:
                hashTag = ['N/A']

            listUp=[actShort.find_element(By.XPATH,'.//*[@id="text"]/a').text #작성자
                ,actShort.find_element(By.XPATH,'.//*[@id="overlay"]/ytd-reel-player-header-renderer/h2/yt-formatted-string').text #내용
                ,actShort.find_element(By.XPATH,'.//*[@id="factoids"]/ytd-factoid-renderer[2]/div').get_attribute('aria-label') #날짜
                ,actShort.find_element(By.XPATH,'.//*[@id="like-button"]/yt-button-shape/label/div/span').text  #좋아요 수
                ,hashTag
                ,actShort.find_element(By.XPATH,'.//*[@id="comments-button"]/ytd-button-renderer/yt-button-shape/label/div/span').text  #댓글 수
                ,driver.current_url]# Url

            with open('20231227_youtubeShortsRandom.csv', 'a', encoding='utf-8-sig', newline='') as file:
                writer = csv.writer(file)
                writer.writerow(listUp)
            print(i,':',listUp)
        except Exception as e:
            print("에러")

if __name__ == "__main__":
    crawl()
