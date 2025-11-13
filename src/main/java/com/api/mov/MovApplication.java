package com.api.mov;

import com.api.mov.domain.facility.entity.Facility;
import com.api.mov.domain.facility.repository.FacilityRepository;
import com.api.mov.domain.pass.entity.Pass;
import com.api.mov.domain.pass.entity.PassItem;
import com.api.mov.domain.pass.entity.Sport;
import com.api.mov.domain.pass.repository.PassItemRepository;
import com.api.mov.domain.pass.repository.PassRepository;
import com.api.mov.domain.pass.repository.SportRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;
import java.util.List;

@EnableJpaAuditing
@SpringBootApplication
public class MovApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(
            SportRepository sportRepository,
            FacilityRepository facilityRepository,
            PassRepository passRepository,
            PassItemRepository passItemRepository
    ) {
        return args -> {

            // ========================================
            // 1. Sport 마스터 데이터 (9종목)
            // ========================================
            List<Sport> sports = sportRepository.findAll();
            if (sports.isEmpty()) {
                System.out.println("Initializing Sport master data...");

                sports = Arrays.asList(
                        Sport.builder().name("웨이트 & 크로스핏").build(),  // ID: 1
                        Sport.builder().name("실내 클라이밍").build(),      // ID: 2
                        Sport.builder().name("필라테스").build(),          // ID: 3
                        Sport.builder().name("요가").build(),              // ID: 4
                        Sport.builder().name("실내 수영").build(),         // ID: 5
                        Sport.builder().name("댄스").build(),              // ID: 6
                        Sport.builder().name("테니스").build(),            // ID: 7
                        Sport.builder().name("풋살").build(),              // ID: 8
                        Sport.builder().name("골프").build()               // ID: 9
                );

                sports = sportRepository.saveAll(sports);
                System.out.println("Sport data initialization complete.");
            }

            // ========================================
            // 2. Facility 샘플 데이터 (각 종목별 3개씩 = 총 27개)
            // ========================================
            List<Facility> allFacilities = facilityRepository.findAll();

            Facility weight1, weight2, weight3;
            Facility climb1, climb2, climb3;
            Facility pilates1, pilates2, pilates3;
            Facility yoga1, yoga2, yoga3;
            Facility swim1, swim2, swim3;
            Facility dance1, dance2, dance3;
            Facility tennis1, tennis2, tennis3;
            Facility futsal1, futsal2, futsal3;
            Facility golf1, golf2, golf3;

            if (allFacilities.isEmpty()) {
                System.out.println("Initializing Sample Facility data (27 facilities)...");

                // 웨이트 & 크로스핏 (3개)
                weight1 = facilityRepository.save(Facility.builder()
                        .name("파워짐 강남점").contact("010-1111-0001")
                        .address("서울시 강남구").detailAddress("테헤란로 123")
                        .price(15000).postCode("06234").sport(sports.get(0)).build());

                weight2 = facilityRepository.save(Facility.builder()
                        .name("머슬팩토리 홍대점").contact("010-1111-0002")
                        .address("서울시 마포구").detailAddress("양화로 456")
                        .price(12000).postCode("04043").sport(sports.get(0)).build());

                weight3 = facilityRepository.save(Facility.builder()
                        .name("크로스핏박스 역삼").contact("010-1111-0003")
                        .address("서울시 강남구").detailAddress("역삼로 789")
                        .price(18000).postCode("06235").sport(sports.get(0)).build());

                // 실내 클라이밍 (3개)
                climb1 = facilityRepository.save(Facility.builder()
                        .name("더클라임 홍대").contact("010-2222-0001")
                        .address("서울시 마포구").detailAddress("와우산로 111")
                        .price(16000).postCode("04043").sport(sports.get(1)).build());

                climb2 = facilityRepository.save(Facility.builder()
                        .name("클라이밍파크 강남").contact("010-2222-0002")
                        .address("서울시 강남구").detailAddress("논현로 222")
                        .price(17000).postCode("06234").sport(sports.get(1)).build());

                climb3 = facilityRepository.save(Facility.builder()
                        .name("볼더스 신촌점").contact("010-2222-0003")
                        .address("서울시 서대문구").detailAddress("신촌로 333")
                        .price(15000).postCode("03785").sport(sports.get(1)).build());

                // 필라테스 (3개)
                pilates1 = facilityRepository.save(Facility.builder()
                        .name("필라인 스튜디오").contact("010-3333-0001")
                        .address("서울시 강남구").detailAddress("선릉로 444")
                        .price(25000).postCode("06235").sport(sports.get(2)).build());

                pilates2 = facilityRepository.save(Facility.builder()
                        .name("코어필라 송파점").contact("010-3333-0002")
                        .address("서울시 송파구").detailAddress("올림픽로 555")
                        .price(23000).postCode("05551").sport(sports.get(2)).build());

                pilates3 = facilityRepository.save(Facility.builder()
                        .name("바디라인 필라테스").contact("010-3333-0003")
                        .address("서울시 서초구").detailAddress("반포대로 666")
                        .price(24000).postCode("06592").sport(sports.get(2)).build());

                // 요가 (3개)
                yoga1 = facilityRepository.save(Facility.builder()
                        .name("젠요가 센터").contact("010-4444-0001")
                        .address("서울시 서초구").detailAddress("강남대로 777")
                        .price(18000).postCode("06592").sport(sports.get(3)).build());

                yoga2 = facilityRepository.save(Facility.builder()
                        .name("요가원 홍대점").contact("010-4444-0002")
                        .address("서울시 마포구").detailAddress("홍익로 888")
                        .price(16000).postCode("04043").sport(sports.get(3)).build());

                yoga3 = facilityRepository.save(Facility.builder()
                        .name("힐링요가 강남점").contact("010-4444-0003")
                        .address("서울시 강남구").detailAddress("테헤란로 999")
                        .price(20000).postCode("06234").sport(sports.get(3)).build());

                // 실내 수영 (3개)
                swim1 = facilityRepository.save(Facility.builder()
                        .name("아쿠아스포츠 수영장").contact("010-5555-0001")
                        .address("서울시 송파구").detailAddress("올림픽로 101")
                        .price(14000).postCode("05551").sport(sports.get(4)).build());

                swim2 = facilityRepository.save(Facility.builder()
                        .name("스위밍클럽 강남").contact("010-5555-0002")
                        .address("서울시 강남구").detailAddress("삼성로 202")
                        .price(15000).postCode("06234").sport(sports.get(4)).build());

                swim3 = facilityRepository.save(Facility.builder()
                        .name("워터파크 수영센터").contact("010-5555-0003")
                        .address("서울시 마포구").detailAddress("마포대로 303")
                        .price(13000).postCode("04043").sport(sports.get(4)).build());

                // 댄스 (3개)
                dance1 = facilityRepository.save(Facility.builder()
                        .name("댄스플로우 스튜디오").contact("010-6666-0001")
                        .address("서울시 강남구").detailAddress("논현로 404")
                        .price(19000).postCode("06236").sport(sports.get(5)).build());

                dance2 = facilityRepository.save(Facility.builder()
                        .name("리듬앤무브 홍대").contact("010-6666-0002")
                        .address("서울시 마포구").detailAddress("양화로 505")
                        .price(17000).postCode("04043").sport(sports.get(5)).build());

                dance3 = facilityRepository.save(Facility.builder()
                        .name("댄스아카데미 강남").contact("010-6666-0003")
                        .address("서울시 강남구").detailAddress("역삼로 606")
                        .price(18000).postCode("06235").sport(sports.get(5)).build());

                // 테니스 (3개)
                tennis1 = facilityRepository.save(Facility.builder()
                        .name("테니스클럽 서초").contact("010-7777-0001")
                        .address("서울시 서초구").detailAddress("반포대로 707")
                        .price(22000).postCode("06592").sport(sports.get(6)).build());

                tennis2 = facilityRepository.save(Facility.builder()
                        .name("코트에이스 송파").contact("010-7777-0002")
                        .address("서울시 송파구").detailAddress("올림픽로 808")
                        .price(20000).postCode("05551").sport(sports.get(6)).build());

                tennis3 = facilityRepository.save(Facility.builder()
                        .name("스매시테니스 강남").contact("010-7777-0003")
                        .address("서울시 강남구").detailAddress("선릉로 909")
                        .price(23000).postCode("06234").sport(sports.get(6)).build());

                // 풋살 (3개)
                futsal1 = facilityRepository.save(Facility.builder()
                        .name("풋살파크 마포").contact("010-8888-0001")
                        .address("서울시 마포구").detailAddress("월드컵로 1010")
                        .price(8000).postCode("04043").sport(sports.get(7)).build());

                futsal2 = facilityRepository.save(Facility.builder()
                        .name("골든풋살 강남").contact("010-8888-0002")
                        .address("서울시 강남구").detailAddress("강남대로 1111")
                        .price(9000).postCode("06234").sport(sports.get(7)).build());

                futsal3 = facilityRepository.save(Facility.builder()
                        .name("풋살존 송파").contact("010-8888-0003")
                        .address("서울시 송파구").detailAddress("올림픽로 1212")
                        .price(7500).postCode("05551").sport(sports.get(7)).build());

                // 골프 (3개)
                golf1 = facilityRepository.save(Facility.builder()
                        .name("골프존 강남점").contact("010-9999-0001")
                        .address("서울시 강남구").detailAddress("테헤란로 1313")
                        .price(30000).postCode("06234").sport(sports.get(8)).build());

                golf2 = facilityRepository.save(Facility.builder()
                        .name("스윙골프 서초점").contact("010-9999-0002")
                        .address("서울시 서초구").detailAddress("강남대로 1414")
                        .price(28000).postCode("06592").sport(sports.get(8)).build());

                golf3 = facilityRepository.save(Facility.builder()
                        .name("프리미엄골프 송파").contact("010-9999-0003")
                        .address("서울시 송파구").detailAddress("올림픽로 1515")
                        .price(32000).postCode("05551").sport(sports.get(8)).build());

                System.out.println("Facility data initialization complete (27 facilities created).");

            } else {
                // 기존 데이터 로드
                List<Facility> weightFacilities = facilityRepository.findBySportId(sports.get(0).getId(), null).getContent();
                List<Facility> climbFacilities = facilityRepository.findBySportId(sports.get(1).getId(), null).getContent();
                List<Facility> pilatesFacilities = facilityRepository.findBySportId(sports.get(2).getId(), null).getContent();
                List<Facility> yogaFacilities = facilityRepository.findBySportId(sports.get(3).getId(), null).getContent();
                List<Facility> swimFacilities = facilityRepository.findBySportId(sports.get(4).getId(), null).getContent();
                List<Facility> danceFacilities = facilityRepository.findBySportId(sports.get(5).getId(), null).getContent();
                List<Facility> tennisFacilities = facilityRepository.findBySportId(sports.get(6).getId(), null).getContent();
                List<Facility> futsalFacilities = facilityRepository.findBySportId(sports.get(7).getId(), null).getContent();
                List<Facility> golfFacilities = facilityRepository.findBySportId(sports.get(8).getId(), null).getContent();

                weight1 = weightFacilities.get(0);
                weight2 = weightFacilities.get(1);
                weight3 = weightFacilities.get(2);

                climb1 = climbFacilities.get(0);
                climb2 = climbFacilities.get(1);
                climb3 = climbFacilities.get(2);

                pilates1 = pilatesFacilities.get(0);
                pilates2 = pilatesFacilities.get(1);
                pilates3 = pilatesFacilities.get(2);

                yoga1 = yogaFacilities.get(0);
                yoga2 = yogaFacilities.get(1);
                yoga3 = yogaFacilities.get(2);

                swim1 = swimFacilities.get(0);
                swim2 = swimFacilities.get(1);
                swim3 = swimFacilities.get(2);

                dance1 = danceFacilities.get(0);
                dance2 = danceFacilities.get(1);
                dance3 = danceFacilities.get(2);

                tennis1 = tennisFacilities.get(0);
                tennis2 = tennisFacilities.get(1);
                tennis3 = tennisFacilities.get(2);

                futsal1 = futsalFacilities.get(0);
                futsal2 = futsalFacilities.get(1);
                futsal3 = futsalFacilities.get(2);

                golf1 = golfFacilities.get(0);
                golf2 = golfFacilities.get(1);
                golf3 = golfFacilities.get(2);
            }

            // ========================================
            // 3. Pass 메타데이터 (Data 1 - AI 추천용)
            // 총 30개, 모두 2종목 1회 체험 패키지
            // ========================================
            if (passRepository.count() == 0) {
                System.out.println("Initializing Pass metadata (30 trial packages)...");

                Pass p;

                // ========== LOW intensity (저강도) ==========

                // 1. 요가+필라테스 (LOW, EXPLORE)
                p = passRepository.save(Pass.builder()
                        .name("운동 첫걸음 (요가1회+필라1회)")
                        .price(43000)
                        .description("운동이 처음이신 분들을 위한 입문 패키지입니다. 요가로 몸의 유연성을 깨우고 필라테스로 코어 근육을 느껴보세요.")
                        .intensity("LOW")
                        .purposeTag("EXPLORE")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(yoga1).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(pilates1).build());

                // 3. 필라테스+요가 (LOW, REHAB)
                p = passRepository.save(Pass.builder()
                        .name("몸 회복 케어 (필라1회+요가1회)")
                        .price(43000)
                        .description("허리 통증이나 잘못된 자세로 고생하시나요? 필라테스와 요가로 통증을 완화하고 바른 자세를 되찾아보세요.")
                        .intensity("LOW")
                        .purposeTag("REHAB")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(pilates2).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(yoga2).build());

                // 4. 댄스+요가 (LOW, STRESS_RELIEF)
                p = passRepository.save(Pass.builder()
                        .name("힐링 라이프 (댄스1회+요가1회)")
                        .price(35000)
                        .description("일상의 스트레스를 날려버리세요. 음악에 맞춰 몸을 움직이고 요가로 마음을 편안하게 만드는 힐링 패키지입니다.")
                        .intensity("LOW")
                        .purposeTag("STRESS_RELIEF")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(dance1).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(yoga3).build());

                // ========== HIGH intensity (고강도) ==========

                // 2. 웨이트+수영 (HIGH, DIET)
                p = passRepository.save(Pass.builder()
                        .name("체중감량 시작 (웨이트1회+수영1회)")
                        .price(29000)
                        .description("다이어트를 결심하셨나요? 웨이트 트레이닝으로 근육을 만들고 수영으로 지방을 태우는 효과적인 조합입니다.")
                        .intensity("HIGH")
                        .purposeTag("DIET")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(weight1).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(swim1).build());

                // 5. 웨이트+클라이밍 (MID, FITNESS)
                p = passRepository.save(Pass.builder()
                        .name("체력 업그레이드 (웨이트1회+클라이밍1회)")
                        .price(32000)
                        .description("체력의 한계를 뛰어넘고 싶으신가요? 웨이트로 근력을 키우고 클라이밍으로 전신 지구력을 향상시키는 균형잡힌 패키지입니다.")
                        .intensity("MID")
                        .purposeTag("FITNESS")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(weight2).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(climb1).build());

                // 6. 웨이트+댄스 (MID, DIET)
                p = passRepository.save(Pass.builder()
                        .name("탄탄 바디 만들기 (웨이트1회+댄스1회)")
                        .price(31000)
                        .description("몸매 변화를 원하신다면 이 패키지가 정답입니다. 웨이트로 근육을 만들고 댄스로 유산소 운동을 더해 탄탄한 몸을 만들어보세요.")
                        .intensity("MID")
                        .purposeTag("DIET")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(weight3).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(dance2).build());

                // 7. 웨이트+클라이밍 (HIGH, FITNESS)
                p = passRepository.save(Pass.builder()
                        .name("익스트림 도전 (웨이트1회+클라이밍1회)")
                        .price(35000)
                        .description("강도 높은 운동을 찾으시나요? 무거운 중량의 웨이트와 도전적인 클라이밍 루트로 한계를 돌파하는 강력한 패키지입니다.")
                        .intensity("HIGH")
                        .purposeTag("FITNESS")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(weight1).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(climb2).build());

                // 8. 수영+필라테스 (MID, FITNESS)
                p = passRepository.save(Pass.builder()
                        .name("수영 입문 (수영1회+필라1회)")
                        .price(39000)
                        .description("수영을 시작하고 싶으신가요? 필라테스로 수영에 필요한 코어 근육을 먼저 단련하고 수영장에서 자신감 있게 첫 물장구를 떠보세요.")
                        .intensity("MID")
                        .purposeTag("FITNESS")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(swim2).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(pilates3).build());

                // 9. 테니스+요가 (MID, EXPLORE)
                p = passRepository.save(Pass.builder()
                        .name("라켓 스포츠 입문 (테니스1회+요가1회)")
                        .price(40000)
                        .description("새로운 취미를 찾고 계신가요? 테니스로 라켓 스포츠의 재미를 느끼고 요가로 운동 후 근육을 이완시키는 균형잡힌 조합입니다.")
                        .intensity("MID")
                        .purposeTag("EXPLORE")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(tennis1).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(yoga1).build());

                // 10. 풋살+웨이트 (MID, FITNESS)
                p = passRepository.save(Pass.builder()
                        .name("팀 스포츠 체험 (풋살1회+웨이트1회)")
                        .price(23000)
                        .description("혼자가 아닌 함께하는 운동을 원하신다면! 풋살로 팀플레이의 즐거움을 느끼고 웨이트로 경기력을 높이는 실속있는 패키지입니다.")
                        .intensity("MID")
                        .purposeTag("FITNESS")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(futsal1).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(weight2).build());

                // 11. 골프+필라테스 (LOW, EXPLORE)
                p = passRepository.save(Pass.builder()
                        .name("골프 시작 (골프1회+필라1회)")
                        .price(55000)
                        .description("골프에 관심이 생기셨나요? 필라테스로 골프 스윙에 필요한 코어와 회전력을 키우고 스크린 골프장에서 첫 샷을 날려보세요.")
                        .intensity("LOW")
                        .purposeTag("EXPLORE")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(golf1).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(pilates1).build());

                // 12. 클라이밍+요가 (MID, STRESS_RELIEF)
                p = passRepository.save(Pass.builder()
                        .name("클라이밍 힐링 (클라이밍1회+요가1회)")
                        .price(34000)
                        .description("머리를 비우고 싶으신가요? 클라이밍으로 벽에만 집중하며 잡념을 날리고 요가로 몸과 마음을 이완시키는 힐링 패키지입니다.")
                        .intensity("MID")
                        .purposeTag("STRESS_RELIEF")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(climb3).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(yoga2).build());

                // 13. 수영+댄스 (HIGH, DIET)
                p = passRepository.save(Pass.builder()
                        .name("유산소 끝판왕 (수영1회+댄스1회)")
                        .price(32000)
                        .description("체지방을 확실하게 줄이고 싶다면 이 조합을 추천합니다. 수영으로 전신 칼로리를 태우고 댄스로 땀을 흘리며 즐겁게 다이어트하세요.")
                        .intensity("HIGH")
                        .purposeTag("DIET")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(swim3).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(dance3).build());

                // 14. 테니스+웨이트 (MID, FITNESS)
                p = passRepository.save(Pass.builder()
                        .name("파워 테니스 (테니스1회+웨이트1회)")
                        .price(37000)
                        .description("더 강한 스윙을 원하신다면! 웨이트로 어깨와 코어 근력을 키우고 테니스 코트에서 파워풀한 샷을 날려보세요.")
                        .intensity("MID")
                        .purposeTag("FITNESS")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(tennis2).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(weight3).build());

                // 15. 풋살+수영 (MID, DIET)
                p = passRepository.save(Pass.builder()
                        .name("체력 다이어트 (풋살1회+수영1회)")
                        .price(22000)
                        .description("즐겁게 살을 빼고 싶다면 이 패키지가 답입니다. 풋살로 뛰면서 칼로리를 소모하고 수영으로 무릎에 무리 없이 전신을 단련하세요.")
                        .intensity("MID")
                        .purposeTag("DIET")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(futsal2).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(swim1).build());

                // 16. 골프+웨이트 (LOW, FITNESS)
                p = passRepository.save(Pass.builder()
                        .name("골프 근력 강화 (골프1회+웨이트1회)")
                        .price(45000)
                        .description("골프 비거리를 늘리고 싶으신가요? 웨이트로 허리와 하체 근력을 강화하고 골프장에서 더 멀리 날아가는 샷을 경험해보세요.")
                        .intensity("LOW")
                        .purposeTag("FITNESS")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(golf2).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(weight1).build());

                // 17. 댄스+필라테스 (LOW, STRESS_RELIEF)
                p = passRepository.save(Pass.builder()
                        .name("리듬 힐링 (댄스1회+필라1회)")
                        .price(42000)
                        .description("일과 일상에 지쳐 있나요? 신나는 음악에 맞춰 댄스로 스트레스를 날리고 필라테스로 굳은 몸을 풀어주는 휴식 패키지입니다.")
                        .intensity("LOW")
                        .purposeTag("STRESS_RELIEF")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(dance1).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(pilates2).build());

                // 18. 클라이밍+수영 (HIGH, FITNESS)
                p = passRepository.save(Pass.builder()
                        .name("전신 운동 콤보 (클라이밍1회+수영1회)")
                        .price(31000)
                        .description("상하체를 모두 발달시키고 싶으신가요? 클라이밍으로 등과 팔 근육을 키우고 수영으로 하체와 심폐지구력을 강화하는 완벽한 조합입니다.")
                        .intensity("HIGH")
                        .purposeTag("FITNESS")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(climb1).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(swim2).build());

                // 19. 테니스+필라테스 (LOW, REHAB)
                p = passRepository.save(Pass.builder()
                        .name("부상 예방 패키지 (테니스1회+필라1회)")
                        .price(47000)
                        .description("운동 중 부상이 걱정되시나요? 필라테스로 관절을 보호하는 코어를 만들고 테니스로 안전하게 운동 강도를 높여보세요.")
                        .intensity("LOW")
                        .purposeTag("REHAB")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(tennis3).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(pilates3).build());

                // 20. 풋살+댄스 (MID, STRESS_RELIEF)
                p = passRepository.save(Pass.builder()
                        .name("즐거운 운동 (풋살1회+댄스1회)")
                        .price(26000)
                        .description("운동이 지루하게 느껴지시나요? 풋살로 친구들과 즐겁게 공을 차고 댄스로 신나는 음악에 몸을 맡기는 재미있는 패키지입니다.")
                        .intensity("MID")
                        .purposeTag("STRESS_RELIEF")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(futsal3).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(dance2).build());

                // 21. 골프+요가 (LOW, STRESS_RELIEF)
                p = passRepository.save(Pass.builder()
                        .name("골프 멘탈 강화 (골프1회+요가1회)")
                        .price(50000)
                        .description("골프는 정신력이 중요한 운동입니다. 요가로 집중력과 평정심을 기르고 골프장에서 흔들리지 않는 멘탈로 좋은 스코어를 만들어보세요.")
                        .intensity("LOW")
                        .purposeTag("STRESS_RELIEF")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(golf3).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(yoga3).build());

                // 22. 클라이밍+웨이트 (HIGH, DIET)
                p = passRepository.save(Pass.builder()
                        .name("근육 폭발 (클라이밍1회+웨이트1회)")
                        .price(33000)
                        .description("상체 근육을 집중적으로 키우고 싶으신가요? 클라이밍으로 등과 어깨를 자극하고 웨이트로 가슴과 팔 근육까지 완성하는 상체 특화 패키지입니다.")
                        .intensity("HIGH")
                        .purposeTag("DIET")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(climb2).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(weight2).build());

                // 23. 수영+요가 (LOW, REHAB)
                p = passRepository.save(Pass.builder()
                        .name("관절 회복 (수영1회+요가1회)")
                        .price(32000)
                        .description("무릎이나 관절이 안 좋으신가요? 물에서 무중력 상태로 부담 없이 운동하고 요가로 관절 가동범위를 넓히는 재활 중심 패키지입니다.")
                        .intensity("LOW")
                        .purposeTag("REHAB")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(swim1).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(yoga1).build());

                // 24. 테니스+댄스 (MID, FITNESS)
                p = passRepository.save(Pass.builder()
                        .name("민첩성 향상 (테니스1회+댄스1회)")
                        .price(39000)
                        .description("빠른 움직임이 필요한 스포츠를 준비 중이신가요? 테니스로 순발력을 키우고 댄스로 리듬감과 발놀림을 향상시키는 민첩성 강화 패키지입니다.")
                        .intensity("MID")
                        .purposeTag("FITNESS")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(tennis1).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(dance3).build());

                // 25. 풋살+클라이밍 (MID, EXPLORE)
                p = passRepository.save(Pass.builder()
                        .name("새로운 도전 (풋살1회+클라이밍1회)")
                        .price(24000)
                        .description("익숙한 운동에서 벗어나 새로운 것을 시도해보고 싶으신가요? 풋살로 팀 스포츠를, 클라이밍으로 익스트림 스포츠를 체험하는 탐험 패키지입니다.")
                        .intensity("MID")
                        .purposeTag("EXPLORE")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(futsal1).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(climb3).build());

                // 26. 골프+수영 (LOW, FITNESS)
                p = passRepository.save(Pass.builder()
                        .name("저강도 운동 (골프1회+수영1회)")
                        .price(44000)
                        .description("강도 높은 운동이 부담스러우신가요? 골프로 가볍게 몸을 움직이고 수영으로 천천히 심폐 기능을 키우는 편안한 운동 패키지입니다.")
                        .intensity("LOW")
                        .purposeTag("FITNESS")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(golf1).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(swim3).build());

                // 27. 댄스+수영 (MID, DIET)
                p = passRepository.save(Pass.builder()
                        .name("지방 연소 (댄스1회+수영1회)")
                        .price(31000)
                        .description("다이어트의 핵심은 유산소 운동입니다. 댄스로 땀을 흘리며 즐겁게 칼로리를 태우고 수영으로 전신 지방을 연소시키는 효율적인 다이어트 패키지입니다.")
                        .intensity("MID")
                        .purposeTag("DIET")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(dance1).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(swim2).build());

                // 28. 클라이밍+필라테스 (MID, REHAB)
                p = passRepository.save(Pass.builder()
                        .name("코어 강화 (클라이밍1회+필라1회)")
                        .price(41000)
                        .description("몸의 중심인 코어를 단단하게 만들고 싶으신가요? 클라이밍으로 실전에서 코어를 쓰는 법을 배우고 필라테스로 깊은 코어 근육까지 자극하는 집중 패키지입니다.")
                        .intensity("MID")
                        .purposeTag("REHAB")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(climb1).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(pilates1).build());

                // 29. 테니스+풋살 (MID, EXPLORE)
                p = passRepository.save(Pass.builder()
                        .name("구기 종목 탐험 (테니스1회+풋살1회)")
                        .price(30000)
                        .description("공을 다루는 운동에 흥미가 있으신가요? 테니스로 라켓과 공의 감각을, 풋살로 발과 공의 터치감을 익히며 구기 종목의 재미를 발견하는 패키지입니다.")
                        .intensity("MID")
                        .purposeTag("EXPLORE")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(tennis2).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(futsal2).build());

                // 30. 골프+댄스 (LOW, STRESS_RELIEF)
                p = passRepository.save(Pass.builder()
                        .name("여유로운 운동 (골프1회+댄스1회)")
                        .price(47000)
                        .description("운동이 부담스럽지 않은 편안한 조합을 원하신다면! 골프로 여유롭게 스윙을 즐기고 댄스로 가볍게 몸을 풀며 기분 좋게 땀 흘리는 힐링 패키지입니다.")
                        .intensity("LOW")
                        .purposeTag("STRESS_RELIEF")
                        .build());
                passItemRepository.save(PassItem.builder().pass(p).facility(golf2).build());
                passItemRepository.save(PassItem.builder().pass(p).facility(dance2).build());


                System.out.println("Pass metadata initialization complete (30 packages created).");
            }
        };
    }
}
