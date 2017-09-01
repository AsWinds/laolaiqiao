package com.codi.laolaiqiao.tools.db.init;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codi.laolaiqiao.api.domain.ArtTeam;
import com.codi.laolaiqiao.api.domain.User;
import com.codi.laolaiqiao.biz.dao.ArtTeamDao;
import com.codi.laolaiqiao.biz.dao.UploadedVideoDao;
import com.codi.laolaiqiao.biz.dao.UserDao;

@Component
public class DBInitService {
	
    private static final String[] user_prefix = {"李大师", "张大师", "宋大师", "欧大师"};
    private static final String[] phone_prefix = {"1301000100", "1302000200", "1303000300", "1304000400"};
    private static final int NUM = 6;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	ArtTeamDao teamDao;
	
	@Autowired
	UploadedVideoDao uploadedVideoDao;
	
	@Transactional
	@PostConstruct
	public void init(){
		initUsers();
		initArtTeams();
	}

	private void initUsers() {
		for (int i = 0; i < user_prefix.length; i++) {
	        for (int j = 0; j < NUM; j++) {
		        String name = user_prefix[i] + j;
		        String phone = phone_prefix[i] + j;
//		        User u = new User(name, phone, "西湖路" + j + "号", User.ROLE_REG_USER, null);
		        User u = null;
		        userDao.save(u);
            }
        }
    }
	
	private void initArtTeams() {
	    for (int i = 0; i < user_prefix.length; i++) {
	        String tname = user_prefix[i] + "艺术团";
	        String location = "上塘路15号" + tname;
	        String detail = tname + "艺术团详情";
	        int fansAmount = 0, likeAmount = 0;
	        Long leaderId = userDao.findIdByPhone(phone_prefix + "0");
	        ArtTeam team = new ArtTeam(tname, location, detail, null, leaderId, fansAmount, likeAmount);
	        teamDao.save(team);
        }
    }
}
