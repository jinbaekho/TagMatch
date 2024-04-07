from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import re
from selenium.webdriver.common.by import By
from bs4 import BeautifulSoup
import pandas as pd
import os

import time

class InstagramCrawler:
    def __init__(self):
        self.tagList = ["맛집", "가족", "축제", "여친", "남친", "운동", "헬스", "ootd", "카페", "데일리룩",
       "출근룩", "강아지", "고양이", "데일리", "챌린지", "여행", "MT", "유머", "MBTI", "사진",
       "맞팔", "짤", "연말", "크리스마스", "애니메이션", "애니", "꿀팁", "베이커리", "T1", "페이커",
       "야식", "메이크업", "헤어", "존예", "존잘"]
        
    def insta_searching(self, word):
        url = "https://www.instagram.com/explore/tags/" + str(word)
        return url

    def select_first(self, driver):
        first = driver.find_element(By.CSS_SELECTOR, '._aagw')
        first.click()
        time.sleep(3)

    def get_content(self, driver):
        html = driver.page_source
        soup = BeautifulSoup(html, 'html.parser')

        writer = soup.select('span.xt0psk2')[0].text

        try:
            content = soup.select('div._a9zs')[0].text
        except:
            content = ''

        date = soup.select('time._aaqe')[0]['datetime'][:10]

        tags = re.findall(r'#[^\s#,\\]+', content)

        try:
            like = soup.select('span.x193iq5w.xeuugli.x1fj9vlw.x13faqbe.x1vvkbs.xt0psk2.x1i0vuye.xvs91rp.x1s688f.x5n08af.x10wh9bi.x1wdrske.x8viiok.x18hxmgj')[0].findAll('span')[-1].text
        except:
            like = 0

        try:
            place = soup.select('div._aaqm')[0].text
        except:
            place = ''

        try:
            while True:
                try:
                    button = driver.find_element(By.CSS_SELECTOR, 'body > div.x1n2onr6.xzkaem6 > div.x9f619.x1n2onr6.x1ja2u2z > div > div.x1uvtmcs.x4k7w5x.x1h91t0o.x1beo9mf.xaigb6o.x12ejxvf.x3igimt.xarpa2k.xedcshv.x1lytzrv.x1t2pt76.x7ja8zs.x1n2onr6.x1qrby5j.x1jfb8zj > div > div > div > div > div.xb88tzc.xw2csxc.x1odjw0f.x5fp0pe.x1qjc9v5.xjbqb8w.x1lcm9me.x1yr5g0i.xrt01vj.x10y3i5r.xr1yuqi.xkrivgy.x4ii5y1.x1gryazu.x15h9jz8.x47corl.xh8yej3.xir0mxb.x1juhsu6 > div > article > div > div._ae65 > div > div > div._ae2s._ae3v._ae3w > div._ae5q._akdn._ae5r._ae5s > ul > div:nth-child(3) > div > div > li > div > button')
                    print('더보기 버튼 찾기 성공')
                except:
                    break

                if button is not None:
                    try:
                        driver.find_element(By.CSS_SELECTOR, 'body > div.x1n2onr6.xzkaem6 > div.x9f619.x1n2onr6.x1ja2u2z > div > div.x1uvtmcs.x4k7w5x.x1h91t0o.x1beo9mf.xaigb6o.x12ejxvf.x3igimt.xarpa2k.xedcshv.x1lytzrv.x1t2pt76.x7ja8zs.x1n2onr6.x1qrby5j.x1jfb8zj > div > div > div > div > div.xb88tzc.xw2csxc.x1odjw0f.x5fp0pe.x1qjc9v5.xjbqb8w.x1lcm9me.x1yr5g0i.xrt01vj.x10y3i5r.xr1yuqi.xkrivgy.x4ii5y1.x1gryazu.x15h9jz8.x47corl.xh8yej3.xir0mxb.x1juhsu6 > div > article > div > div._ae65 > div > div > div._ae2s._ae3v._ae3w > div._ae5q._akdn._ae5r._ae5s > ul > div:nth-child(3) > div > div > li > div > button').click()
                        print('더보기 누름')
                        time.sleep(1)
                    except:
                        break

            time.sleep(1)

            element = driver.find_element(By.CSS_SELECTOR, 'body > div.x1n2onr6.xzkaem6 > div.x9f619.x1n2onr6.x1ja2u2z > div > div.x1uvtmcs.x4k7w5x.x1h91t0o.x1beo9mf.xaigb6o.x12ejxvf.x3igimt.xarpa2k.xedcshv.x1lytzrv.x1t2pt76.x7ja8zs.x1n2onr6.x1qrby5j.x1jfb8zj > div > div > div > div > div.xb88tzc.xw2csxc.x1odjw0f.x5fp0pe.x1qjc9v5.xjbqb8w.x1lcm9me.x1yr5g0i.xrt01vj.x10y3i5r.xr1yuqi.xkrivgy.x4ii5y1.x1gryazu.x15h9jz8.x47corl.xh8yej3.xir0mxb.x1juhsu6 > div > article > div > div._ae65 > div > div > div._ae2s._ae3v._ae3w > div._ae5q._akdn._ae5r._ae5s > ul > div:nth-child(3) > div > div')
            child_elements_replies = element.find_elements(By.XPATH, './div')
            comment_count = len(child_elements_replies)

        except:
            comment_count = 999

        try:
            url = driver.current_url
        except:
            url = ''

        try:
            music = ''
        except:
            music = ''

        try:
            share = ''
        except:
            share = 0

        data = [writer, content, date, like, place, tags, comment_count, url, music, share]
        return data

    def move_next(self, driver):
        # right = driver.find_element(By.CSS_SELECTOR, "div._aaqg._aaqh")
        # WebDriverWait를 사용하여 요소를 기다림
        try:
            right = WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.CSS_SELECTOR, "div._aaqg._aaqh")))
            right.click()
        except:
            return -1

        time.sleep(3)


    def crawl_instagram(self):
        chrome_options = Options()
        chrome_options.add_argument("--start-maximized")

        service = Service("C:\\Users\\hbi\\Desktop\\3파이썬 예제\\프로잭트\\chromedriver.exe")
        # service = Service("C:\\Users\\Swhale\\Desktop\\3파이썬 예제\\프로잭트\\chromedriver")
        driver = webdriver.Chrome(service=service, options=chrome_options)

        driver.get("https://www.instagram.com/accounts/login/")
        time.sleep(1)

        driver.implicitly_wait(10)
        login_id = driver.find_element(By.CSS_SELECTOR, 'input[name="username"]')
        login_id
        login_pwd = driver.find_element(By.CSS_SELECTOR, 'input[name="password"]')
        login_pwd
        driver.implicitly_wait(10)
        login_id.send_keys(Keys.ENTER)

        for tag in self.tagList:
            time.sleep(3)
            print(tag)
            url = self.insta_searching(str(tag))

            driver.get(url)
            time.sleep(3)

            self.select_first(driver)

            results = []
            target = 30
            for i in range(target):
                print(i+1, "번")
                try:
                    data = self.get_content(driver)
                    results.append(data)
                    if self.move_next(driver) == -1:
                        break
                except Exception as e:
                    print("Error occurred while getting content: ", e)
                    time.sleep(2)
                    if self.move_next(driver) == -1:
                        break
                time.sleep(3)

            if results:
                print(results[:5])
                results_df = pd.DataFrame(results)
                results_df.columns = ['Writer', 'Content', 'Date', 'Like', 'place', 'Tags', 'Comments', 'Url', 'Music', 'Share']

                csv_file_name = f"_about_{tag}_insta_crawling.csv"

                if not os.path.isfile(csv_file_name):
                    results_df.to_csv(csv_file_name, index=False, encoding="utf-8-sig")
                else:
                    results_df.to_csv(csv_file_name, mode='a', header=False, index=False, encoding="utf-8-sig")
            else:
                print("No results to save into DataFrame")

            time.sleep(5)

if __name__ == "__main__":
    crawler = InstagramCrawler()
    crawler.crawl_instagram()

