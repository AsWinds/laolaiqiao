package com.codi.laolaiqiao.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codi.laolaiqiao.api.domain.ArtTeam;
import com.codi.laolaiqiao.api.domain.User;
import com.codi.laolaiqiao.api.req.UserInfo;
import com.codi.laolaiqiao.api.result.UserResult;
import com.codi.laolaiqiao.api.service.UserService;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.common.web.result.CollectionResult;
import com.codi.laolaiqiao.common.web.result.SingleDataResult;
import com.codi.laolaiqiao.web.controller.reqModel.UserFollowReq;
import com.laolaiqiao.web.servlet.util.UserUtil;


@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

    /**
     * 查询用户个人信息
     * @return
     */
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public BaseResult detail(@RequestParam(required = false) Long otherUserId) {
        Long userId = UserUtil.getUserId();

        // 用户未登录的场合，且查看自己的主页时，直接返回
        if (otherUserId == null && userId == null) {
            return new BaseResult(true);
        }
        UserResult user = userService.findUserDetail(userId, otherUserId);
        return new SingleDataResult<>(user);
    }

    /**
     * 关注用户/团队
     * @param req
     * @return
     */
    @RequestMapping(value = "follow", method = RequestMethod.POST)
    public BaseResult follow(@Valid UserFollowReq req) {
        userService.follow(req.videoId, UserUtil.getUserId(), 1);
        return new BaseResult(true);
    }

    /**
     * 取消关注用户/团队
     * @param req
     * @return
     */
    @RequestMapping(value = "unFollow", method = RequestMethod.POST)
    public BaseResult unFollow(@Valid UserFollowReq req) {
        userService.follow(req.videoId, UserUtil.getUserId(), -1);
        return new BaseResult(true);
    }

    /**
     * 关注用户
     * @param otherUserId 其他用户
     * @return
     */
    @RequestMapping(value = "followPerson", method = RequestMethod.POST)
    public BaseResult followPerson(Long otherUserId) {
        userService.followPerson(UserUtil.getUserId(), 1, otherUserId);
        return new BaseResult(true);
    }

    /**
     * 取消关注用户
     * @param otherUserId 其他用户
     * @return
     */
    @RequestMapping(value = "unFollowPerson", method = RequestMethod.POST)
    public BaseResult unFollowPerson(Long otherUserId) {
        userService.followPerson(UserUtil.getUserId(), -1, otherUserId);
        return new BaseResult(true);
    }

    /**
     * 关注用户
     * @param followedTeamId 关注的团队ID
     * @return
     */
    @RequestMapping(value = "followTeam", method = RequestMethod.POST)
    public BaseResult followTeam(Long followedTeamId)   {
        userService.followTeam(UserUtil.getUserId(), 1, followedTeamId);
        return new BaseResult(true);
    }

    /**
     * 我的关注
     * @param size
     * @param page
     * @return
     */
    @RequestMapping(value = "myFollow", method = RequestMethod.GET)
    public BaseResult myFollow(Integer size, Integer page) {
        Page<User> users = userService.queryFans(size, page, UserUtil.getUserId(), 0);
        return new CollectionResult<>(users);
    }

    /**
     * 我的粉丝
     * @param size
     * @param page
     * @return
     */
    @RequestMapping(value = "myFans", method = RequestMethod.GET)
    public BaseResult myFans(Integer size, Integer page) {
        Page<User> users = userService.queryFans(size, page, UserUtil.getUserId(), 1);
        return new CollectionResult<>(users);
    }

    /**
     * 我关注的团队
     * @param size
     * @param page
     * @return
     */
    @RequestMapping(value = "myFollowedTeam", method = RequestMethod.GET)
    public BaseResult myFollowedTeams(Integer size, Integer page) {
        Page<ArtTeam> users = userService.myFollowedTeam(UserUtil.getUserId(), page, size);
        return new CollectionResult<>(users);
    }
    
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResult updateUserDetail(@Valid UserInfo info){
    	return new SingleDataResult<>(userService.updateUser(UserUtil.getUserId(), info));
    }

}
