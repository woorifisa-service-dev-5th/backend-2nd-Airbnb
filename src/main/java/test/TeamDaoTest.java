package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dao.TeamDao;

class TeamDaoTest {

    /** "1,3,5" 또는 "1|3|5" 같은 문자열을 ID 리스트로 변환 */
    private static List<Integer> parseIds(String csv) {
        if (csv == null || csv.trim().isEmpty()) {
			return Collections.emptyList();
		}
        return Stream.of(csv.split("[,;|\\s]+"))
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("user_id로 team_id 목록 조회 — 기대값은 입력값으로 비교(순서 무시)")
    void findTeamIdsByUserId_returnsTeamIds() {
        // 🔧 여기서 바꾸면 됨: 기본 userId와 기대 팀ID들
        int userId = Integer.parseInt(System.getProperty("userId", "2"));
        String expectedCsv = System.getProperty("expectedTeamIds", "1,3"); // 예: "2,4,7"

        List<Integer> expected = parseIds(expectedCsv);

        TeamDao dao = new TeamDao();
        List<Integer> actual = TeamDao.findTeamIdsByUserId(userId);

        // ORDER BY가 없다면 순서 보장은 안됨 → 집합 비교로 검증
        assertEquals(new HashSet<>(expected), new HashSet<>(actual),
                () -> "Expected team IDs " + expected + " but got " + actual);
    }

    @Test
    @DisplayName("없는 user_id를 조회하면 빈 리스트가 리턴된다")
    void findTeamIdsByUserId_returnsEmptyWhenNotFound() {
        int userId = Integer.parseInt(System.getProperty("missingUserId", "999999"));
        TeamDao dao = new TeamDao();
        List<Integer> actual = TeamDao.findTeamIdsByUserId(userId);
        assertTrue(actual.isEmpty(), "Expected empty list but got " + actual);
    }

    @Test
    @DisplayName("teamID를 입력하면 해당 ID를 갖는 팀의 이름을 반환한다.")
    void IdToNameTest1() {
        int teamId = 2;
        String expectedName = "TEAM2";

        TeamDao dao = new TeamDao();
        String actual = dao.idToName(teamId);

        System.out.println(expectedName);
        System.out.println(actual);

        assertNotNull(actual, "DB에 해당 teamId가 없어서 null이 반환되었습니다.");
        assertEquals(expectedName, actual, "팀 이름이 기대값과 다릅니다.");
    }

    @Test
    @DisplayName("없는 teamID를 입력하면 NULL을 반환한다.")
    void IdToNameTest2() {
        // 실행 시 변경 가능: -DidToName.missingTeamId=999999
        int missingId = Integer.parseInt(System.getProperty("idToName.missingTeamId", "2000000000"));

        TeamDao dao = new TeamDao();
        String actual = dao.idToName(missingId);

        assertNull(actual, () -> "존재하지 않는 teamId여야 합니다. 현재 teamId=" + missingId + "가 존재하는 것 같습니다.");
    }
}
