package com.framework.reference;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;

import com.framework.dao.SuperDao;;

/**
 * @author Pradeep Chawadkar
 *
 */
public interface AppReferenceUtils {
	public void setAppReferenceDataOnLogin(HttpSession fsSession, JdbcTemplate jdbcTemplate);
}
