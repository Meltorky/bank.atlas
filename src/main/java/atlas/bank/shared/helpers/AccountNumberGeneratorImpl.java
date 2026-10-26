package atlas.bank.shared.helpers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccountNumberGeneratorImpl
        implements AccountNumberGenerator {

    private final JdbcTemplate jdbcTemplate;

    public AccountNumberGeneratorImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String generate() {

        Long number = jdbcTemplate.queryForObject(
                "SELECT nextval('account_number_seq')",
                Long.class
        );

        if (number == null || number > 999999999999999L) {
            throw new IllegalStateException(
                    "Unable to generate account number"
            );
        }

        return String.format("%015d", number);
    }
}