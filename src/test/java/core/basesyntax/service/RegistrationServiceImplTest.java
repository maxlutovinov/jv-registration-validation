package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {
    private static final int NEGATIVE_AGE = -1;
    private static final int AGE_0 = 0;
    private static final int AGE_17 = 17;
    private static final int AGE_18 = 18;
    private static final int AGE_30 = 30;
    private static final String FIVE_SYMBOLS = "qwert";
    private static final String SIX_SYMBOLS = "qwerty";
    private static final String TEN_SYMBOLS = "?-qwerty@7";
    private static final String EXISTING_LOGIN = "here_i_am";
    private static final String NEW_LOGIN = "new_first";
    private static final String EMPTY_STRING = "";
    private static final String SPACE_SYMBOL = " ";
    private static final String TAB_SYMBOL = "  ";
    private static final String SIX_SPACES = "      ";
    private static RegistrationService registrationService;
    private static StorageDao storageDao;
    private User user;

    @BeforeAll
    static void beforeAll() {
        registrationService = new RegistrationServiceImpl();
        storageDao = new StorageDaoImpl();
    }

    @BeforeEach
    void setUp() {
        Storage.people.clear();
        final User existingInStorage = new User();
        existingInStorage.setLogin(EXISTING_LOGIN);
        storageDao.add(existingInStorage);
        user = new User();
        user.setLogin(NEW_LOGIN);
        user.setAge(AGE_18);
        user.setPassword(SIX_SYMBOLS);
    }

    @Test
    void register_nullUser_NotOk() {
        user = null;
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> registrationService.register(user));
        assertEquals("Invalid input user", thrown.getMessage());
    }

    @Test
    void register_nullLogin_NotOk() {
        user.setLogin(null);
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> registrationService.register(user));
        assertEquals("Invalid login", thrown.getMessage());
    }

    @Test
    void register_existingLogin_NotOk() {
        user.setLogin(EXISTING_LOGIN);
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> registrationService.register(user));
        assertEquals("Login already exists", thrown.getMessage());
    }

    @Test
    void register_emptyLogin_NotOk() {
        user.setLogin(EMPTY_STRING);
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> registrationService.register(user));
        assertEquals("Empty login", thrown.getMessage());
    }

    @Test
    void register_spacesLogin_NotOk() {
        user.setLogin(SPACE_SYMBOL);
        RuntimeException thrownSpace = assertThrows(RuntimeException.class,
                () -> registrationService.register(user));
        assertEquals("Empty login", thrownSpace.getMessage());
        user.setLogin(TAB_SYMBOL);
        RuntimeException thrownTab = assertThrows(RuntimeException.class,
                () -> registrationService.register(user));
        assertEquals("Empty login", thrownTab.getMessage());
        user.setLogin(SIX_SPACES);
        RuntimeException thrownSixSpaces = assertThrows(RuntimeException.class,
                () -> registrationService.register(user));
        assertEquals("Empty login", thrownSixSpaces.getMessage());
    }

    @Test
    void register_Age18Password6SymbolsNewLogin_Ok() {
        User actualSuitableUser = registrationService.register(user);
        assertEquals(user, actualSuitableUser);
    }

    @Test
    void register_ageOver18_Ok() {
        user.setAge(AGE_30);
        User actualAge30 = registrationService.register(user);
        assertEquals(user, actualAge30);
    }

    @Test
    void register_nullAge_NotOk() {
        user.setAge(null);
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> registrationService.register(user));
        assertEquals("Invalid age", thrown.getMessage());
    }

    @Test
    void register_ageUnder18_NotOk() {
        user.setAge(AGE_17);
        RuntimeException thrownAge17 = assertThrows(RuntimeException.class,
                () -> registrationService.register(user));
        assertEquals("Age should be at least 18 years old", thrownAge17.getMessage());
        user.setAge(AGE_0);
        RuntimeException thrownAge0 = assertThrows(RuntimeException.class,
                () -> registrationService.register(user));
        assertEquals("Age should be at least 18 years old", thrownAge0.getMessage());
        user.setAge(NEGATIVE_AGE);
        RuntimeException thrownNegativeAge = assertThrows(RuntimeException.class,
                () -> registrationService.register(user));
        assertEquals("Age should be at least 18 years old", thrownNegativeAge.getMessage());
    }

    @Test
    void register_passwordOverSixSymbols_Ok() {
        user.setPassword(TEN_SYMBOLS);
        User actualPassword10 = registrationService.register(user);
        assertEquals(user, actualPassword10);
    }

    @Test
    void register_nullPassword_NotOk() {
        user.setPassword(null);
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> registrationService.register(user));
        assertEquals("Invalid password", thrown.getMessage());
    }

    @Test
    void register_passwordSixSpaces_NotOk() {
        user.setPassword(SIX_SPACES);
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> registrationService.register(user));
        assertEquals("Password should be at least 6 symbols", thrown.getMessage());
    }

    @Test
    void register_passwordUnderSixSymbols_NotOk() {
        user.setPassword(FIVE_SYMBOLS);
        RuntimeException thrownFiveSymbols = assertThrows(RuntimeException.class,
                () -> registrationService.register(user));
        assertEquals("Password should be at least 6 symbols", thrownFiveSymbols.getMessage());
        user.setPassword(EMPTY_STRING);
        RuntimeException thrownEmptyString = assertThrows(RuntimeException.class,
                () -> registrationService.register(user));
        assertEquals("Password should be at least 6 symbols", thrownEmptyString.getMessage());
        user.setPassword(SPACE_SYMBOL);
        RuntimeException thrownSpace = assertThrows(RuntimeException.class,
                () -> registrationService.register(user));
        assertEquals("Password should be at least 6 symbols", thrownSpace.getMessage());
    }
}
