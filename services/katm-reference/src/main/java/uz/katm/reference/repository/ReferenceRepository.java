package uz.katm.reference.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import uz.katm.reference.domain.RefDto;

import java.util.List;

/**
 * Доступ к справочникам через Oracle pipelined-функции пакета {@code datas.pkg_retailers}
 * (перенос gov.uz.katm.repo.ref.RefRepositoryImpl).
 */
@Repository
public class ReferenceRepository {

    private static final RowMapper<RefDto> CODE_NAME_MAPPER =
            (rs, rowNum) -> new RefDto(rs.getString("CODE"), rs.getString("NAME"));

    /** Для GET_IN_TEST_STATUSES код берётся из колонки ID. */
    private static final RowMapper<RefDto> ID_NAME_MAPPER =
            (rs, rowNum) -> new RefDto(rs.getString("ID"), rs.getString("NAME"));

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public ReferenceRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public List<RefDto> getRegions() {
        return jdbcTemplate.query(
                "select * from table(datas.pkg_retailers.GET_REGIONS_LIST)",
                CODE_NAME_MAPPER);
    }

    public List<RefDto> getLocalRegions(String code) {
        return namedJdbcTemplate.query(
                "select * from table(datas.pkg_retailers.GET_local_REGIONS_LIST(:code))",
                new MapSqlParameterSource("code", code),
                CODE_NAME_MAPPER);
    }

    public List<RefDto> getRetailerModes() {
        return jdbcTemplate.query(
                "select * from table(datas.pkg_retailers.GET_IN_TEST_STATUSES)",
                ID_NAME_MAPPER);
    }
}
