package cart.member.dao;

import cart.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcMemberDao implements MemberDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> Member.of(
            resultSet.getLong("id"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );

    @Override
    public void insert(final Member member) {
        final String sql = "INSERT INTO member(email, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword());
    }

    @Override
    public Member findByEmailAndPassword(final String email, final String password) {
        final String sql = "SELECT * FROM member WHERE email = ? AND password = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper, email, password);
    }

    @Override
    public Member findById(final Long memberId) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper, memberId);
    }

    @Override
    public List<Member> findAll() {
        final String sql = "SELECT * FROM member";
        return jdbcTemplate.query(sql, memberRowMapper);
    }
}
