package ra.edu.business.dao.account;

import ra.edu.business.model.account.Account;

import java.util.List;

public interface AccountDAO {
    List<Account> findAllAccount();
}
