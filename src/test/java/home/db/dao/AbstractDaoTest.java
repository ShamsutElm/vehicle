package home.db.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class AbstractDaoTest {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractDaoTest.class);
    private static final String userMsg = "MY_MESSAGE";

    private static DaoSQLite daoSQLite;

    @BeforeAll
    static void init() {
        daoSQLite = (DaoSQLite) DaoSQLite.getInstance();
    }

    @ParameterizedTest(name = "[{0}] : {2}")
    @CsvSource(delimiter = ';', value = {
            //name............|results........|description
            "correct_execution; 1, 0, 3, -2, 4; Check results of batch execution.",
            "null_arg         ;               ; Check work with null wrg results.",
    })
    void existingType(String testName, String strBatchResults, String description)
            throws SQLException {
        int[] intBatchResults = convertToIntArray(strBatchResults);
        assertDoesNotThrow(() -> daoSQLite.checkBatchExecution(intBatchResults, userMsg, LOG));
    }

    @ParameterizedTest(name = "[{0}] : {2}")
    @CsvSource(delimiter = ';', value = {
            //name|results|erroMsg
            "execute_failed; 1, 2, 3, 4, -3 ; When executing the batch, result code 'EXECUTE_FAILED' was received.",
            "undocumented  ; 1, 2, -11, 3, 4; When executing the batch, unknown result code '-11' was received.",
    })
    void checkBatchFailTest(String testName, String strBatchResult, String erroMsg) {
        try {
            int[] intBatchResults = convertToIntArray(strBatchResult);
            daoSQLite.checkBatchExecution(intBatchResults, userMsg, LOG);
            fail("Expected java.sql.SQLExeption to be thrown, but nothing was thrown.");
        } catch (SQLException e) {
            String actual = e.getMessage().strip();
            assertTrue(actual.endsWith(erroMsg), """
                    Expected and actual error message does not match:
                    actual: %s
                    expected: %s
                    """.formatted(actual, erroMsg));
        }
    }

    // Think about replacing it by
    // Array.stream(strBatchResults.split(",")).mapToInt(Integer::parseInt).toArray();
    private int[] convertToIntArray(String strBatchResults) {
        if (strBatchResults == null) {
            return null;
        }

        String[] array = strBatchResults.split(",");
        int size = array.length;

        int[] intBatchresults = new int[size];
        for (int i = 0; i < size; i++) {
            intBatchresults[i] = Integer.parseInt(array[i].strip());
        }
        return intBatchresults;
    }
}
