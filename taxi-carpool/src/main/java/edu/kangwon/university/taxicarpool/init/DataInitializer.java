package edu.kangwon.university.taxicarpool.init;

import edu.kangwon.university.taxicarpool.member.Gender;
import edu.kangwon.university.taxicarpool.member.MemberRepository;
import edu.kangwon.university.taxicarpool.member.MemberService;
import edu.kangwon.university.taxicarpool.member.dto.MemberCreateDTO;
import edu.kangwon.university.taxicarpool.party.PartyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MemberService memberService;

    public DataInitializer(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void run(String... args) {

        // 초기데이터 넣을 때, 원래 signUp을 해야하지만, 이메일 인증 과정을
        // 우회하기 위해 초기 멤버데이터 넣기.
        MemberCreateDTO member1 = new MemberCreateDTO("user1@kangwon.ac.kr", "password1", "유저1", Gender.MALE);
        MemberCreateDTO member2 = new MemberCreateDTO("user2@kangwon.ac.kr", "password2", "유저2", Gender.FEMALE);
        MemberCreateDTO member3 = new MemberCreateDTO("user3@kangwon.ac.kr", "password3", "유저3", Gender.MALE);

        memberService.createMember(member1);
        memberService.createMember(member2);
        memberService.createMember(member3);

        System.out.println("===== 초기 데이터 로딩 완료 =====");
    }
}