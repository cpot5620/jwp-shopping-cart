package cart.member.service;

import cart.exception.DuplicateEmailException;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.member.dto.DtoMapper;
import cart.member.dto.MemberAddRequest;
import cart.member.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {

    private final MemberDao memberDao;

    @Autowired
    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberDto> getAllMembers() {
        return memberDao.findAll()
                        .stream()
                        .map(DtoMapper::toMemberDto)
                        .collect(Collectors.toUnmodifiableList());
    }

    public long register(MemberAddRequest memberAddRequest) {
        final Member validMember = DtoMapper.toValidMember(memberAddRequest);
        if (memberDao.containsEmail(validMember.getEmail())) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다");
        }

        final Member saved = memberDao.save(validMember);
        return saved.getId();
    }
}
