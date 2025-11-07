package com.api.mov;

import com.api.mov.domain.pass.entity.Sport;
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
    public CommandLineRunner initData(SportRepository sportRepository) {
        return args -> {
            // DB에 데이터가 없을 때만 실행 (중복 방지)
            if (sportRepository.count() == 0) {
                System.out.println("Initializing Sport master data...");

                List<Sport> sports = Arrays.asList(
                        Sport.builder().name("헬스/PT").build(),
                        Sport.builder().name("필라테스").build(),
                        Sport.builder().name("요가").build(),
                        Sport.builder().name("수영").build(),
                        Sport.builder().name("클라이밍").build(),
                        Sport.builder().name("크로스핏").build(),
                        Sport.builder().name("F45").build(),
                        Sport.builder().name("파워리프팅").build()
                        );

                sportRepository.saveAll(sports);
                System.out.println("Sport data initialization complete.");
            }
        };
    }
}
