package com.codi.laolaiqiao.api.result;

import com.codi.base.util.DateUtils;
import com.codi.laolaiqiao.common.qiniu.QiNiuManager;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
public class ArtTeamSearchResult {

    /**
     * 团队ID
     */
    private Long id;

    /**
     * 团队名称
     */
    private String name;

    /**
     * 团队详情
     */
    private String detail;

    /**
     *
     * 默认团队头像
     *
     * */
    private String imgUrl;

    /**
     * 团长名称
     */
    private String userName;

    /**
     * 团长ID
     */
    private Long leaderId;

    /**
     * 点赞数量
     */
    private Integer likeAmount;

    /**
     * 粉丝数量
     */
    private Integer fansAmount;

    /**
     * 团队所属区域
     */
    private String address;

    /**
     * 团队数量
     */
    private Integer memberCount;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 图片文件名
     */
    private String imageFileName;

    /**
     * 团队申请状态：
     *   NULL: 未申请
     * ‘auditing’:审核中；
       ‘rejected’:被拒绝；
       ‘approved’：审核通过
     */
    private String joinStatus;

    /**
     * 构造方法
     *
     * @param id       资源ID
     * @param name     艺术团名称
     * @param userName 团长
     */
    public ArtTeamSearchResult(Long id, String name, String detail, String imgUrl, String userName, String address, Integer likeAmount, Integer fansAmount, Long leaderId) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.userName = userName;
        this.address = address;
        this.likeAmount = likeAmount;
        this.fansAmount = fansAmount;
        this.leaderId = leaderId;
        this.imgUrl = QiNiuManager.getImgFileUrl(imgUrl);
        if (this.imgUrl == null) {
	        this.imgUrl = QiNiuManager.getImgFileUrl(QiNiuManager.defaultTeamPhoto);
        }
        this.imageFileName = imgUrl;
    }

    public ArtTeamSearchResult(BigInteger id, String name, Date createTime, String address, String userName, BigInteger memberCount) {
        this.id = id.longValue();
        this.name = name;
        this.createTime = DateUtils.formatDate6(createTime);
        this.userName = userName;
        this.address = address;
        this.memberCount = memberCount.intValue();
    }

    public ArtTeamSearchResult() {}
}
