package ra.edu.business.dao.account;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.account.Account;
import ra.edu.business.model.account.Role;
import ra.edu.business.model.account.Status;
import ra.edu.utils.Color;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOImp implements  AccountDAO {
    @Override
    public List<Account> findAllAccount() {
        List<Account> accountList = new ArrayList<>();
        String sql = "{Call GetAllAccounts()}";
        try(Connection conn = ConnectionDB.openConnection();
            CallableStatement callSt = conn.prepareCall(sql);
            ResultSet rs = callSt.executeQuery()){

            while (rs.next()) {
                Account account = new Account();
                account.setId(rs.getInt("id"));
                account.setStudent_id(rs.getInt("student_id"));
                account.setEmail(rs.getString("email"));
                account.setRole(Role.fromString(rs.getString("role")));
                account.setStatus(Status.fromString(rs.getString("status")));
                accountList.add(account);
            }
        } catch (SQLException e) {
            System.err.println(Color.RED + "Lỗi khi truy xuất tất cả học viên: " + e.getMessage() + Color.RESET);
        }

        return accountList;
    }
}
