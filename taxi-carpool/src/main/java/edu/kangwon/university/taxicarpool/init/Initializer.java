package edu.kangwon.university.taxicarpool.init;

import edu.kangwon.university.taxicarpool.member.Gender;
import edu.kangwon.university.taxicarpool.member.MemberEntity;
import edu.kangwon.university.taxicarpool.member.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Initializer {

    @Bean
    public CommandLineRunner initMembers(MemberRepository memberRepository,
        PasswordEncoder passwordEncoder) {
        return args -> {
            MemberEntity alice = new MemberEntity(
                "ex1@example.com",
                passwordEncoder.encode("1234"),
                "Alice",
                Gender.FEMALE
            );
            memberRepository.save(alice);

            MemberEntity bob = new MemberEntity(
                "ex2@example.com",
                passwordEncoder.encode("1234"),
                "Bob",
                Gender.MALE
            );
            memberRepository.save(bob);

            MemberEntity carol = new MemberEntity(
                "ex3@example.com",
                passwordEncoder.encode("1234"),
                "Carol",
                Gender.FEMALE
            );
            memberRepository.save(carol);
            System.out.println("초기 데이터 로딩 완료");
        };
    }
}
