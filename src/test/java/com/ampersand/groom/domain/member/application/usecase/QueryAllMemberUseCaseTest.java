package com.ampersand.groom.domain.member.application.usecase;

import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.domain.member.persistence.mapper.MemberMapper;
import com.ampersand.groom.domain.member.presentation.data.response.GetMemberResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Member 전체 조회 UseCase 클래스의")
class QueryAllMemberUseCaseTest {

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private QueryAllMemberUseCase queryAllMemberUseCase;

    @DisplayName("execute 메서드는")
    @Nested
    class Describe_execute {

        @Nested
        @DisplayName("모든 Member가 존재할 때")
        class Context_with_existing_members {

            @Test
            @DisplayName("모든 Member를 조회하여 반환한다.")
            void it_returns_all_members() {
                // given
                List<Member> members = List.of(
                        Member.builder()
                                .id(1L)
                                .name("홍길동")
                                .generation(1)
                                .email("s00001@gsm.hs.kr")
                                .isAvailable(false)
                                .role(MemberRole.ROLE_ADMIN)
                                .build(),
                        Member.builder()
                                .id(2L)
                                .name("성춘향")
                                .generation(2)
                                .email("s00002@gsm.hs.kr")
                                .isAvailable(true)
                                .role(MemberRole.ROLE_STUDENT)
                                .build(),
                        Member.builder()
                                .id(3L)
                                .name("이몽룡")
                                .generation(3)
                                .email("s00003@gsm.hs.kr")
                                .isAvailable(true)
                                .role(MemberRole.ROLE_TEACHER)
                                .build()
                );

                when(memberPersistencePort.queryAllMember()).thenReturn(members);

                members.forEach(member -> {
                    when(memberMapper.toResponse(member)).thenReturn(
                            new GetMemberResponse(
                                    member.getId(),
                                    member.getName(),
                                    member.getGeneration(),
                                    member.getEmail(),
                                    member.getIsAvailable(),
                                    member.getRole()
                            )
                    );
                });

                // when
                List<GetMemberResponse> result = queryAllMemberUseCase.execute();

                // then
                verify(memberPersistencePort).queryAllMember();
                assertEquals(members.size(), result.size());
                for (int i = 0; i < members.size(); i++) {
                    assertMemberEquals(members.get(i), result.get(i));
                }
            }
        }

        @Nested
        @DisplayName("Member가 하나도 존재하지 않을 때")
        class Context_with_no_members {

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_an_empty_list() {
                // given
                when(memberPersistencePort.queryAllMember()).thenReturn(List.of());

                // when
                List<GetMemberResponse> result = queryAllMemberUseCase.execute();

                // then
                verify(memberPersistencePort).queryAllMember();
                assertTrue(result.isEmpty());
            }
        }
    }

    private void assertMemberEquals(Member member, GetMemberResponse response) {
        assertEquals(member.getId(), response.id());
        assertEquals(member.getName(), response.name());
        assertEquals(member.getGeneration(), response.generation());
        assertEquals(member.getEmail(), response.email());
        assertEquals(member.getIsAvailable(), response.isAvailable());
        assertEquals(member.getRole(), response.role());
    }
}