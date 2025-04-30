package ra.edu.business.service.account;

import ra.edu.business.dao.account.AccountDAOImp;
import ra.edu.business.model.account.Account;

import java.util.List;

public class AccountServiceImp implements AccountService{
    AccountDAOImp accountDAOImp = new AccountDAOImp();
    @Override
    public List<Account> findAllAccount() {
        return accountDAOImp.findAllAccount();
    }
}
