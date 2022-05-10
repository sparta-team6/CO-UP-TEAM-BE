package hanghae.api.coupteambe.service;

import hanghae.api.coupteambe.domain.dto.kanban.BucketDto;
import hanghae.api.coupteambe.domain.dto.kanban.BucketInfoDto;
import hanghae.api.coupteambe.domain.dto.kanban.CardInfoDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KanbanService {

    /**
     * M2-1 버킷 생성
     */
    public void createBucket(BucketInfoDto bucketInfoDto) {

        // 1. 파라매터로 받은 버킷 객체에서 필요한 데이터를 추출한다.

        // 2. 새 버킷 객체를 생성한다.

        // 3. 새로 생성한 버킷을 Repository 를 이용하여 DB에 저장한다.

    }

    /**
     * M2-2 버킷 수정
     */
    public void modifyBucket(BucketInfoDto bucketInfoDto) {

        // 1. 파라매터로 받은 버킷 객체에서 필요한 데이터를 추출한다.

        // 2. 버킷 ID를 key 로 해당 버킷을 DB 에서 조회한다. Repository(JPA)이용

        // 3. 새 버킷 객체를 생성하고 조회한 버킷으로 초기화 한다.

        // 4. 버킷을 업데이트 시킨다.

    }

    /**
     * M2-3 버킷 삭제
     */
    public void deleteBucket(String kbbId) {

        // 파라매터로 받은 버킷 ID를 key 로 DB 에서 해당 버킷을 삭제한다.
        // Repository(JPA)이용

    }

    /**
     * M2-4 전체 버킷, 카드 조회
     */
    public List<BucketDto> getAllBucketsAndCards(String pjId) {

        // 1. 파라매터로 받은 프로젝트 ID 를 가지고 있는 모든 버킷들을 조회한다.

        // 2. 순차적으로 조회된 버킷의 버킷 ID를 가지고 있는 모든 카드들을 조회한다.

        return new ArrayList<>();
    }

    /**
     * M2-5 카드 생성
     */
    public void createCard(CardInfoDto cardInfoDto) {

        // 1. 파라매터로 받은 카드 객체에서 필요한 데이터를 추출한다.

        // 2. 새 카드 객체를 생성한다.

        // 3. 새로 생성한 카드를 Repository 를 이용하여 DB에 저장한다.

    }

    /**
     * M2-6 카드 수정
     */
    public void modifyCard(CardInfoDto cardInfoDto) {

        // 1. 파라매터로 받은 카드 객체에서 필요한 데이터를 추출한다.

        // 2. 카드 ID를 key 로 해당 카드를 DB 에서 조회한다. Repository(JPA)이용

        // 3. 새 카드 객체를 생성하고 조회한 카드로 초기화 한다.

        // 4. 카드를 업데이트 시킨다.

    }

    /**
     * M2-7 카드 삭제
     */
    public void deleteCard(String kbcId) {

        // 파라매터로 받은 카드 ID를 key 로 DB 에서 해당 카드를 삭제한다.
        // Repository(JPA)이용

    }

    /**
     * M2-8 선택카드 상세 조회
     */
    public CardInfoDto getOneCard(String kbcId) {

        // 1. 파라매터로 받은 카드 ID 를 key 로 카드 정보를 조회한다.

        // 2. 새 카드 객체를 생성하고 조회한 카드로 초기화 한다.

        // 3. 생성된 카드 객체를 반환한다.

        return new CardInfoDto();
    }

}
