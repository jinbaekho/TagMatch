package FinalProject.TagMatch.Service;

import FinalProject.TagMatch.DTO.TagDTO;
import FinalProject.TagMatch.Entity.Contents;
import FinalProject.TagMatch.Entity.Tag;
import FinalProject.TagMatch.Repository.ContentsRepository;
import FinalProject.TagMatch.Repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final ContentsRepository contentsRepository;


    public List<TagDTO> taginfo(String tagid) {
        List<Tag> tt = tagRepository.findAllByOrderByTagcountDesc();
        List<TagDTO> tdt = new ArrayList<TagDTO>();

        for (Tag en : tt) {
            TagDTO DTList = new TagDTO(
                    en.getTagid(),
                    en.getTagcount()
            );
            tdt.add(DTList);
        }

        return tdt;
    }

////////////////////////////////////////////////////////////////

    public String[] getRTag(String id){
        String Rtag=tagRepository.findById(id).get().getRtags();
        String[] items = Rtag.replaceAll("\\[|\\]|'|#|\\s+", "").split(",");
        return items;
    }

    public String[] makeGTagList() {
        List<Contents> allContents = contentsRepository.findAll();
        List<String> items = new ArrayList<>();
        for (Contents contents : allContents) {
            String[] gtagsArray = contents.getGtags()
                    .replaceAll("\\[|\\]|'|#|\\s", "")
                    .split(",");
            items.addAll(Arrays.asList(gtagsArray));
        }
        return items.toArray(String[]::new);
    }
    public void saveTagID(String[] tagList) {
        Map<String, Integer> countMap = new HashMap<>();
        // 배열을 순회하면서 등장 횟수 계산
        for (String str : tagList) {
            //다른 언어제거
            Pattern pattern = Pattern.compile("[^가-힣a-zA-Z0-9\\s!@#$%^&*()_+{};':\",.<>?/`~\\[\\]]");
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()){continue;}
            //모두 소문자로 변환
            str=str.toLowerCase();
            if (str.isEmpty()) {
                continue;
            }
            // 기존에 해당 문자열이 맵에 있는지 확인
            if (countMap.containsKey(str)) {
                // 있으면 기존 값에 1 추가
                countMap.put(str, countMap.get(str) + 1);
            } else {
                // 없으면 새로운 키로 추가
                countMap.put(str, 1);
            }
        }
        for (String key : countMap.keySet()) {
            try {
                if (countMap.get(key)<20){continue;}
            tagRepository.save(new Tag(key, null , countMap.get(key)));
            } catch (Exception e){System.out.printf("에러발생");}
        }
    }

    public void makeRTagList(String tagname) {
        List<Contents> allContents = contentsRepository.findAllByGtagsContains(tagname);
        List<String> items = new ArrayList<>();

        for (Contents contents : allContents) {
            String[] gtagsArray = contents.getGtags()
                    .replaceAll("\\[|\\]|'|#|\\s", "")
                    .split(",");
            items.addAll(Arrays.asList(gtagsArray));
        }
        Map<String, Integer> countMap = new HashMap<>();

        // 배열을 순회하면서 등장 횟수 계산
        for (String str : items) {
            //기준태그제외
            if (str.equals(tagname)){continue;}
            //다른 언어제거
            Pattern pattern = Pattern.compile("[^가-힣a-zA-Z0-9\\s!@#$%^&*()_+{};':\",.<>?/`~\\[\\]]");
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                continue;
            }
            //모두 소문자로 변환
            str = str.toLowerCase();
            if (str.isEmpty()) {
                continue;
            }
            // 기존에 해당 문자열이 맵에 있는지 확인
            if (countMap.containsKey(str)) {
                // 있으면 기존 값에 1 추가
                countMap.put(str, countMap.get(str) + 1);
            } else {
                // 없으면 새로운 키로 추가
                countMap.put(str, 1);
            }
        }
        Map<String, Integer> sortMap = sortMapByValues(countMap);
        String[] rTags =new String[20];
        int rTagcount=0;
        for (Map.Entry<String, Integer> entry : sortMap.entrySet()) {
            if (rTagcount < 20) {
                rTags[rTagcount]=entry.getKey();
                rTagcount++;
            } else {
                StringBuilder stringRTags = new StringBuilder();
                for (String rtag : rTags) {
                    stringRTags.append("'#").append(rtag).append("',");
                }
                tagRepository.updateRtagsByTagId(tagname,stringRTags.toString());
                break;
            }
        }
    }
    private static Map<String, Integer> sortMapByValues(Map<String, Integer> inputMap) {
        // Map의 엔트리를 리스트로 변환
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(inputMap.entrySet());

        // Comparator를 사용하여 값을 기준으로 엔트리 정렬 (내림차순)
        entryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // 정렬된 엔트리를 새로운 LinkedHashMap에 넣어 반환
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}

