package FinalProject.TagMatch.Service;

import FinalProject.TagMatch.DTO.ContentsDTO;
import FinalProject.TagMatch.Entity.Contents;
import FinalProject.TagMatch.Repository.ContentsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ContentsService {
    private final ContentsRepository contentsRepository;

    public String[] getPlatformRank(int MN, List<String> platformid) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(MN);
        List<Contents> rank = contentsRepository.findByCrawlingdateBetween(startDate, endDate);
        List<String> items = new ArrayList<>();

        for (Contents contents : rank) {
            if (platformid.contains(contents.getPlatformid())) {
                String[] gtagsArray = contents.getGtags()
                        .replaceAll("\\[|\\]|'|#|\\s", "")
                        .split(",");
                items.addAll(Arrays.asList(gtagsArray));
            }
        }
        return items.toArray(String[]::new);
    }

    public Map<String, Integer> gTagCount(String[] tagList) {
        Map<String, Integer> countMap = new HashMap<>();

        // 배열을 순회하면서 등장 횟수 계산
        for (String str : tagList) {
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
        return countMap;
    }

    public <K, V extends Comparable<? super V>> Map<K, V> sortByValueDesc(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.<K, V>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public List<ContentsDTO> getLikeRank(String tagName) {

        List<Contents> contentsList = contentsRepository.findAllByGtagsContains("\'#" + tagName + "\'", Sort.by(Sort.Direction.DESC, "likes"));
        List<ContentsDTO> items = new ArrayList<>();
        for (Contents contents : contentsList) {
            ContentsDTO DTList = new ContentsDTO(
                    contents.getUrl(),
                    contents.getLikes(),
                    contents.getContent(),
                    contents.getPlatformid()
            );
            items.add(DTList);
        }
        return items;
    }

    public List<ContentsDTO> topThreeMaking(List<ContentsDTO> contentsList, String platformid) {
        List<ContentsDTO> items = new ArrayList<>();
        int count = 0;
        for (ContentsDTO contentsDTO : contentsList) {
            if (contentsDTO.getPlatformid().equals(platformid)) {
                ContentsDTO DTList = new ContentsDTO(
                        contentsDTO.getUrl(),
                        contentsDTO.getLikes(),
                        contentsDTO.getContent(),
                        contentsDTO.getPlatformid());
                items.add(DTList);
                if (items.size() >= 3) {
                    return items;
                }
            }
        }
        return items;
    }

    public String[] categoryTags(int categoryid) {
        List<Contents> ent = contentsRepository.findByCategoryid(categoryid);
        List<ContentsDTO> dtolist = new ArrayList<>();

        for (Contents contents : ent) {

                ContentsDTO DTList = new ContentsDTO(
                        contents.getGtags(),
                        contents.getCategoryid());
                dtolist.add(DTList);

        }
        List<String> items = new ArrayList<>();
        for (ContentsDTO contents : dtolist) {
            if (contents.getCategoryid() == categoryid) {
                String[] gtagsArray = contents.getGtags()
                        .replaceAll("\\[|\\]|'|#|\\s", "")
                        .split(",");
                items.addAll(Arrays.asList(gtagsArray));
            }
        }
        return items.toArray(String[]::new);

    }
}
