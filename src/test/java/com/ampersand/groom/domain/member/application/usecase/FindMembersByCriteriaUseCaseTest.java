package com.ampersand.groom.domain.member.application.usecase;

import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.domain.member.persistence.mapper.MemberMapper;
import com.ampersand.groom.domain.member.presentation.data.response.GetMemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Member 검색 UseCase 클래스의")
public class FindMembersByCriteriaUseCaseTest {

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private FindMembersByCriteriaUseCase findMembersByCriteriaUseCase;

    @DisplayName("execute 메서드는")
    @Nested
    class Describe_execute {

        @Nested
        @DisplayName("모든 검색 조건이 제공되었을 때")
        class Context_with_all_parameters {

            @DisplayName("조건에 맞는 Member 리스트를 반환한다.")
            @Test
            void it_returns_matching_members() {
                // given
                Long id = 1L;
                String name = "홍길동";
                Integer generation = 1;
                String email = "s00001@gsm.hs.kr";
                Boolean isAvailable = true;
                MemberRole role = MemberRole.ROLE_STUDENT;

                List<Member> members = List.of(
                        Member.builder()
                                .id(1L)
                                .name("홍길동")
                                .generation(1)
                                .email("s00001@gsm.hs.kr")
                                .isAvailable(true)
                                .role(MemberRole.ROLE_STUDENT)
                                .build()
                );

                when(memberPersistencePort.findMembersByCriteria(id, name, generation, email, isAvailable, role))
                        .thenReturn(members);
                when(memberMapper.toResponse(members.getFirst()))
                        .thenReturn(new GetMemberResponse(1L, "홍길동", 1, "s00001@gsm.hs.kr", true, MemberRole.ROLE_STUDENT));

                // when
                List<GetMemberResponse> result = findMembersByCriteriaUseCase.execute(id, name, generation, email, isAvailable, role);

                // then
                verify(memberPersistencePort).findMembersByCriteria(id, name, generation, email, isAvailable, role);
                assertEquals(1, result.size());
                assertEquals("홍길동", result.getFirst().name());
                assertEquals("s00001@gsm.hs.kr", result.getFirst().email());
            }
        }

        @Nested
        @DisplayName("검색 조건이 일부만 제공되었을 때")
        class Context_with_partial_parameters {

            @DisplayName("조건에 맞는 Member 리스트를 반환한다.")
            @Test
            void it_returns_matching_members_with_partial_conditions() {
                // given
                String name = "홍길동";
                Boolean isAvailable = true;

                List<Member> members = List.of(
                        Member.builder()
                                .id(2L)
                                .name("성춘향")
                                .generation(2)
                                .email("s00002@gsm.hs.kr")
                                .isAvailable(true)
                                .role(MemberRole.ROLE_STUDENT)
                                .build()
                );

                when(memberPersistencePort.findMembersByCriteria(null, name, null, null, isAvailable, null))
                        .thenReturn(members);
                when(memberMapper.toResponse(members.getFirst()))
                        .thenReturn(new GetMemberResponse(2L, "성춘향", 2, "s00002@gsm.hs.kr", true, MemberRole.ROLE_STUDENT));

                // when
                List<GetMemberResponse> result = findMembersByCriteriaUseCase.execute(null, name, null, null, isAvailable, null);

                // then
                verify(memberPersistencePort).findMembersByCriteria(null, name, null, null, isAvailable, null);
                assertEquals(1, result.size());
                assertEquals("성춘향", result.getFirst().name());
                assertEquals("s00002@gsm.hs.kr", result.getFirst().email());
            }
        }

        @Nested
        @DisplayName("검색 조건에 맞는 Member가 없을 때")
        class Context_with_no_matching_members {

            @DisplayName("빈 리스트를 반환한다.")
            @Test
            void it_returns_empty_list() {
                // given
                String name = "놀부";

                when(memberPersistencePort.findMembersByCriteria(null, name, null, null, null, null))
                        .thenReturn(List.of());

                // when
                List<GetMemberResponse> result = findMembersByCriteriaUseCase.execute(null, name, null, null, null, null);

                // then
                verify(memberPersistencePort).findMembersByCriteria(null, name, null, null, null, null);
                assertTrue(result.isEmpty());
            }
        }

        @Nested
        @DisplayName("검색 조건이 모두 null일 때")
        class Context_with_all_null_parameters {

            @DisplayName("전체 Member 리스트를 반환한다.")
            @Test
            void it_returns_all_members() {
                // given
                List<Member> members = List.of(
                        Member.builder()
                                .id(1L)
                                .name("홍길동")
                                .generation(1)
                                .email("s00001@gsm.hs.kr")
                                .isAvailable(true)
                                .role(MemberRole.ROLE_STUDENT)
                                .build(),
                        Member.builder()
                                .id(2L)
                                .name("성춘향")
                                .generation(2)
                                .email("s00002@gsm.hs.kr")
                                .isAvailable(false)
                                .role(MemberRole.ROLE_ADMIN)
                                .build()
                );

                when(memberPersistencePort.findMembersByCriteria(null, null, null, null, null, null))
                        .thenReturn(members);
                when(memberMapper.toResponse(members.get(0)))
                        .thenReturn(new GetMemberResponse(1L, "홍길동", 1, "s00001@gsm.hs.kr", true, MemberRole.ROLE_STUDENT));
                when(memberMapper.toResponse(members.get(1)))
                        .thenReturn(new GetMemberResponse(2L, "성춘향", 2, "s00002@gsm.hs.kr", false, MemberRole.ROLE_ADMIN));

                // when
                List<GetMemberResponse> result = findMembersByCriteriaUseCase.execute(null, null, null, null, null, null);

                // then
                verify(memberPersistencePort).findMembersByCriteria(null, null, null, null, null, null);
                assertEquals(2, result.size());
                assertEquals("홍길동", result.get(0).name());
                assertEquals("성춘향", result.get(1).name());
            }
        }
    }
}