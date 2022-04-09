package kz.iitu.itse1910.issenbayev.repository;

import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.entity.User_;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserJdbcMapper userJdbcMapper;

    @Override
    public Page<User> findAll(Pageable pageable) {
        String sql = "select * from users";
        List<User> users = jdbcTemplate.query(sql + paginationSql(pageable), userJdbcMapper);
        return toUserPage(users, pageable, totalRowsForNonCountSql(sql));
    }

    private String paginationSql(Pageable pageable) {
        return String.format(" order by id limit %d offset %d",
                pageable.getPageSize(), pageable.getPageNumber() * pageable.getPageSize());
    }

    private long totalRowsForNonCountSql(String nonCountSql) {
        Long total = jdbcTemplate.queryForObject(toCountSql(nonCountSql), Long.class);
        assert(total != null);
        return total;
    }

    private String toCountSql(String nonCountSql) {
        String[] sqlTokens = nonCountSql.split(" ");
        sqlTokens[1] = "count(id)";
        return String.join(" ", sqlTokens);
    }

    private Page<User> toUserPage(List<User> users, Pageable pageable, Long total) {
        return new PageImpl<>(users, pageable, total);
    }

    @Override
    public Page<User> findAll(Pageable pageable, String role) {
        String parameterizedSql = "select * from users where " + User_.ROLE + "=?";
        List<User> users = jdbcTemplate.query(parameterizedSql + paginationSql(pageable), userJdbcMapper, role);
        Long totalRows = jdbcTemplate.queryForObject(toCountSql(parameterizedSql), Long.class, role);
        assert(totalRows != null);
        return toUserPage(users, pageable, totalRows);
    }

    @Override
    public Page<User> findUnassignedLeadDevs(Pageable pageable) {
        String sql = "select * from users u where " + User_.ROLE + "='" + User.Role.LEAD_DEV + "' and " +
                "(select count(p.id) from projects p where p.lead_dev_id = u.id) = 0";
        List<User> users = jdbcTemplate.query(sql + paginationSql(pageable), userJdbcMapper);
        return toUserPage(users, pageable, totalRowsForNonCountSql(sql));
    }

    @Override
    public Page<User> findAssignedLeadDevs(Pageable pageable) {
        String sql = "select * from users u where " + User_.ROLE + "='" + User.Role.LEAD_DEV + "' and " +
                "(select count(p.id) from projects p where p.lead_dev_id = u.id) = 1";
        List<User> users = jdbcTemplate.query(sql + paginationSql(pageable), userJdbcMapper);
        return toUserPage(users, pageable, totalRowsForNonCountSql(sql));
    }

    @Override
    public Page<User> findUnassignedDevelopers(Pageable pageable) {
        String sql = "select * from users u where " + User_.ROLE + "='" + User.Role.DEVELOPER + "' and " +
                        "(select count(pa.id) from project_assignees pa where pa.assignee_id = u.id) = 0";
        List<User> users = jdbcTemplate.query(sql + paginationSql(pageable), userJdbcMapper);
        return toUserPage(users, pageable, totalRowsForNonCountSql(sql));
    }

    @Override
    public Page<User> findAssignedDevelopers(Pageable pageable) {
        String sql = "select * from users u where " + User_.ROLE + "='" + User.Role.DEVELOPER + "' and " +
                "(select count(pa.id) from project_assignees pa where pa.assignee_id = u.id) = 1";
        List<User> users = jdbcTemplate.query(sql + paginationSql(pageable), userJdbcMapper);
        return toUserPage(users, pageable, totalRowsForNonCountSql(sql));
    }

    @Override
    public Optional<User> findById(long id) {
        String parameterizedSql = "select * from users where id=?";
        List<User> userList = jdbcTemplate.query(parameterizedSql, userJdbcMapper, id);
        if (userList.isEmpty()) {
            return Optional.empty();
        } else if (userList.size() == 1) {
            return Optional.of(userList.get(0));
        }
        throw new IllegalStateException("Select by ID returned more than one row.");
    }

    @Override
    public User insert(User user) {
        Map<String, Object> paramMap = Map.of(
                User_.ROLE, user.getRole(),
                User_.EMAIL, user.getEmail(),
                User_.USERNAME, user.getUsername(),
                User_.PASSWORD, user.getPassword(),
                User.Field.CREATED_ON, new Date(new java.util.Date().getTime()).toString()
        );
        KeyHolder keyHolder = new GeneratedKeyHolder();
        InsertUser insertUser = new InsertUser(jdbcTemplate.getDataSource());
        insertUser.updateByNamedParam(paramMap, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        Map<String, Object> paramMap = Map.of(
                User_.ID, user.getId(),
                User_.ROLE, user.getRole(),
                User_.EMAIL, user.getEmail(),
                User_.USERNAME, user.getUsername()
        );
        UpdateUser updateUser  = new UpdateUser(jdbcTemplate.getDataSource());
        updateUser.updateByNamedParam(paramMap);
        return user;
    }

    @Override
    public void delete(User user) {
        String sql = "delete from users where id=?";
        int isDeleted = jdbcTemplate.update(sql, user.getId());
        if (isDeleted != 1) {
            throw new IllegalStateException("Couldn't delete user with id " + user.getId());
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        String parameterizedSql = "select count(id) = 1 from users where " + User_.USERNAME + "=?";
        Boolean exists = jdbcTemplate.queryForObject(parameterizedSql, Boolean.class, username);
        assert(exists != null);
        return exists;
    }

    @Override
    public boolean existsByEmail(String email) {
        String parameterizedSql = "select count(id) = 1 from users where " + User_.EMAIL + "=?";
        Boolean exists = jdbcTemplate.queryForObject(parameterizedSql, Boolean.class, email);
        assert(exists != null);
        return exists;
    }

    @Component
    private static class UserJdbcMapper implements RowMapper<User> {
        public UserJdbcMapper() {}

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .id(rs.getLong(User_.ID))
                    .role(rs.getString(User_.ROLE))
                    .email(rs.getString(User_.EMAIL))
                    .username(rs.getString(User_.USERNAME))
                    .password(rs.getString(User_.PASSWORD))
                    .build();
        }
    }

    private static class InsertUser extends SqlUpdate {
        private static final String SQL_INSERT_USER = "insert into users " +
                "(" + User_.ROLE + ", " + User_.EMAIL + ", " + User_.USERNAME + ", " + User_.PASSWORD +  ", "
                + User.Field.CREATED_ON + ") " +
            "values (:" + User_.ROLE + ", :" + User_.EMAIL + ", :" + User_.USERNAME + ", :" + User_.PASSWORD +
                ", :" + User.Field.CREATED_ON + ")";

        public InsertUser(DataSource ds) {
            super(ds, SQL_INSERT_USER);
            super.declareParameter(new SqlParameter(User_.ROLE, Types.VARCHAR));
            super.declareParameter(new SqlParameter(User_.EMAIL, Types.VARCHAR));
            super.declareParameter(new SqlParameter(User_.USERNAME, Types.VARCHAR));
            super.declareParameter(new SqlParameter(User_.PASSWORD, Types.VARCHAR));
            super.declareParameter(new SqlParameter(User.Field.CREATED_ON, Types.DATE));
        }
    }

    private static class UpdateUser extends SqlUpdate {
        private static final String SQL_UPDATE_USER = "update users set " +
                User_.ROLE + "=:" + User_.ROLE +
                ", " + User_.EMAIL + "=:" + User_.EMAIL +
                ", " + User_.USERNAME + "=:" + User_.USERNAME +
                " where " + User_.ID + "=:" + User_.ID;

        public UpdateUser(DataSource ds) {
            super(ds, SQL_UPDATE_USER);
            super.declareParameter(new SqlParameter(User_.ID, Types.BIGINT));
            super.declareParameter(new SqlParameter(User_.ROLE, Types.VARCHAR));
            super.declareParameter(new SqlParameter(User_.EMAIL, Types.VARCHAR));
            super.declareParameter(new SqlParameter(User_.USERNAME, Types.VARCHAR));
        }
    }
}
